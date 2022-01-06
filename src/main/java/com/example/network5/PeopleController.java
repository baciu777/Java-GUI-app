package com.example.network5;

import ChangeEvent.FrRequestChangeEvent;
import ChangeEvent.FriendChangeEvent;
import domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import observer.Observer;
import service.*;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PeopleController extends MenuController  {
    @FXML
    TextField textFieldSearch;
    @FXML
    TilePane peopleTile;


    ObservableList<DtoUser> modelUser = FXCollections.observableArrayList();
    Observer<FriendChangeEvent> frObs=new Observer<FriendChangeEvent>() {
        @Override
        public void update(FriendChangeEvent friendChangeEvent) {
            System.out.println("dell");
            initModelUser();

        }
    };
    Observer<FrRequestChangeEvent> obsFrrPriv=new Observer<FrRequestChangeEvent>() {
        @Override
        public void update(FrRequestChangeEvent frRequestChangeEvent) {


            initModelUser();
        }
    };


    public void set(ServiceUser service, ServiceMessage mess, ServiceFriendship serviceFriendshipNew, ServiceFriendshipRequest serviceFriendRequestt, Stage stage, Page user) {
        this.serviceUser = service;
        this.serviceMessage = mess;
        this.serviceF = serviceFriendshipNew;

        this.serviceFr = serviceFriendRequestt;
        this.dialogStage = stage;
        this.userLogin = user;
        this.serviceFr.addObserver(obsFrrPriv);
        this.serviceF.addObserver(frObs);
        //serviceFr.addObserver(frObs);
        //serviceMessage.addObserver(obsMess);
        //serviceFr.addObserver(this);

        initModelUser();
        setLabelName();
        setStylePane();

    }

    private void setStylePane() {
        peopleTile.setHgap(30);
        peopleTile.setVgap(20);
        peopleTile.setPrefColumns(2);

    }

    @FXML
    public void initialize() {


        populateUsers();
        textFieldSearch.textProperty().addListener(o -> handleFilter());
    }

    @FXML
    public void populateUsers() {


    }



    public void handleFilter() {
        Predicate<User> p1 = n -> n.getFirstName().startsWith(textFieldSearch.getText()) || n.getLastName().startsWith(textFieldSearch.getText());
        Iterable<User> users = serviceUser.printUs();
        List<User> userForTile = StreamSupport.stream(users.spliterator(), false)
                .filter(p1).collect(Collectors.toList());

        peopleTile.getChildren().clear();

        for (User u : userForTile) {
            OneUserPeopleController oneUser = new OneUserPeopleController();
            oneUser.set(userLogin, u, serviceFr, serviceF, serviceUser, this);
            peopleTile.getChildren().add(oneUser.getBox());
        }
    }

    public void handleSend() {


    }

    @FXML
    public void handleSendRequest() {


    }

    //@Override
   // public void update(FriendChangeEvent friendChangeEvent) {
      //  System.out.println("doaaamne ajutaaa");
      //  if(Objects.equals(friendChangeEvent.getData().getId().getRight(), userLogin.getId())) {
      //      if (Objects.equals(friendChangeEvent.getType(), "add")) {
      //          System.out.println("intru");
       //         User u1 = serviceUser.findOne(friendChangeEvent.getData().getId().getLeft());
        //        userLogin.addFriend(u1);
        //    }
        //    if (Objects.equals(friendChangeEvent.getType(), "del")) {
         //       User u1 = serviceUser.findOne(friendChangeEvent.getData().getId().getLeft());
         //       userLogin.deleteFriend(u1);
      //      }
    //    }
 //   }


    public void initModelUser() {
        Iterable<User> users = serviceUser.printUs();

        peopleTile.getChildren().clear();

        for (User u : users) {

            OneUserPeopleController oneUser = new OneUserPeopleController();
            oneUser.set(userLogin, u, serviceFr, serviceF, serviceUser, this);
            peopleTile.getChildren().add(oneUser.getBox());

        }
    }


    @FXML
    public void handleCancel() {
        serviceF.removeObserver(frObs);
        serviceFr.removeObserver(obsFrrPriv);

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
