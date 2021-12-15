package com.example.network5;

import ObserverController.GenericObserver;

import domain.DtoFriendReq;
import domain.DtoUser;
import domain.User;
import domain.validation.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import observer.Observer;
import service.ServiceFriendship;
import service.ServiceFriendshipRequest;
import service.ServiceMessage;
import service.ServiceUser;

import java.util.Objects;

public class FriendshipsReqController extends GenericObserver {

    Stage dialogStage;
    User user;

    @FXML
    private TextField SearchingName;

    @FXML
    TableView<DtoUser> tableViewUser;
    @FXML
    TableColumn<DtoUser, String> tableColumnUserName;
    @FXML
    TableColumn<DtoUser, String> tableColumnRelation;


    @FXML
    TableView<DtoFriendReq> tableViewFriendReq;
    @FXML
    TableColumn<DtoFriendReq, String> tableColumnFrom;
    @FXML
    TableColumn<DtoFriendReq, String> tableColumnTo;

    @FXML
    TableColumn<DtoFriendReq, String> tableColumnDate;
    @FXML
    TableColumn<DtoFriendReq, String> tableColumnStatus;




    public void setService(ServiceUser servUser, ServiceMessage serviceM, ServiceFriendship servFriend, ServiceFriendshipRequest serviceFriendRequest, Stage dialogStage, User user) {
    super.setService(servUser,serviceM,servFriend,serviceFriendRequest,user);
    this.dialogStage=dialogStage;
    this.user=user;

    initModelFriendship();
    initModelFriendshipReq();
    initModelUser();

    }
    @FXML
    public void initialize() {
        // TODO



        tableColumnFrom.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("from"));
        tableColumnTo.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("to"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("date"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("status"));



        tableViewFriendReq.setItems(modelFriendshipReq);
        populateUsers();
    }
    @FXML
    public void populateUsers()
    {
        tableColumnUserName.setCellValueFactory(new PropertyValueFactory<DtoUser, String>("name"));
        tableColumnRelation.setCellValueFactory(new PropertyValueFactory<DtoUser, String>("relation"));

        tableViewUser.setItems(modelUser);
    }
    @FXML
    public void handleCancel(){
        dialogStage.close();
    }




    @FXML
    public void handleSendRequest() {
        try {
            String fullName = SearchingName.getText();
            User found = serviceUser.findbyNameFirst(fullName);
            if(Objects.equals(found.getId(), user.getId()))
                throw new Exception("This is you");
            serviceFr.addFriend(user.getId(), found.getId());
        }
        catch (Exception e) {
        MessageAlert.showErrorMessage(null,e.getMessage());
    }

    }
    @FXML
    public void handleRejectRequest()  {
       try {
           String fullName = SearchingName.getText();
           User found = serviceUser.findbyNameFirst(fullName);
           if(Objects.equals(found.getId(), user.getId()))
               throw new Exception("This is you");
           serviceFr.rejectRequest(user.getId(), found.getId());
       }catch (Exception e) {
           MessageAlert.showErrorMessage(null,e.getMessage());
       }
    }

    @FXML
    public void handleAcceptRequest()  {
        try {
            String fullName = SearchingName.getText();
            User found = serviceUser.findbyNameFirst(fullName);
            if(Objects.equals(found.getId(), user.getId()))
                throw new Exception("This is you");
            serviceFr.addFriend(user.getId(), found.getId());
        }catch (Exception e) {
                MessageAlert.showErrorMessage(null,e.getMessage());
            }

    }

    @FXML
    public void handleDeleteFriend()
    {
        try{
        String fullName = SearchingName.getText();
        User found = serviceUser.findbyNameFirst(fullName);
        if(Objects.equals(found.getId(), user.getId()))
            throw new Exception("This is you");
        serviceF.deleteFriend(user.getId(), found.getId());
        }
        catch (Exception e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
        }
    }
}
