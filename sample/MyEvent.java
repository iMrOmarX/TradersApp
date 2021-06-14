package sample;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

public class MyEvent  extends Event {

    public MyEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public MyEvent(Object o, EventTarget eventTarget, EventType<? extends Event> eventType) {
        super(o, eventTarget, eventType);
    }
}
