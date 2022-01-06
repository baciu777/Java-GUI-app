package com.example.network5;

import domain.*;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    private ServiceEvent serviceEvent;
    Stage dialogStage;
    Page user;
    //ObservableList<NotaDto> modelGrade = FXCollections.observableArrayList();

    @FXML
    public void handleCancel(){

        showWelcomeEditDialog();
    }


    public void setService(ServiceUser service, ServiceMessage mess, ServiceFriendship fr, ServiceFriendshipRequest servFriendReq,ServiceEvent servEvent, Stage stage) {

        this.servUser = service;
        this.dialogStage = stage;
        this.serviceFriendship=fr;
        this.serviceMessage=mess;
        this.serviceFriendRequest=servFriendReq;
        this.serviceEvent=servEvent;

    }

    private void setFields(User s) {
        //textFieldId.setText(s.getFirstName());
        //textFieldId.setText(s.getLastName());
    }


    public void handleLogin(ActionEvent actionEvent) {
        String username = textFieldId.getText();
        String password = textFieldPassword.getText();

        try {

            user=servUser.findByUsername(username);

            servUser.verifyPasswordUser(password,user);
            System.out.println(user);
            initPage();
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

    private void initPage() {
        initModelFriendsUser();
        initModelFriendshipReqUser();
        initModelFriendshipReqUser2();
        initModelChatUser();

    }

    private void initModelChatUser() {
        user.setMessages( serviceMessage.userMessages(user));
    }

    protected void initModelFriendsUser()
    {
        System.out.println(user.getId());
        user.setFriends(servUser.findOne(user.getId()).getFriends());
    }
    protected void initModelFriendshipReqUser() {
        Predicate<FriendRequest> certainUserRight = x -> x.getId().getRight().equals(user.getId());

        List<DtoFriendReq> friendshipsReqList = StreamSupport.stream(serviceFriendRequest.findAllRenew().spliterator(), false)
                .filter(certainUserRight)
                .map(x ->
                {
                    User u1 = servUser.findOne(x.getId().getLeft());
                    DtoFriendReq pp=new DtoFriendReq(u1.getFirstName() + " " + u1.getLastName(), user.getFirstName() + " " + user.getLastName(), x.getDate(), x.getStatus());

                    return pp;
                })
                .collect(Collectors.toList());

        user.setFriendRequestsReceived(friendshipsReqList);

    }
    protected void initModelFriendshipReqUser2() {
        Predicate<FriendRequest> certainUserLeft = x -> x.getId().getLeft().equals(user.getId());

//am facut aici o susta de functie pt findall()
        List<DtoFriendReq> friendshipsReqList = StreamSupport.stream(serviceFriendRequest.findAllRenew().spliterator(), false)
                .filter(certainUserLeft)
                .map(x ->
                {

                    User u1 = servUser.findOne(x.getId().getRight());
                    return new DtoFriendReq(user.getFirstName() + " " + user.getLastName(), u1.getFirstName() + " " + u1.getLastName(), x.getDate(), x.getStatus());

                })
                .collect(Collectors.toList());

        user.setFriendRequestsSent(friendshipsReqList);

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
            menuController.setService(servUser,serviceMessage,serviceFriendship,serviceFriendRequest,serviceEvent, dialogStage, user);

            dialogStage.show();



        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void showMessageTaskEditDialog(Page user) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("meniu.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            MenuController menuController = loader.getController();
            menuController.setService(servUser,serviceMessage,serviceFriendship,serviceFriendRequest,serviceEvent, dialogStage, user);

            menuController.setObs();
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setStage(Stage stage)
    {
        //dialogStage=stage;
    }
    public void showWelcomeEditDialog() {
        dialogStage.close();

    }
}
