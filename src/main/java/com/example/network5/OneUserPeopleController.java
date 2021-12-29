package com.example.network5;

import domain.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import service.ServiceFriendship;
import service.ServiceFriendshipRequest;
import service.ServiceUser;

import java.util.Objects;

public class OneUserPeopleController {
    HBox container;
    Button sendRequest;
    Label relation;
    Label userName;
    Page loggedUser;
    User peopleUser;
    ServiceFriendshipRequest servRequests;
    ServiceFriendship servFriend;
    ServiceUser servUser;
    PeopleController control;

    public void set(Page loggedUser, User peopleUser, ServiceFriendshipRequest serv, ServiceFriendship servFriend, ServiceUser servUser, PeopleController control) {
        this.loggedUser = loggedUser;
        this.peopleUser = peopleUser;
        this.servRequests = serv;
        this.servFriend = servFriend;
        this.servUser = servUser;
        this.control = control;
        container = new HBox();
        userName = new Label();
        relation = new Label();
        sendRequest = new Button("Send Request");
        userName.setText(peopleUser.getFirstName() + " " + peopleUser.getLastName());

        relation.setText("not friends");
        if (servFriend.areFriends(loggedUser, peopleUser))
            relation.setText("friend");
        String reqStatus = servRequests.get_request_status(loggedUser.getId(), peopleUser.getId());
        if (reqStatus != null)
            relation.setText(reqStatus);
        if (Objects.equals(loggedUser.getId(), peopleUser.getId()))
            relation.setText("you");


        container.getChildren().add(userName);
        container.getChildren().add(relation);
        container.getChildren().add(sendRequest);
        if (!Objects.equals(relation.getText(), "not friends")) {
            sendRequest.setVisible(false);
        }
        sendRequest.setOnAction(event -> {
            try {
                handleSendRequest();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void handleSendRequest() throws Exception {
        servRequests.addFriend(loggedUser.getId(), peopleUser.getId());
        Tuple<Long, Long> tuple = new Tuple<>(loggedUser.getId(), peopleUser.getId());

        FriendRequest friendRequest = servRequests.findOne(tuple);
        User u1 = servUser.findOne(peopleUser.getId());
        DtoFriendReq newRequest = new DtoFriendReq(loggedUser.getFirstName() + " " + loggedUser.getLastName(), u1.getFirstName() + " " + u1.getLastName(), friendRequest.getDate(), friendRequest.getStatus());
        loggedUser.addRequestSent(newRequest);

        control.initModelUser();
    }

    public HBox getBox() {
        return container;
    }
}