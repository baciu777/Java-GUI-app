package com.example.network5;

import domain.User;
import domain.validation.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

    @FXML private AnchorPane content;
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
    //ObservableList<NotaDto> modelGrade = FXCollections.observableArrayList();

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
        String username = textFieldId.getText();
        String password = textFieldPassword.getText();

        try {

            User user=servUser.findByUsername(username);
            servUser.verifyPasswordUser(password,user);
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
    public void handleCreateAccount(ActionEvent actionEvent) {

        try {
            // create a new stage for the popup dialog.

             //AnchorPane content2 =FXMLLoader.load(getClass().getResource("createAccount.fxml"));

            //content.getChildren().setAll(content2);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("createAccount.fxml"));

            AnchorPane root = (AnchorPane) loader.load();



            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            CreateAccountController menuController = loader.getController();
            menuController.setService(servUser,serviceMessage,serviceFriendship,serviceFriendRequest, dialogStage, user);

            dialogStage.show();



        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void showMessageTaskEditDialog(User user) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("meniu.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


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
