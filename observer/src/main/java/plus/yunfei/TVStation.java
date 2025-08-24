package plus.yunfei;

import java.util.*;

public class TVStation {

    private Set<EventListener> listeners = new HashSet<>();

    private Map<Class<? extends Event>, List<EventListener>> listenerMap =new HashMap<>();


    public void subscribe(EventListener listener,Class<? extends Event> eventClass) {
        listenerMap.computeIfAbsent(eventClass,k->new  ArrayList<>()).add(listener);
    }

    public void publish(Event event) {
        Class<? extends Event> eventClass = event.getClass();
        List<EventListener> eventListeners = listenerMap.get(eventClass);
        if (eventListeners != null) {
            for (EventListener eventListener : eventListeners) {
                eventListener.onEvent(event);
            }
        }
    }
}