package plus.yunfei;

public class WeatherUpdateEvent extends BaseEvent {

    private final String info;

    public WeatherUpdateEvent(String info) {
        this.info = info;
    }

    @Override
    public long timestamp() {
        return 0;
    }

    @Override
    public Object source() {
        return info;
    }
}
