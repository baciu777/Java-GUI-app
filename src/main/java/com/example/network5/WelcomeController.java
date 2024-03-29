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
import service.*;

import java.io.IOException;
import java.time.LocalDateTime;

public class WelcomeController {

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
    private ServiceEvent serviceEvent;
    Stage dialogStage;
    User user;
    //ObservableList<NotaDto> modelGrade = FXCollections.observableArrayList();

    public void setService(ServiceUser service, ServiceMessage mess, ServiceFriendship fr, ServiceFriendshipRequest servFriendReq,ServiceEvent servEvent, Stage stage) {
        this.servUser = service;
        this.dialogStage = stage;
        this.serviceFriendship=fr;
        this.serviceMessage=mess;
        this.serviceFriendRequest=servFriendReq;
        this.serviceEvent=servEvent;

    }
    @FXML
    public void handleGoToLogin(ActionEvent actionEvent) {
        try {
            // create a new stage for the popup dialog.


            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("loginPage.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Stage dialogStage = new Stage();
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            LoginController lController = loader.getController();
            lController.setService(servUser,serviceMessage,serviceFriendship,serviceFriendRequest,serviceEvent, dialogStage);

            dialogStage.show();



        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    public void handleCreateAccount(ActionEvent actionEvent) {

        try {
            // create a new stage for the popup dialog.


            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("signupPage.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Stage dialogStage = new Stage();
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            CreateAccountController menuController = loader.getController();
            menuController.setService(servUser,serviceMessage,serviceFriendship,serviceFriendRequest,serviceEvent, dialogStage);

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