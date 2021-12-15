package com.example.network5;

import ObserverController.GenericObserver;

import domain.DtoFriendReq;
import domain.DtoUser;
import domain.User;
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
    TableColumn<DtoFriendReq, String> tableColumnReqName;
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
    //observerUserFriend.setServiceModelUser(servUser,servFriend, serviceFriendRequest,modelUserFriends,user);
    }
    @FXML
    public void initialize() {
        // TODO


        tableColumnReqName.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("name"));
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
    public void handleSendRequest() throws Exception {
        String fullName = SearchingName.getText();
        User found = serviceUser.findbyNameFirst(fullName);
        serviceFr.addFriend(user.getId(), found.getId());

    }
    @FXML
    public void handleRejectRequest() throws Exception {
        String fullName = SearchingName.getText();
        User found = serviceUser.findbyNameFirst(fullName);
        serviceFr.rejectRequest(user.getId(), found.getId());

    }

    @FXML
    public void handleAcceptRequest() throws Exception {
        String fullName = SearchingName.getText();
        User found = serviceUser.findbyNameFirst(fullName);
        serviceFr.addFriend(user.getId(), found.getId());

    }

    @FXML
    public void handleDeleteFriend()
    {
        String fullName = SearchingName.getText();
        User found = serviceUser.findbyNameFirst(fullName);
        serviceF.deleteFriend(user.getId(), found.getId());

    }
}
