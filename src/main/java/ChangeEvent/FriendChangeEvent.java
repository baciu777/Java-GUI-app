package ChangeEvent;

import domain.FriendRequest;
import domain.Friendship;

public class FriendChangeEvent implements Event {

    private Friendship data;
    private String stringT;

    public FriendChangeEvent( Friendship  data,String stringT) {

        this.data = data;
        this.stringT=stringT;
    }




    public Friendship getData() {
        return data;
    }
    public String getType() {
        return stringT;
    }

}