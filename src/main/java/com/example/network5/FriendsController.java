package com.example.network5;

import ChangeEvent.FrRequestChangeEvent;
import ChangeEvent.FriendChangeEvent;
import domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import observer.Observer;

import service.*;


import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendsController extends MenuController {
    @FXML
    TextField textFieldSearch;
    @FXML
    ScrollPane scroll;

    @FXML
    TilePane friendsTile;


    ObservableList<User> modelFriendship = FXCollections.observableArrayList();
    Observer<FrRequestChangeEvent> obsFrrPriv=new Observer<FrRequestChangeEvent>() {
        @Override
        public void update(FrRequestChangeEvent frRequestChangeEvent) {



            System.out.println("daaaaaaaaaaaa");
            FriendRequest frR = frRequestChangeEvent.getData();
            if (Objects.equals(frR.getId().getRight(), userLogin.getId()) && Objects.equals(frR.getStatus(), "APPROVED"))
                initModelFriendship();
        }
    };

    public void set(ServiceUser service, ServiceMessage mess, ServiceFriendship serviceFriendshipNew, ServiceFriendshipRequest serviceFriendRequestt, ServiceEvent servEvent, Stage stage, Page user) {

        this.serviceUser = service;
        this.serviceMessage = mess;
        this.serviceF = serviceFriendshipNew;

        this.serviceFr = serviceFriendRequestt;
        this.dialogStage = stage;
        this.serviceEvent = servEvent;
        this.userLogin = user;
        //serviceMessage.addObserver(obsMess);
        serviceFr.addObserver(obsFrrPriv);
        serviceF.addObserver(frObs);
        initModelFriendship();
        textFieldSearch.textProperty().addListener(o -> handleFilter());
        setLabelName();
        setStylePane();

    }
    private void setStylePane()
    {
        friendsTile.setHgap(30);
        friendsTile.setVgap(20);
        friendsTile.setPrefColumns(2);
        scroll.setStyle("-fx-background: transparent;" +
                "-fx-background-color: transparent;");
    }
    @FXML
    public void initialize() {




    }


    public void initModelFriendship() {


        List<User> friendshipsList=userLogin.getFriends();

        modelFriendship.setAll(friendshipsList);
        friendsTile.getChildren().clear();
        for(User u:friendshipsList)
        {
           OneUserFriendController oneUser = new OneUserFriendController();
           oneUser.set(u,userLogin,serviceF,this);

           friendsTile.getChildren().add(oneUser.getBox());
        }
    }

    public void handleFilter()
    {
        Predicate<User> p1 = n -> n.getFirstName().startsWith(textFieldSearch.getText());
        Iterable<User> users = serviceF.friends(userLogin.getId());
        List<User> friendshipsList = StreamSupport.stream(users.spliterator(), false)
                .filter(p1)
                .collect(Collectors.toList());
        modelFriendship.setAll(friendshipsList);
        friendsTile.getChildren().clear();
        for(User u:friendshipsList)
        {
            OneUserFriendController oneUser = new OneUserFriendController();
            oneUser.set(u,userLogin,serviceF,this);
            friendsTile.getChildren().add(oneUser.getBox());
        }
    }


    Observer<FriendChangeEvent> frObs=new Observer<FriendChangeEvent>() {
        @Override
        public void update(FriendChangeEvent friendChangeEvent) {

            if (Objects.equals(friendChangeEvent.getType(), "del") && Objects.equals(friendChangeEvent.getData().getId().getRight(), userLogin.getId())) {
                initModelFriendship();
            }

        }
    };

    @FXML
    public void handleCancel() {
        serviceFr.removeObserver(obsFrrPriv);
        serviceF.removeObserver(frObs);
        showWelcomeEditDialog();
    }

    @FXML
    public void handleFriendRequests() {
        serviceFr.removeObserver(obsFrrPriv);
        serviceF.removeObserver(frObs);
        showFriendReqEditDialog();
    }

    @FXML
    public void handleFriends() {
        serviceFr.removeObserver(obsFrrPriv);
        serviceF.removeObserver(frObs);
        showFriendsDialog();
    }

    @FXML
    public void handlePeople() {
        serviceFr.removeObserver(obsFrrPriv);

        serviceF.removeObserver(frObs);
        showPeopleDialog();
    }

    @FXML
    public void handleChats() {
        serviceFr.removeObserver(obsFrrPriv);

        serviceF.removeObserver(frObs);
        showChatsEditDialog();
    }

    @FXML
    public void handleEvents() {
        serviceFr.removeObserver(obsFrrPriv);
        serviceF.removeObserver(frObs);
        showEventsEditDialog();
    }

    @FXML
    public void handleNotifications() {
        serviceFr.removeObserver(obsFrrPriv);
        serviceF.removeObserver(frObs);
        showNotifEditDialog();
    }
}
