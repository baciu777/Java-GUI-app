package com.example.network5;

import ChangeEvent.Event;

import domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.ServiceFriendship;
import service.ServiceFriendshipRequest;
import service.ServiceMessage;
import service.ServiceUser;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MenuController  {




    protected ServiceFriendship serviceF;
    protected ServiceFriendshipRequest serviceFr;
    protected ServiceUser serviceUser;
    protected ServiceMessage serviceMessage;
    Stage dialogStage;
    User userLogin;

    public void setService(ServiceUser service, ServiceMessage mess,ServiceFriendship serviceFriendshipNew,ServiceFriendshipRequest serviceFriendRequestt,Stage stage,User user) {

        this.serviceUser = service;
        this.serviceMessage=mess;
        this.serviceF=serviceFriendshipNew;

        this.serviceFr=serviceFriendRequestt;
        this.dialogStage = stage;
        this.userLogin=user;





    }
    @FXML
    public void initialize() {
        // TODO




    }




    @FXML
    public void handleCancel(){
        dialogStage.close();
    }

    @FXML
    public void handleFriendRequests()
    {
    showFriendReqEditDialog(userLogin);
    }
    @FXML
    public void handleFriends()
    {
        showFriendsDialog(userLogin);
    }
    @FXML
    public void handleUser()
    {
        showUserEditDialog(userLogin);
    }
    @FXML
    public void handlePeople()
    {
        showPeopleDialog(userLogin);
    }
    @FXML
    public void handleChats()
    {
        showChatsEditDialog(userLogin);
    }

    public void showFriendReqEditDialog(User user) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("friendReq.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            FriendshipsReqController controller = loader.getController();
            controller.set(serviceUser,serviceMessage,serviceF,serviceFr,dialogStage,userLogin);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showChatsEditDialog(User user) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("chats.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            ChatsController controller = loader.getController();
            controller.set(serviceUser,serviceMessage,serviceF,serviceFr,dialogStage,userLogin);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showUserEditDialog(User user) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("user.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            UserController controller = loader.getController();
            controller.set(serviceUser,serviceMessage,serviceF,serviceFr,dialogStage,userLogin);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showPeopleDialog(User user) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("people.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            PeopleController controller = loader.getController();
            controller.set(serviceUser,serviceMessage,serviceF,serviceFr,dialogStage,userLogin);


            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showFriendsDialog(User user) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("friends.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            FriendsController controller = loader.getController();
            controller.set(serviceUser,serviceMessage,serviceF,serviceFr,dialogStage,userLogin);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
