package plus.yunfei;

import java.util.function.Consumer;

public class User implements EventListener {

    private Consumer<String> consumer;

    private String name;

    public User(String name, Consumer<String> consumer) {
        this.name = name;
        this.consumer = consumer;
    }

    public void receiveInfo(String info) {
        consumer.accept(info);
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof WeatherUpdateEvent) {
            String source = (String) event.source();
            receiveInfo(source);
        }
    }
}
