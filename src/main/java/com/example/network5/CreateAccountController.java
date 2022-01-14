package com.example.network5;

import domain.DtoFriendReq;
import domain.FriendRequest;
import domain.Page;
import domain.User;
import domain.validation.ValidationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CreateAccountController extends MenuController{




    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private TextField textFieldUsername;
    @FXML
    private TextField textFieldPassword;
    @FXML
    private TextField textFieldPasswordRepeat;
    @FXML
    private DatePicker textFieldBirth;




    public void setService(ServiceUser service, ServiceMessage mess, ServiceFriendship serviceFriendshipNew, ServiceFriendshipRequest serviceFriendRequestt, ServiceEvent servEvent, Stage stage) {

        this.serviceUser = service;
        this.serviceMessage=mess;
        this.serviceF=serviceFriendshipNew;

        this.serviceFr=serviceFriendRequestt;
        this.dialogStage = stage;
        this.serviceEvent=servEvent;





    }
    @FXML
    public void handleConfirm()
    {
        //String id = textFieldId.getText();
        //String password = textFieldPassword.getText();
        String username=textFieldUsername.getText();
        String password=textFieldPassword.getText();
        String passwordRepeat=textFieldPasswordRepeat.getText();
        String firstName=textFieldFirstName.getText();
        String lastName=textFieldLastName.getText();
        LocalDate birth=textFieldBirth.getValue();
        try {

            serviceUser.verifyUsername(username);
            serviceUser.verifyPassword(password,passwordRepeat);
            serviceUser.save(firstName,lastName,username,password,birth);
            userLogin = serviceUser.findByUsername(username);
            initPage(userLogin);
            GoToMenu(userLogin);
        }
        catch (ValidationException e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
            textFieldPassword.clear();//
            textFieldPasswordRepeat.clear();
        }
        catch (IllegalArgumentException ee)
        {
            MessageAlert.showErrorMessage(null,"id null");
        }
    }
    private void initPage(Page user)
    {

        initModelFriendsUser(user);
        initModelFriendshipReqUser(user);
        initModelFriendshipReqUser2(user);
        initModelChatUser(user);

    }
    private void initModelChatUser(Page user) {
        user.setMessages( serviceMessage.userMessages(user));
    }

    protected void initModelFriendsUser(Page user)
    {
        System.out.println(user.getId());
        user.setFriends(serviceUser.findOne(user.getId()).getFriends());
    }
    protected void initModelFriendshipReqUser(Page user) {
        Predicate<FriendRequest> certainUserRight = x -> x.getId().getRight().equals(user.getId());

        List<DtoFriendReq> friendshipsReqList = StreamSupport.stream(serviceFr.findAllRenew().spliterator(), false)
                .filter(certainUserRight)
                .map(x ->
                {
                    User u1 = serviceUser.findOne(x.getId().getLeft());
                    DtoFriendReq pp=new DtoFriendReq(u1.getFirstName() + " " + u1.getLastName(), user.getFirstName() + " " + user.getLastName(), x.getDate(), x.getStatus());

                    return pp;
                })
                .collect(Collectors.toList());

        user.setFriendRequestsReceived(friendshipsReqList);

    }
    protected void initModelFriendshipReqUser2(Page user) {
        Predicate<FriendRequest> certainUserLeft = x -> x.getId().getLeft().equals(user.getId());

//am facut aici o susta de functie pt findall()
        List<DtoFriendReq> friendshipsReqList = StreamSupport.stream(serviceFr.findAllRenew().spliterator(), false)
                .filter(certainUserLeft)
                .map(x ->
                {

                    User u1 = serviceUser.findOne(x.getId().getRight());
                    return new DtoFriendReq(user.getFirstName() + " " + user.getLastName(), u1.getFirstName() + " " + u1.getLastName(), x.getDate(), x.getStatus());

                })
                .collect(Collectors.toList());

        user.setFriendRequestsSent(friendshipsReqList);

    }

    private void GoToMenu(Page user)
    {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("meniu.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            MenuController menuController = loader.getController();
            menuController.setService(serviceUser,serviceMessage,serviceF,serviceFr,serviceEvent, dialogStage, user);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleCancel(){

     showWelcomeEditDialog();
    }



}
