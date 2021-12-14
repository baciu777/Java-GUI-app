package com.example.network5;

import ObserverController.ObserverFriendReq;
import domain.DtoFriendReq;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.ServiceFriendshipRequest;
import service.ServiceUser;

public class FriendshipsReqController {
    private ServiceFriendshipRequest serviceFriendRequest;
    Stage dialogStage;
    User user;
    ServiceUser servUser;
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
    ObservableList<DtoFriendReq> modelFrR = FXCollections.observableArrayList();
    ObserverFriendReq observerFriendshipReq=new ObserverFriendReq();
    public void setService(ServiceUser servUser, ServiceFriendshipRequest serviceFriendRequest, Stage dialogStage, User user) {
    this.serviceFriendRequest=serviceFriendRequest;
    this.dialogStage=dialogStage;
    this.user=user;
    this.servUser=servUser;
        observerFriendshipReq.setServiceModelFriendshipReq(servUser,serviceFriendRequest,modelFrR,user);
    }
    @FXML
    public void initialize() {
        // TODO


        tableColumnFrom.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("from"));
        tableColumnTo.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("to"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("date"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("status"));
        tableViewFriendReq.setItems(modelFrR);

    }
    @FXML
    public void handleCancel(){
        dialogStage.close();
    }






}
