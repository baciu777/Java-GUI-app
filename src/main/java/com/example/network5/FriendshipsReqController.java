package com.example.network5;

import ChangeEvent.FrRequestChangeEvent;
import domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import observer.Observer;
import service.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FriendshipsReqController extends MenuController  {

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
    TableColumn<DtoFriendReq, String> tableColumnTo2;

    @FXML
    TableColumn<DtoFriendReq, String> tableColumnDate2;
    @FXML
    TableColumn<DtoFriendReq, String> tableColumnStatus2;
    @FXML
    TableColumn<DtoFriendReq, String> tableColumnUnsent;
    ObservableList<DtoFriendReq> modelFriendshipReqRec = FXCollections.observableArrayList();

    ObservableList<DtoFriendReq> modelFriendshipReqSent = FXCollections.observableArrayList();

    Observer<FrRequestChangeEvent> obsFrr=new Observer<FrRequestChangeEvent>() {
        @Override
        public void update(FrRequestChangeEvent frRequestChangeEvent) {



                FriendRequest frR = frRequestChangeEvent.getData();

                    initModelFriendshipReqRec();
                    initModelFriendshipReqSent();

            }
        };

    public void set(ServiceUser service, ServiceMessage mess, ServiceFriendship serviceFriendshipNew, ServiceFriendshipRequest serviceFriendRequestt, ServiceEvent servEvent, Stage stage, Page user) {

        this.serviceUser = service;
        this.serviceMessage = mess;
        this.serviceF = serviceFriendshipNew;

        this.serviceFr = serviceFriendRequestt;
        this.dialogStage = stage;
        this.userLogin = user;
        this.serviceEvent = servEvent;
        serviceFriendRequestt.addObserver(obsFrr);
        //serviceMessage.addObserver(obsMess);

//        initModelFriendship();

        initModelFriendshipReqRec();
        initModelFriendshipReqSent();

        setLabelName();
        setStyleTable();
    }

    @FXML
    public void initialize() {
        // TODO


        tableColumnFrom.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("from"));

        tableColumnDate.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("date"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("status"));
        tableColumnAccept.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("imageAcc"));
        tableColumnDecline.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("imageDec"));

        tableViewFriendReq.setItems(modelFriendshipReqRec);


        tableColumnTo2.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("to"));
        tableColumnDate2.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("date"));
        tableColumnStatus2.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("status"));
        tableColumnUnsent.setCellValueFactory(new PropertyValueFactory<DtoFriendReq, String>("imageUndo"));

        tableViewFriendReq2.setItems(modelFriendshipReqSent);

    }


    /*
    bucatele de style care nu se pot face din scene builder
     */
    public void setStyleTable()
    {
        tableViewFriendReq.setStyle("-fx-background: transparent;" +
                "-fx-background-color: transparent;");

    }

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

            initModelFriendshipReqRec();
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


            if (Objects.equals(found.getId(), userLogin.getId()))
                throw new Exception("This is you");
            serviceFr.addFriend(userLogin.getId(), found.getId());
            User userLoginNew = serviceUser.findOne(userLogin.getId());

            //aici putem scoate de tot approved din baza de date!!!!!!!!!!!
            serviceFr.check_update_deletes(found, userLoginNew);//ar trebui sa se stearga approved requests


            initModelFriendshipReqRec();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }

    }
    @FXML
    public void handleUndoRequest() {
        try {
            DtoFriendReq selected = tableViewFriendReq2.getSelectionModel().getSelectedItem();


            String fullName = selected.getTo();
            User found = serviceUser.findbyNameFirst(fullName);


            if (Objects.equals(found.getId(), userLogin.getId()))
                throw new Exception("This is you");
//init?????

            serviceFr.deleteRequest( found.getId(),userLogin.getId());
            initModelFriendshipReqSent();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }

    }


    protected void initModelFriendshipReqRec() {

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
            // se afiseaza in tabel doar
        modelFriendshipReqRec.setAll(friendshipsReqList);

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
    private ImageView setImgUndo() {
        ImageView image = new ImageView(new Image(this.getClass().getResourceAsStream("/icons8-checkbox-tick-mark-accept-your-checklist-queries-96.png")));

        image.mouseTransparentProperty().addListener((observable, oldVal, newVal) -> {
            if (newVal) {
                image.setMouseTransparent(false);
            }
        });

        image.setFitHeight(20);
        image.setFitWidth(20);
        image.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
            handleUndoRequest();
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


    protected void initModelFriendshipReqSent() {

        List<DtoFriendReq> friendshipsReqList = userLogin.getFriendRequestsSent().stream()

                .map(x ->
                {
                    ImageView imgUndo = setImgUndo();


                    x.setImageUndo(imgUndo);
                    return x;
                })
                .collect(Collectors.toList());

        modelFriendshipReqSent.setAll(friendshipsReqList);

    }





    @FXML
    public void handleCancel() {
        serviceFr.removeObserver(obsFrr);

        showWelcomeEditDialog();
    }

    @FXML
    public void handleFriendRequests() {
        serviceFr.removeObserver(obsFrr);

        showFriendReqEditDialog();
    }

    @FXML
    public void handleFriends() {
        serviceFr.removeObserver(obsFrr);

        showFriendsDialog();
    }

    @FXML
    public void handlePeople() {
        serviceFr.removeObserver(obsFrr);


        showPeopleDialog();
    }

    @FXML
    public void handleChats() {
        serviceFr.removeObserver(obsFrr);


        showChatsEditDialog();
    }

    @FXML
    public void handleEvents() {
        serviceFr.removeObserver(obsFrr);

        showEventsEditDialog();
    }

    @FXML
    public void handleNotifications() {
        serviceFr.removeObserver(obsFrr);

        showNotifEditDialog();
    }


}
