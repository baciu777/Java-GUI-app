package ChangeEvent;

import domain.Message;

public class MessageTaskChangeEvent implements Event {

    private Message data;

    public MessageTaskChangeEvent( Message data) {

        this.data = data;
    }




    public Message getData() {
        return data;
    }

}