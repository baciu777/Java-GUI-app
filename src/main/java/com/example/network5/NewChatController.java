package com.example.network5;

import domain.Chat;
import domain.Page;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.*;

import java.util.ArrayList;
import java.util.List;

public class NewChatController extends MenuController {


    @FXML
    TableView<User> tableViewUsers;
    @FXML
    TableColumn<User, String> tableColumnFirstName;
    @FXML
    TableColumn<User, String> tableColumnLastName;

    ObservableList<User> modelNewChat = FXCollections.observableArrayList();
    public void set(ServiceUser service, ServiceMessage mess, ServiceFriendship serviceFriendshipNew, ServiceFriendshipRequest serviceFriendRequestt, ServiceEvent serviceEvent, Stage stage, Page user) {

        this.serviceUser = service;
        this.serviceMessage = mess;
        this.serviceF = serviceFriendshipNew;

        this.serviceFr = serviceFriendRequestt;
        this.dialogStage = stage;
        this.userLogin = user;


        initModelNewChat();
        setLabelName();

    }
    @FXML
    public void initialize() {



        //populate the new group table
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("FirstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("LastName"));

        tableViewUsers.setItems(modelNewChat);

    }
    protected void initModelNewChat() {
        List<User> usersList=new ArrayList<>();

        serviceUser.printUs().forEach(usersList::add);
        modelNewChat.setAll(usersList);

    }
    @FXML
    public void handleDone()
    {

    }
}
