package com.example.network5;

import domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.ServiceFriendship;
import service.ServiceFriendshipRequest;
import service.ServiceMessage;
import service.ServiceUser;

public class UserController extends MenuController{

    @FXML
    private TextField textFieldId;


    public void set(ServiceUser service, ServiceMessage mess,ServiceFriendship serviceFriendshipNew,ServiceFriendshipRequest serviceFriendRequestt,Stage stage,User user) {

        this.serviceUser = service;
        this.serviceMessage = mess;
        this.serviceF = serviceFriendshipNew;

        this.serviceFr = serviceFriendRequestt;
        this.dialogStage = stage;
        this.userLogin = user;


        setFields(userLogin);


    }
    private void setFields(User s)
    {

        textFieldId.setText(s.getId()+" "+s.getFirstName()+" "+s.getLastName());

    }
}
