package plus.yunfei;

import java.util.Random;

public class WeatherStation {


    private final TVStation tvStation;

    public WeatherStation(TVStation tvStation) {
        this.tvStation = tvStation;
    }

    public String getInfo() {
        if (new Random().nextBoolean()) {
            return "晴天";
        }
        return "雨天";
    }

    public void start() throws InterruptedException {
        while (true) {
            String info = getInfo();
            WeatherUpdateEvent event = new WeatherUpdateEvent(info);
            tvStation.publish(event);
            Thread.sleep(3000);
        }
    }


    public static void main(String[] args) throws InterruptedException {
        TVStation tvStation = new TVStation();
        WeatherStation station = new WeatherStation(tvStation);
        User cxk = new User("cxk", (info) -> {
            if (info.equals("晴天")) {
                System.out.println("今天是晴天，cxk出去玩");
            } else {
                System.out.println("cxk呆在家里");
            }
        });

        User tom = new User("tom", (info) -> {
            if (info.equals("晴天")) {
                System.out.println("tom出去完");
            }
        });

        tvStation.subscribe(cxk,WeatherUpdateEvent.class);
        tvStation.subscribe(tom, WeatherUpdateEvent.class);
        station.start();
    }

}


