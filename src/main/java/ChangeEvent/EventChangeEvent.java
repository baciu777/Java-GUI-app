package ChangeEvent;


public class EventChangeEvent implements Event {

    private domain.Event data;
    private String stringT;

   public EventChangeEvent( domain.Event data) {

        this.data = data;
        this.stringT=null;
    }




    public domain.Event getData() {
        return data;
    }
    public String getType() {
        return stringT;
    }

}