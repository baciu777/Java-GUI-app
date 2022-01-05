package com.example.network5;

import domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.TilePane;
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

public class PeopleController extends MenuController{
    @FXML
    TextField textFieldSearch;

    @FXML
    ScrollPane scroll;
    @FXML
    TilePane peopleTile;
    ObservableList<DtoUser> modelUser = FXCollections.observableArrayList();



    public void set(ServiceUser service, ServiceMessage mess, ServiceFriendship serviceFriendshipNew, ServiceFriendshipRequest serviceFriendRequestt, Stage stage, Page user) {
        this.serviceUser = service;
        this.serviceMessage = mess;
        this.serviceF = serviceFriendshipNew;

        this.serviceFr = serviceFriendRequestt;
        this.dialogStage = stage;
        this.userLogin = user;

        initModelUser();
        setLabelName();
        setStylePane();

    }
    private void setStylePane()
    {
        peopleTile.setHgap(30);
        peopleTile.setVgap(30);
        peopleTile.setPrefColumns(2);
       scroll.setStyle("-fx-background: transparent;" +
               "-fx-background-color: transparent;");


    }

    @FXML
    public void initialize() {


        textFieldSearch.textProperty().addListener(o -> handleFilter());
    }



    public void initModelUser() {
        Iterable<User> users = serviceUser.printUs();

        peopleTile.getChildren().clear();

        for(User u:users)
        {

            OneUserPeopleController oneUser = new OneUserPeopleController();
            oneUser.set(userLogin,u,serviceFr,serviceF,serviceUser,this);
            peopleTile.getChildren().add(oneUser.getBox());

        }
    }
    public void handleFilter()
    {
        Predicate<User> p1 = n -> n.getFirstName().startsWith(textFieldSearch.getText()) || n.getLastName().startsWith(textFieldSearch.getText());
        Iterable<User> users = serviceUser.printUs();
        List<User> userForTile = StreamSupport.stream(users.spliterator(), false)
                .filter(p1).collect(Collectors.toList());

        peopleTile.getChildren().clear();

        for(User u:userForTile)
        {
            OneUserPeopleController oneUser = new OneUserPeopleController();
            oneUser.set(userLogin,u,serviceFr,serviceF,serviceUser,this);
            peopleTile.getChildren().add(oneUser.getBox());
        }
    }


}
