package com.example.network5;

import domain.*;
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
import service.ServiceMessage;
import service.ServiceUser;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendshipsReqController extends MenuController{




    @FXML
    private TextField SearchingName;




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

    ObservableList<DtoFriendReq> modelFriendshipReq = FXCollections.observableArrayList();

    public void set(ServiceUser service, ServiceMessage mess,ServiceFriendship serviceFriendshipNew,ServiceFriendshipRequest serviceFriendRequestt,Stage stage,User user) {

        this.serviceUser = service;
        this.serviceMessage = mess;
        this.serviceF = serviceFriendshipNew;

        this.serviceFr = serviceFriendRequestt;
        this.dialogStage = stage;
        this.userLogin = user;

//        initModelFriendship();
        initModelFriendshipReq();


    }

    @FXML
    public void initialize() {
        // TODO


        tableColumnFrom.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("from"));
        tableColumnTo.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("to"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("date"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("status"));


        tableViewFriendReq.setItems(modelFriendshipReq);

    }



    @FXML
    public void handleCancel() {
        dialogStage.close();
    }


    @FXML
    public void handleSendRequest() {
        try {
            String fullName = SearchingName.getText();
            User found = serviceUser.findbyNameFirst(fullName);
            if (Objects.equals(found.getId(), userLogin.getId()))
                throw new Exception("This is you");
            serviceFr.addFriend(userLogin.getId(), found.getId());
            initModelFriendshipReq();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());

        }

    }

    @FXML
    public void handleRejectRequest() {
        try {
            String fullName = SearchingName.getText();
            User found = serviceUser.findbyNameFirst(fullName);
            if (Objects.equals(found.getId(), userLogin.getId()))
                throw new Exception("This is you");
            serviceFr.rejectRequest(userLogin.getId(), found.getId());
            //serviceFr.check_update_deletes();
            initModelFriendshipReqUpdate(userLogin,found);
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    @FXML
    public void handleAcceptRequest() {
        try {
            String fullName = SearchingName.getText();
            User found = serviceUser.findbyNameFirst(fullName);
            if (Objects.equals(found.getId(), userLogin.getId()))
                throw new Exception("This is you");
            serviceFr.addFriend(userLogin.getId(), found.getId());
            //serviceFr.check_update_deletes();
            initModelFriendshipReqUpdate(userLogin,found);

        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }

    }

    @FXML
    public void handleDeleteFriend() {
        try {
            String fullName = SearchingName.getText();
            User found = serviceUser.findbyNameFirst(fullName);
            if (Objects.equals(found.getId(), userLogin.getId()))
                throw new Exception("This is you");
            serviceF.deleteFriend(userLogin.getId(), found.getId());
            serviceFr.check_update_deletes();

            // modelUser.setAll(serviceUser.getFriends(user.getId()));

            initModelFriendshipReqUpdate(userLogin,found);
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    protected void initModelFriendshipReq() {
        Predicate<FriendRequest> certainUserLeft = x -> x.getId().getLeft().equals(userLogin.getId());
        Predicate<FriendRequest> certainUserRight = x -> x.getId().getRight().equals(userLogin.getId());
        Predicate<FriendRequest> testCompound = certainUserLeft.or(certainUserRight);

        Iterable<FriendRequest> friendReq = serviceFr.findAll();
        List<DtoFriendReq> friendshipsReqList = StreamSupport.stream(friendReq.spliterator(), false)
                .filter(testCompound)
                .map(x ->
                {
                    User u1 = serviceUser.findOne(x.getId().getLeft());
                    User u2 = serviceUser.findOne(x.getId().getRight());
                    return new DtoFriendReq(u1.getFirstName() + " " + u1.getLastName(), u2.getFirstName() + " " + u2.getLastName(), x.getDate(), x.getStatus());


                })
                .collect(Collectors.toList());

        modelFriendshipReq.setAll(friendshipsReqList);

    }

    protected void initModelFriendshipReqUpdate(User userLogin,User found) {
        Predicate<FriendRequest> certainUserLeft = x -> x.getId().getLeft().equals(userLogin.getId());
        Predicate<FriendRequest> certainUserRight = x -> x.getId().getRight().equals(found.getId());
        Predicate<FriendRequest> testCompound = certainUserLeft.or(certainUserRight);

        Iterable<FriendRequest> friendReq = serviceFr.findAll();
        List<DtoFriendReq> friendshipsReqList = StreamSupport.stream(friendReq.spliterator(), false)
                .filter(testCompound)
                .map(x ->
                {

                    return new DtoFriendReq(userLogin.getFirstName() + " " + userLogin.getLastName(), found.getFirstName() + " " + found.getLastName(), x.getDate(), x.getStatus());


                })
                .collect(Collectors.toList());

        modelFriendshipReq.setAll(friendshipsReqList);

    }



}
