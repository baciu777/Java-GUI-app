package ChangeEvent;

import domain.Friendship;

public class EventChangeEvent implements Event {

    private Event data;
    private String stringT;

    public EventChangeEvent( Event  data,String stringT) {

        this.data = data;
        this.stringT=stringT;
    }
    public EventChangeEvent( Event  data) {

        this.data = data;
        this.stringT=null;
    }




    public Event getData() {
        return data;
    }
    public String getType() {
        return stringT;
    }

}