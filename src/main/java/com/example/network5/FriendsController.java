package com.example.network5;

import domain.DtoMessage;
import domain.Page;
import domain.User;
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
import service.ServiceFriendship;
import service.ServiceFriendshipRequest;
import service.ServiceMessage;
import service.ServiceUser;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendsController extends MenuController{
    @FXML
    TextField textFieldSearch;


    @FXML
    TilePane friendsTile;


    ObservableList<User> modelFriendship = FXCollections.observableArrayList();

    public void set(ServiceUser service, ServiceMessage mess, ServiceFriendship serviceFriendshipNew, ServiceFriendshipRequest serviceFriendRequestt, Stage stage, Page user) {

        this.serviceUser = service;
        this.serviceMessage = mess;
        this.serviceF = serviceFriendshipNew;

        this.serviceFr = serviceFriendRequestt;
        this.dialogStage = stage;
        this.userLogin = user;

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



}
