package ChangeEvent;


import domain.Entity;
import domain.Friendship;

public class Event {
    private ChangeEventType type;
    private Entity data, oldData;

    public Event(ChangeEventType type, Entity data) {
        this.type = type;
        this.data = data;
    }
    public Event(ChangeEventType type, Entity data, Entity oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Entity getData() {
        return data;
    }

    public Entity getOldData() {
        return oldData;
    }

}