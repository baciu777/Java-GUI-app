package com.example.network5;

import ObserverController.ObserverFriendReq;
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
import service.ServiceFriendship;
import service.ServiceFriendshipRequest;
import service.ServiceUser;

public class FriendshipsReqController {
    private ServiceFriendshipRequest serviceFriendRequest;
    private ServiceUser servUser;
    private ServiceFriendship servFriend;
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
    ObservableList<DtoFriendReq> modelFrR = FXCollections.observableArrayList();
    ObservableList<DtoUser> modelUser = FXCollections.observableArrayList();
    ObserverFriendReq observerFriendshipReq=new ObserverFriendReq();


    public void setService(ServiceUser servUser,ServiceFriendship servFriend, ServiceFriendshipRequest serviceFriendRequest, Stage dialogStage, User user) {
    this.serviceFriendRequest=serviceFriendRequest;
    this.dialogStage=dialogStage;
    this.user=user;
    this.servUser=servUser;
    this.servFriend = servFriend;
    observerFriendshipReq.setServiceModelFriendshipReq(servUser,servFriend,serviceFriendRequest,modelFrR,modelUser,user);

    //observerUserFriend.setServiceModelUser(servUser,servFriend, serviceFriendRequest,modelUserFriends,user);
    }
    @FXML
    public void initialize() {
        // TODO


        tableColumnReqName.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("name"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("date"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("status"));

        tableViewFriendReq.setItems(modelFrR);
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
        User found = servUser.findbyNameFirst(fullName);
        serviceFriendRequest.addFriend(user.getId(), found.getId());

    }
    @FXML
    public void handleRejectRequest() throws Exception {
        String fullName = SearchingName.getText();
        User found = servUser.findbyNameFirst(fullName);
        serviceFriendRequest.rejectRequest(user.getId(), found.getId());

    }

    @FXML
    public void handleAcceptRequest() throws Exception {
        String fullName = SearchingName.getText();
        User found = servUser.findbyNameFirst(fullName);
        serviceFriendRequest.addFriend(user.getId(), found.getId());

    }

    @FXML
    public void handleDeleteFriend()
    {
        String fullName = SearchingName.getText();
        User found = servUser.findbyNameFirst(fullName);
        servFriend.deleteFriend(user.getId(), found.getId());

    }
}
