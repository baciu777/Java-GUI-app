package com.example.network5;

import domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FriendshipsReqController extends MenuController {


    @FXML
    private TextField SearchingName;
    @FXML
    Button buttonAcc;
    @FXML
    Button buttonDec;

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
    @FXML
    TableColumn<DtoFriendReq, String> tableColumnAccept;
    @FXML
    TableColumn<DtoFriendReq, String> tableColumnDecline;
    @FXML
    TableView<DtoFriendReq> tableViewFriendReq2;
    @FXML
    TableColumn<DtoFriendReq, String> tableColumnFrom2;
    @FXML
    TableColumn<DtoFriendReq, String> tableColumnTo2;

    @FXML
    TableColumn<DtoFriendReq, String> tableColumnDate2;
    @FXML
    TableColumn<DtoFriendReq, String> tableColumnStatus2;

    ObservableList<DtoFriendReq> modelFriendshipReq = FXCollections.observableArrayList();

    ObservableList<DtoFriendReq> modelFriendshipReq2 = FXCollections.observableArrayList();

    public void set(ServiceUser service, ServiceMessage mess, ServiceFriendship serviceFriendshipNew, ServiceFriendshipRequest serviceFriendRequestt, ServiceEvent servEvent, Stage stage, Page user) {

        this.serviceUser = service;
        this.serviceMessage = mess;
        this.serviceF = serviceFriendshipNew;

        this.serviceFr = serviceFriendRequestt;
        this.dialogStage = stage;
        this.userLogin = user;
        this.serviceEvent = servEvent;

//        initModelFriendship();
        initModelFriendshipReq();
        initModelFriendshipReq2();
        setLabelName();

    }

    @FXML
    public void initialize() {
        // TODO


        tableColumnFrom.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("from"));
        tableColumnTo.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("to"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("date"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("status"));
        tableColumnAccept.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("imageAcc"));
        tableColumnDecline.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("imageDec"));

        tableViewFriendReq.setItems(modelFriendshipReq);

        tableColumnFrom2.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("from"));
        tableColumnTo2.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("to"));
        tableColumnDate2.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("date"));
        tableColumnStatus2.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("status"));

        tableViewFriendReq2.setItems(modelFriendshipReq2);

    }

    /*
    @FXML
    public void handleCancel() {
        dialogStage.close();
    }
    */

    @FXML
    public void handleRejectRequest() {
        try {
            DtoFriendReq selected = tableViewFriendReq.getSelectionModel().getSelectedItem();


            String fullName = selected.getFrom();
            User found = serviceUser.findbyNameFirst(fullName);

            if (Objects.equals(found.getId(), userLogin.getId()))
                throw new Exception("This is you");
            serviceFr.rejectRequest(userLogin.getId(), found.getId());

            //neaparat astaaaaa
            userLogin.removeFrRequestRec(selected);

            initModelFriendshipReq();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    @FXML
    public void handleAcceptRequest() {
        try {
            DtoFriendReq selected = tableViewFriendReq.getSelectionModel().getSelectedItem();


            String fullName = selected.getFrom();
            User found = serviceUser.findbyNameFirst(fullName);
            User found2 = serviceUser.findOne(found.getId());

            if (Objects.equals(found.getId(), userLogin.getId()))
                throw new Exception("This is you");
            serviceFr.addFriend(userLogin.getId(), found.getId());
            User userLoginNew = serviceUser.findOne(userLogin.getId());

            //aici putem scoate de tot approved din baza de date!!!!!!!!!!!
            serviceFr.check_update_deletes(found, userLoginNew);//ar trebui sa se stearga approved requests
            //initModelFriendshipReqUpdate(userLogin, found);
//ii okkkk
            userLogin.removeFrRequestRec(selected);
            List<User> friends = userLogin.getFriends();
            friends.add(found);
            userLogin.setFriends(friends);
            initModelFriendshipReq();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }

    }


    protected void initModelFriendshipReq() {

        List<DtoFriendReq> friendshipsReqList = userLogin.getFriendRequestsReceived().stream()

                .map(x ->
                {
                    ImageView imgAcc = setImgAcc();
                    ImageView imgDec = setImgDec();
                    x.setImageAcc(imgAcc);
                    x.setImageDec(imgDec);
                    return x;
                })
                .collect(Collectors.toList());

        modelFriendshipReq.setAll(friendshipsReqList);

    }

    private ImageView setImgAcc() {
        ImageView image = new ImageView(new Image(this.getClass().getResourceAsStream("/icons8-checkbox-tick-mark-accept-your-checklist-queries-96.png")));

        image.mouseTransparentProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal) {
                image.setMouseTransparent(false);
            }
        });

        image.setFitHeight(20);
        image.setFitWidth(20);
        image.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            handleAcceptRequest();
            event.consume();
        });
        return image;
    }

    private ImageView setImgDec() {
        ImageView image = new ImageView(new Image(this.getClass().getResourceAsStream("/icons8-cross-sign-in-box-for-decline,-isolated-in-a-white-background.-96.png")));

        image.mouseTransparentProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal) {
                image.setMouseTransparent(false);
            }
        });

        image.setFitHeight(20);
        image.setFitWidth(20);
        image.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            handleRejectRequest();
            event.consume();
        });



        return image;
    }


    protected void initModelFriendshipReq2() {
        List<DtoFriendReq> friendshipsReqList = userLogin.getFriendRequestsSent().stream()

                .map(x ->
                {
                    ImageView imgAcc = setImgAcc();
                    ImageView imgDec = setImgDec();
                    x.setImageAcc(imgAcc);
                    x.setImageDec(imgDec);
                    return x;
                })
                .collect(Collectors.toList());

        modelFriendshipReq2.setAll(friendshipsReqList);

    }


}
