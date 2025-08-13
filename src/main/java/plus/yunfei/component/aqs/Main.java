package plus.yunfei.component.aqs;

import java.util.ArrayList;
import java.util.List;

/**
 * @author houyunfei
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        List<Thread> threadList = new ArrayList<>();
        int[] a = new int[]{1000};
        MyLock lock = new MyLock();
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(() -> {
                lock.lock();
                for (int j = 0; j < 10; j++) {
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    a[0]--;
                }
                lock.unlock();
            });
            threadList.add(thread);
        }
        for (Thread thread : threadList) thread.start();
        for (Thread thread : threadList) thread.join();
        System.out.println("a[0] = " + a[0]);
    }
}
