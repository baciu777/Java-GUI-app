package com.example.network5;

import domain.User;
import domain.validation.ValidationException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.ServiceFriendship;
import service.ServiceFriendshipRequest;
import service.ServiceMessage;
import service.ServiceUser;

import java.io.IOException;
import java.time.LocalDateTime;

public class LoginController {

    @FXML
    private TextField textFieldId;


    @FXML
    private TextField textFieldPassword;

    @FXML
    private Button loginButton;
    private ServiceUser servUser;
    private ServiceMessage serviceMessage;
    private ServiceFriendship serviceFriendship;
    private ServiceFriendshipRequest serviceFriendRequest;
    Stage dialogStage;
    User user;

    public void setService(ServiceUser service, ServiceMessage mess, ServiceFriendship fr, ServiceFriendshipRequest servFriendReq, Stage stage) {
        this.servUser = service;
        this.dialogStage = stage;
        this.serviceFriendship=fr;
        this.serviceMessage=mess;
        this.serviceFriendRequest=servFriendReq;

    }

    private void setFields(User s) {
        //textFieldId.setText(s.getFirstName());
        //textFieldId.setText(s.getLastName());
    }


    public void handleLogin(ActionEvent actionEvent) {
        String id = textFieldId.getText();
        String password = textFieldPassword.getText();

        try {
            long idd = Long.parseLong(id);

            User user=servUser.findOne(idd);
            showMessageTaskEditDialog(user);

        }
        catch (ValidationException e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
        catch (IllegalArgumentException ee)
        {
            MessageAlert.showErrorMessage(null,"id null");
        }

    }
    public void showMessageTaskEditDialog(User user) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("meniu.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Menu");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            MenuController menuController = loader.getController();
            menuController.setService(servUser,serviceMessage,serviceFriendship,serviceFriendRequest, dialogStage, user);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setStage(Stage stage)
    {
        //dialogStage=stage;
    }
}
