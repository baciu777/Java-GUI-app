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
import service.ServiceFriendship;
import service.ServiceFriendshipRequest;
import service.ServiceMessage;
import service.ServiceUser;

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

    public void set(ServiceUser service, ServiceMessage mess, ServiceFriendship serviceFriendshipNew, ServiceFriendshipRequest serviceFriendRequestt, Stage stage, User user) {

        this.serviceUser = service;
        this.serviceMessage = mess;
        this.serviceF = serviceFriendshipNew;

        this.serviceFr = serviceFriendRequestt;
        this.dialogStage = stage;
        this.userLogin = user;

//        initModelFriendship();
        initModelFriendshipReq();
        initModelFriendshipReq2();


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


    @FXML
    public void handleCancel() {
        dialogStage.close();
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
            //serviceFr.check_update_deletes();
            //initModelFriendshipReqUpdate(userLogin, found);
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
            serviceFr.check_update_deletes(found,userLoginNew);//ar trebui sa se stearga approved requests
            //initModelFriendshipReqUpdate(userLogin, found);
            initModelFriendshipReq();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }

    }


    protected void initModelFriendshipReq() {
        Predicate<FriendRequest> certainUserRight = x -> x.getId().getRight().equals(userLogin.getId());

//am facut aici o susta de functie pt findall()
        List<DtoFriendReq> friendshipsReqList = StreamSupport.stream(serviceFr.findAllRenew().spliterator(), false)
                .filter(certainUserRight)
                .map(x ->
                {
                    ImageView imgAcc=setImgAcc();
                    ImageView imgDec=setImgDec();
                    User u1 = serviceUser.findOne(x.getId().getLeft());
                    DtoFriendReq pp=new DtoFriendReq(u1.getFirstName() + " " + u1.getLastName(), userLogin.getFirstName() + " " + userLogin.getLastName(), x.getDate(), x.getStatus());
                    pp.setImageAcc(imgAcc);
                    pp.setImageDec(imgDec);
                    return pp;
                })
                .collect(Collectors.toList());

        modelFriendshipReq.setAll(friendshipsReqList);

    }

    private ImageView setImgAcc() {
        ImageView image=new ImageView(new Image(this.getClass().getResourceAsStream("/icons8-checkbox-tick-mark-accept-your-checklist-queries-96.png")));

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
        ImageView image=new ImageView(new Image(this.getClass().getResourceAsStream("/icons8-cross-sign-in-box-for-decline,-isolated-in-a-white-background.-96.png")));

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

    protected void initModelFriendshipReqUpdate(User userLogin, User found) {
        Tuple<Long, Long> longLongTuple = new Tuple<>();
        longLongTuple.setLeft(userLogin.getId());
        longLongTuple.setRight(found.getId());
        FriendRequest fr = serviceFr.findOne(longLongTuple);
        DtoFriendReq newDto = new DtoFriendReq(userLogin.getFirstName() + " " + userLogin.getLastName(), found.getFirstName() + " " + found.getLastName(), fr.getDate(), fr.getStatus());


        modelFriendshipReq.add(newDto);

    }


    protected void initModelFriendshipReq2() {
        Predicate<FriendRequest> certainUserLeft = x -> x.getId().getLeft().equals(userLogin.getId());

//am facut aici o susta de functie pt findall()
        List<DtoFriendReq> friendshipsReqList = StreamSupport.stream(serviceFr.findAllRenew().spliterator(), false)
                .filter(certainUserLeft)
                .map(x ->
                {

                    User u1 = serviceUser.findOne(x.getId().getRight());
                    return new DtoFriendReq(userLogin.getFirstName() + " " + userLogin.getLastName(), u1.getFirstName() + " " + u1.getLastName(), x.getDate(), x.getStatus());

                })
                .collect(Collectors.toList());

        modelFriendshipReq2.setAll(friendshipsReqList);

    }


}
