package ChangeEvent;

import domain.FriendRequest;
import domain.Message;

public class FrRequestChangeEvent implements Event {

    private FriendRequest data;

    public FrRequestChangeEvent( FriendRequest data) {

        this.data = data;
    }




    public FriendRequest getData() {
        return data;
    }

}

