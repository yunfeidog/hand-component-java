package plus.yunfei.component.schedule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.LockSupport;

class Job implements Comparable<Job> {
    long startTime;
    Runnable task;
    long delay;

    public Job(long delay, long startTime, Runnable task) {
        this.delay = delay;
        this.startTime = startTime;
        this.task = task;
    }

    @Override
    public int compareTo(Job o) {
        return Long.compare(startTime, o.startTime);
    }
}


public class ScheduleService {

    ExecutorService executorService;
    Trigger trigger;


    public ScheduleService() {
        trigger = new Trigger();
        executorService = Executors.newFixedThreadPool(5);
    }

    public void schedule(Runnable task, long delay) {
        Job job = new Job(delay, System.currentTimeMillis() + delay, task);
        trigger.queue.offer(job);
        trigger.wakeUp();
    }

    class Trigger {
        PriorityBlockingQueue<Job> queue;
        Thread thread;

        public Trigger() {
            this.queue = new PriorityBlockingQueue<>();
            this.thread = new Thread(() -> {
                while (true) {
                    while (queue.isEmpty()) LockSupport.park(); // wait for a job
                    Job latelyJob = queue.peek(); // get the job with the earliest execution time
                    if (latelyJob.startTime < System.currentTimeMillis()) {
                        latelyJob = queue.poll(); // remove the job from the queue
                        executorService.execute(latelyJob.task);
                        // create a new Job with the same delay and a new start time = scheduled time + delay
                        Job nextJob = new Job(latelyJob.delay, latelyJob.startTime + latelyJob.delay, latelyJob.task);
                        queue.offer(nextJob);
                    } else {
                        LockSupport.parkUntil(latelyJob.startTime); // wait until the job's start time
                    }
                }
            });
            this.thread.start();
            System.out.println("trigger start");
        }

        void wakeUp() {
            LockSupport.unpark(thread);
        }

    }
}

class Test {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");

    public static void main(String[] args) throws InterruptedException {
        ScheduleService scheduleService = new ScheduleService();
        scheduleService.schedule(() -> {
            System.out.println(now() + " - execute 100ms task");
        }, 100);

        Thread.sleep(1000);
        System.out.println(now() + " - add a 200ms task");

        scheduleService.schedule(() -> {
            System.out.println(now() + " - execute 200ms task");
        }, 200);
    }

    public static String now() {
        return LocalDateTime.now().format(formatter);
    }
}