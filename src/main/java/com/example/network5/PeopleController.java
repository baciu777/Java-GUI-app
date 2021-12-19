package com.example.network5;

import domain.DtoFriendReq;
import domain.DtoMessage;
import domain.DtoUser;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.ServiceFriendship;
import service.ServiceFriendshipRequest;
import service.ServiceMessage;
import service.ServiceUser;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PeopleController extends MenuController{
    @FXML
    TableView<DtoUser> tableViewUser;
    @FXML
    TableColumn<DtoUser, String> tableColumnUserName;
    @FXML
    TableColumn<DtoUser, String> tableColumnRelation;

    ObservableList<DtoUser> modelUser = FXCollections.observableArrayList();



    public void set(ServiceUser service, ServiceMessage mess,ServiceFriendship serviceFriendshipNew,ServiceFriendshipRequest serviceFriendRequestt,Stage stage,User user) {

        this.serviceUser = service;
        this.serviceMessage = mess;
        this.serviceF = serviceFriendshipNew;

        this.serviceFr = serviceFriendRequestt;
        this.dialogStage = stage;
        this.userLogin = user;

        initModelUser();


    }

    @FXML
    public void initialize() {




        populateUsers();
    }
    @FXML
    public void populateUsers() {
        tableColumnUserName.setCellValueFactory(new PropertyValueFactory<DtoUser, String>("name"));
        tableColumnRelation.setCellValueFactory(new PropertyValueFactory<DtoUser, String>("relation"));

        tableViewUser.setItems(modelUser);
    }


    protected void initModelUser() {
        Iterable<User> users = serviceUser.printUs();
        List<DtoUser> usersList = StreamSupport.stream(users.spliterator(), false)
                .map(x ->
                {
                    String fullName = x.getFirstName() + " " + x.getLastName();
                    DtoUser us = new DtoUser(fullName, " not friends");
                    if (serviceF.areFriends(userLogin.getId(), x.getId()))
                        us.setRelation("friend");
                    String reqStatus = serviceFr.get_request_status(userLogin.getId(), x.getId());
                    if (reqStatus != null)
                        us.setRelation(reqStatus);
                    if (Objects.equals(x.getId(), userLogin.getId()))
                        us.setRelation("you");
                    return us;
                })
                .collect(Collectors.toList());
        modelUser.setAll(usersList);

    }

}
