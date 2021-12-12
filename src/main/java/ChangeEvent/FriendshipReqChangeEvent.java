package ChangeEvent;

import domain.Friendship;

public class FriendshipReqChangeEvent extends Event{
    private ChangeEventType type;
    private Friendship data, oldData;

    public FriendshipReqChangeEvent(ChangeEventType type, Friendship data) {
        this.type = type;
        this.data = data;
    }
    public FriendshipReqChangeEvent(ChangeEventType type, Friendship data, Friendship oldData) {
        this.type = type;
        this.data = data;
        this.oldData=oldData;
    }

    public ChangeEventType getType() {
        return type;
    }

    public Friendship getData() {
        return data;
    }

    public Friendship getOldData() {
        return oldData;
    }

}
