package com.example.network5;

import domain.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import service.ServiceFriendship;
import service.ServiceFriendshipRequest;
import service.ServiceUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OneUserPeopleController {
    HBox container;
    VBox nameStatusContainer;
    Button button1;
    Button button2;
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
        nameStatusContainer = new VBox();
       setLogic();
       setStyle();
    }
    public void setStyle()
    {
        container.setMinSize(270,70);
        container.setMaxSize(270,70);
        container.setStyle("-fx-background-color: #2d2d31;" +
                "-fx-padding: 10 20 10 20;" +
                "-fx-background-radius: 20");
        userName.setStyle("-fx-text-fill:#e9e3d1;" +
                "-fx-font-family: Century Gothic; ");
        relation.setStyle("-fx-text-fill:#e9e3d1;" +
                "-fx-font-family: Century Gothic;" +
                "-fx-opacity: 70% ");
        button1.setStyle("-fx-background-color: transparent; -fx-border-color: #e9e3d1;" +
                "-fx-text-fill:#e9e3d1; -fx-font-family: Century Gothic;");
        button2.setStyle("-fx-background-color: transparent; -fx-border-color: #e9e3d1;" +
                "-fx-text-fill:#e9e3d1; -fx-font-family: Century Gothic;");
    }
    public void setLogic()
    {
        userName.setText(peopleUser.getFirstName() + " " + peopleUser.getLastName());

        relation.setText("not friends");
        if (servFriend.areFriends(loggedUser, peopleUser))
            relation.setText("friend");
        String reqStatus = servRequests.get_request_status(loggedUser.getId(), peopleUser.getId());
        if (reqStatus != null)
            relation.setText(reqStatus);
        if (Objects.equals(loggedUser.getId(), peopleUser.getId()))
            relation.setText("you");


        nameStatusContainer.getChildren().add(userName);
        nameStatusContainer.getChildren().add(relation);
        container.getChildren().add(nameStatusContainer);

        setButtons();

    }
    public void setButtons()
    {
        button1 = new Button();
        button2 = new Button();
        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);
        if (Objects.equals(relation.getText(), "not friends")) {
            button1.setText("Send Request");
            button1.setOnAction(event -> {
                try {
                    handleSendRequest();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            container.getChildren().add(region1);
            container.getChildren().add(button1);
        }
        if(Objects.equals(relation.getText(), "requested"))
        {
            button1.setText("Delete Request");
            button1.setOnAction(event -> {
                try {
                    handleUndoRequest();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });
            container.getChildren().add(region1);
            container.getChildren().add(button1);
        }
        if(Objects.equals(relation.getText(), "friend"))
        {
            button1.setText("Delete");
            button1.setVisible(false);
            container.setOnMousePressed(event->{button1.setVisible(true);});
            container.setOnMouseExited(event->{button1.setVisible(false);});
            button1.setOnAction(event->{deleteFriend();});
            container.getChildren().add(region1);
            container.getChildren().add(button1);
        }
        if(Objects.equals(relation.getText(), "pending"))
        {
            button1.setText("Accept");
            button2.setText("Decline");

            button1.setOnAction(event->{
                try {
                    acceptReq();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            button2.setOnAction(event->{
                try {
                    rejectReq();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            container.getChildren().add(region1);
            VBox butonsContainer = new VBox();
            butonsContainer.setSpacing(5);
            butonsContainer.getChildren().add(button1);
            butonsContainer.getChildren().add(button2);
            container.getChildren().add(butonsContainer);
        }


    }
    public void acceptReq() throws Exception {
        Tuple<Long, Long> tuple = new Tuple<>( peopleUser.getId(),loggedUser.getId());

        FriendRequest friendRequest = servRequests.findOne(tuple);
        User u1 = servUser.findOne(peopleUser.getId());
        DtoFriendReq newRequest = new DtoFriendReq( u1.getFirstName() + " " + u1.getLastName(),loggedUser.getFirstName() + " " + loggedUser.getLastName(), friendRequest.getDate(), friendRequest.getStatus());
        loggedUser.removeFrRequestRec(newRequest);

        loggedUser.addFriendPage(peopleUser);

        servRequests.addFriend(loggedUser.getId(),peopleUser.getId());
        control.initModelUser();
    }
    public void rejectReq() throws Exception {


        Tuple<Long, Long> tuple = new Tuple<>( peopleUser.getId(),loggedUser.getId());

        FriendRequest friendRequest = servRequests.findOne(tuple);
        User u1 = servUser.findOne(peopleUser.getId());
        DtoFriendReq newRequest = new DtoFriendReq(u1.getFirstName() + " " + u1.getLastName(),loggedUser.getFirstName() + " " + loggedUser.getLastName(), friendRequest.getDate(), friendRequest.getStatus());
        loggedUser.removeFrRequestRec(newRequest);

        servRequests.rejectRequest(loggedUser.getId(),peopleUser.getId());

        control.initModelUser();
    }
    public void deleteFriend()
    {
        servFriend.deleteFriend(peopleUser.getId(),loggedUser.getId());
        // userLogged.deleteFriend(userfriend);
        List<User> newFR=new ArrayList<>();
        //stergem si din prietenii userului logat(page)
        loggedUser.removeFriend(peopleUser);
        loggedUser.setFriends(newFR);
        control.initModelUser();
    }
    public void handleUndoRequest()
    {
        Tuple<Long, Long> tuple = new Tuple<>(loggedUser.getId(), peopleUser.getId());

        FriendRequest friendRequest = servRequests.findOne(tuple);
        User u1 = servUser.findOne(peopleUser.getId());
        DtoFriendReq newRequest = new DtoFriendReq(loggedUser.getFirstName() + " " + loggedUser.getLastName(), u1.getFirstName() + " " + u1.getLastName(), friendRequest.getDate(), friendRequest.getStatus());
        loggedUser.removeFrRequestSent(newRequest);

        servRequests.deleteRequest( peopleUser.getId(),loggedUser.getId());

        control.initModelUser();
    }
    public void handleSendRequest() throws Exception {
        servRequests.sendRequest(loggedUser.getId(), peopleUser.getId());
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
