package plus.yunfei;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author houyunfei
 */
public class CountPlugin implements MyPlugin {

    AtomicInteger count = new AtomicInteger(0);

    @Override
    public void beforeGetTime() {
        System.out.println("计数器插件：" + count.getAndDecrement());
    }
}
