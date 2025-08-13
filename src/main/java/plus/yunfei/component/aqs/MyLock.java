package plus.yunfei.component.aqs;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * @author houyunfei
 */
public class MyLock {

    AtomicBoolean flag = new AtomicBoolean(false);

    Thread owner = null;

    AtomicReference<Node> head = new AtomicReference<>(new Node());
    AtomicReference<Node> tail = new AtomicReference<>(head.get());

    void lock() {
        // 如果直接能获取到锁，则直接返回
        if (flag.compareAndSet(false, true)) {
            System.out.println(Thread.currentThread().getName() + "直接拿到了锁");
            owner = Thread.currentThread();
            return;
        }
        // 没有拿到锁，安全的把自己放在尾节点
        Node current = new Node();
        current.thread = Thread.currentThread();
        while (true) {
            Node currentTail = tail.get();
            if (tail.compareAndSet(currentTail, current)) {
                System.out.println(Thread.currentThread().getName() + "放入链表尾部");
                current.pre = currentTail;
                currentTail.next = current;
                break;
            }
        }

        while (true) {
            // 需要满足一些条件才能继续执行
            // head -> A --> xxx
            if (current.pre == head.get() && flag.compareAndSet(false, true)) {
                // 成功获取锁
                owner = Thread.currentThread();
                // 需要把头节点指向当前节点
                head.set(current);
                // 需要把前一个节点的next指向null 断掉 head->A 和A->head
                current.pre.next = null;
                current.pre = null;
                System.out.println(Thread.currentThread().getName() + "被唤醒了，拿到了锁");
                return;
            }
            LockSupport.park();
        }
    }

    void unlock() {
        if (Thread.currentThread() != this.owner) {
            throw new IllegalMonitorStateException("当前线程不是锁的持有者，无法释放锁");
        }
        Node headNode = head.get();
        Node next = headNode.next;
        flag.set(false);
        // 接下来可能出现竞争
        if (next != null) {
            System.out.println(Thread.currentThread().getName() + "唤醒了 " + next.thread.getName());
            LockSupport.unpark(next.thread);
        }
    }

    class Node {
        Node pre;
        Node next;
        Thread thread;
    }
}
