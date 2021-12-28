package com.example.network5;

import domain.DtoFriendReq;
import domain.DtoMessage;
import domain.DtoUser;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.TilePane;
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

public class PeopleController extends MenuController{
    @FXML
    TextField textFieldSearch;
    @FXML
    Button buttonSend;
    @FXML
    TableView<DtoUser> tableViewUser;
    @FXML
    TableColumn<DtoUser, String> tableColumnUserName;
    @FXML
    TableColumn<DtoUser, String> tableColumnRelation;
    @FXML
    TilePane peopleTile;
    ObservableList<DtoUser> modelUser = FXCollections.observableArrayList();



    public void set(ServiceUser service, ServiceMessage mess,ServiceFriendship serviceFriendshipNew,ServiceFriendshipRequest serviceFriendRequestt,Stage stage,User user) {
        this.serviceUser = service;
        this.serviceMessage = mess;
        this.serviceF = serviceFriendshipNew;

        this.serviceFr = serviceFriendRequestt;
        this.dialogStage = stage;
        this.userLogin = user;

        initModelUser();
        setLabelName();

    }

    @FXML
    public void initialize() {


        buttonSend.setVisible(false);
        populateUsers();
        textFieldSearch.textProperty().addListener(o -> handleFilter());
    }
    @FXML
    public void populateUsers() {
        tableColumnUserName.setCellValueFactory(new PropertyValueFactory<DtoUser, String>("name"));
        tableColumnRelation.setCellValueFactory(new PropertyValueFactory<DtoUser, String>("relation"));

        tableViewUser.setItems(modelUser);

    }


    public void initModelUser() {
        Iterable<User> users = serviceUser.printUs();
        List<DtoUser> usersList = StreamSupport.stream(users.spliterator(), false)
                .map(x ->
                {
                    String fullName = x.getFirstName() + " " + x.getLastName();
                    DtoUser us = new DtoUser(fullName, "not friends");
                    if (serviceF.areFriends(userLogin, x))
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
        peopleTile.getChildren().clear();
        peopleTile.getChildren().add(new Label("test"));
        for(User u:users)
        {

            OneUserPeopleController oneUser = new OneUserPeopleController();
            oneUser.set(userLogin,u,serviceFr,serviceF,this);
            peopleTile.getChildren().add(oneUser.getBox());

        }
    }
    public void handleFilter()
    {
        Predicate<User> p1 = n -> n.getFirstName().startsWith(textFieldSearch.getText()) || n.getLastName().startsWith(textFieldSearch.getText());
        Iterable<User> users = serviceUser.printUs();
        List<DtoUser> usersList = StreamSupport.stream(users.spliterator(), false)
                .filter(p1)
                .map(x ->
                {
                    String fullName = x.getFirstName() + " " + x.getLastName();
                    DtoUser us = new DtoUser(fullName, " not friends");
                    if (serviceF.areFriends(userLogin, x))
                        us.setRelation("friend");
                    String reqStatus = serviceFr.get_request_status(userLogin.getId(), x.getId());
                    if (reqStatus != null)
                        us.setRelation(reqStatus);
                    if (Objects.equals(x.getId(), userLogin.getId()))
                        us.setRelation("you");
                    return us;
                })
                .collect(Collectors.toList());
        List<User> userForTile = StreamSupport.stream(users.spliterator(), false)
                .filter(p1).collect(Collectors.toList());
        modelUser.setAll(usersList);
        peopleTile.getChildren().clear();
        peopleTile.getChildren().add(new Label("filter"));
        for(User u:userForTile)
        {
            OneUserPeopleController oneUser = new OneUserPeopleController();
            oneUser.set(userLogin,u,serviceFr,serviceF,this);
            peopleTile.getChildren().add(oneUser.getBox());
        }
    }

    public void handleSend()
    {

        DtoUser selected = tableViewUser.getSelectionModel().getSelectedItem();

        if(Objects.equals(selected.getRelation(), "not friends"))
            buttonSend.setVisible(true);
        else
            buttonSend.setVisible(false);

    }
    @FXML
    public void handleSendRequest() {
        try {
            DtoUser selected = tableViewUser.getSelectionModel().getSelectedItem();


            User found = serviceUser.findbyNameFirst(selected.getName());
            if (Objects.equals(found.getId(), userLogin.getId()))
                throw new Exception("This is you");
            serviceFr.addFriend(userLogin.getId(), found.getId());
            initModelUser();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());

        }

    }

}
