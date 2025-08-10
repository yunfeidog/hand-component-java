package plus.yunfei.component.threadpool;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

interface RejectHandler {
    void reject(Runnable task, ThreadPool threadPool);
}

class ThrowRejectHandler implements RejectHandler {

    @Override
    public void reject(Runnable task, ThreadPool threadPool) {
        throw new RuntimeException("Task rejected: " + task.toString() + " because thread pool is full");
    }
}

public class ThreadPool {
    int codePoolSize; // the number of core threads
    int maxSize; // the maximum number of threads
    int timeout;  //the maximum time a thread can be idle before being terminated
    TimeUnit timeUnit; // the time unit for the timeout
    BlockingQueue<Runnable> taskQueue; // the queue to hold tasks
    RejectHandler rejectHandler; // the handler for rejected tasks
    List<Thread> coreThreads; // the list of core threads
    List<Thread> supportThreads; // the list of support threads

    public ThreadPool(int codePoolSize, int maxSize, int timeout, TimeUnit timeUnit, RejectHandler rejectHandler, BlockingQueue<Runnable> taskQueue) {
        this.codePoolSize = codePoolSize;
        this.maxSize = maxSize;
        this.rejectHandler = rejectHandler;
        this.taskQueue = taskQueue;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        coreThreads = new ArrayList<>();
        supportThreads = new ArrayList<>();
    }

    public void execute(Runnable task) {
        if (coreThreads.size() < codePoolSize) {
            Thread thread = new CoreThread(task);
            coreThreads.add(thread);
            thread.start();
            return;
        }
        // if the number of core threads is full, we need to check if we can add a support thread
        if (taskQueue.offer(task)) return; // if insert to the queue is successful, we can return
        if (coreThreads.size() + supportThreads.size() < maxSize) {
            Thread thread = new SupportThread(task);
            supportThreads.add(thread);
            thread.start();
            return;
        }
        if (!taskQueue.offer(task)) {
            // if the task queue is full, we need to reject the task
            rejectHandler.reject(task, this);
        }
    }

    class CoreThread extends Thread {

        Runnable firstTask;

        public CoreThread(Runnable firstTask) {
            this.firstTask = firstTask;
        }

        @Override
        public void run() {
            firstTask.run();
            while (true) {
                try {
                    Runnable task = taskQueue.take();
                    task.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }

    class SupportThread extends Thread {
        Runnable firstTask;

        public SupportThread(Runnable firstTask) {
            this.firstTask = firstTask;
        }

        @Override
        public void run() {
            firstTask.run();
            while (true) {
                try {
                    Runnable task = taskQueue.poll(timeout, timeUnit);
                    if (task == null) break;
                    task.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

class Test {
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss SSS");

    public static String now() {
        return LocalDateTime.now().format(formatter);
    }

    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(2, 4, 1,
                TimeUnit.SECONDS, new ThrowRejectHandler(), new ArrayBlockingQueue<>(2));
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            threadPool.execute(() -> {
                System.out.println(now() + Thread.currentThread().getName() + " " + finalI);
            });
        }
        System.out.println("here is the main thread which not be blocked");
    }
}
