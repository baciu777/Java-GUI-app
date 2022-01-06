package ChangeEvent;

import domain.FriendRequest;
import domain.Message;

public class FrRequestChangeEvent implements Event {

    private FriendRequest data;
    private String type;
    public FrRequestChangeEvent( FriendRequest data) {

        this.data = data;
    }
    public FrRequestChangeEvent( FriendRequest data,String type) {

        this.data = data;
        this.type=type;
    }




    public FriendRequest getData() {
        return data;
    }
    public String getType(){return type;}

}

