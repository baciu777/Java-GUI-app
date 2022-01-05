package com.example.network5;

import domain.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.*;

import java.io.IOException;

public class MenuController  {




    protected ServiceFriendship serviceF;
    protected ServiceFriendshipRequest serviceFr;
    protected ServiceUser serviceUser;
    protected ServiceMessage serviceMessage;
    protected ServiceEvent serviceEvent;
    @FXML
    protected Label idName;

    Stage dialogStage;
    Page userLogin;

    public void setService(ServiceUser service, ServiceMessage mess,ServiceFriendship serviceFriendshipNew,ServiceFriendshipRequest serviceFriendRequestt,ServiceEvent servEvent,Stage stage,Page user) {

        this.serviceUser = service;
        this.serviceMessage=mess;
        this.serviceF=serviceFriendshipNew;

        this.serviceFr=serviceFriendRequestt;
        this.dialogStage = stage;
        this.userLogin=user;
        this.serviceEvent=servEvent;
        setLabelName();



    }
    @FXML
    public void initialize() {
        // TODO




    }




    @FXML
    public void handleCancel(){
        showWelcomeEditDialog();
    }

    @FXML
    public void handleFriendRequests()
    {
    showFriendReqEditDialog();
    }
    @FXML
    public void handleFriends()
    {
        showFriendsDialog();
    }

    @FXML
    public void handlePeople()
    {
        showPeopleDialog();
    }
    @FXML
    public void handleChats()
    {
        showChatsEditDialog();
    }
    @FXML
    public void handleEvents()
    {
        showEventsEditDialog();
    }
    @FXML
    public void handleNotifications()
    {
        showNotifEditDialog();
    }

    private void showNotifEditDialog() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("notifications.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            NotificationsController controller = loader.getController();
            controller.set(serviceUser,serviceMessage,serviceF,serviceFr,serviceEvent,dialogStage,userLogin);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showEventsEditDialog() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("events.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            EventsController controller = loader.getController();
            controller.set(serviceUser,serviceMessage,serviceF,serviceFr,serviceEvent,dialogStage,userLogin);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showFriendReqEditDialog() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("friendReq.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            FriendshipsReqController controller = loader.getController();
            controller.set(serviceUser,serviceMessage,serviceF,serviceFr,serviceEvent,dialogStage,userLogin);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showChatsEditDialog() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("chats.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            ChatsController controller = loader.getController();
            controller.set(serviceUser,serviceMessage,serviceF,serviceFr,serviceEvent,dialogStage,userLogin);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void showPeopleDialog() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("people.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            PeopleController controller = loader.getController();
            controller.set(serviceUser,serviceMessage,serviceF,serviceFr,serviceEvent,dialogStage,userLogin);


            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showFriendsDialog() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("friends.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            FriendsController controller = loader.getController();
            controller.set(serviceUser,serviceMessage,serviceF,serviceFr,serviceEvent,dialogStage,userLogin);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showWelcomeEditDialog() {
        dialogStage.close();
        //am schimbat aici
        //try {

            // create a new stage for the popup dialog.
           // FXMLLoader loader = new FXMLLoader();
           // loader.setLocation(getClass().getResource("welcomePage.fxml"));

           // AnchorPane root = (AnchorPane) loader.load();


           // Scene scene = new Scene(root);
            //dialogStage.setScene(scene);

           // WelcomeController controller = loader.getController();
            //controller.setService(serviceUser,serviceMessage,serviceF,serviceFr,serviceEvent,dialogStage);

            //dialogStage.show();

        //} catch (IOException e) {
           // e.printStackTrace();
       // }

    }
    protected void setLabelName()
    {
        idName.setText(userLogin.toString3());
    }

}
