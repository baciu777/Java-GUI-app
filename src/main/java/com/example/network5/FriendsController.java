package com.example.network5;

import domain.DtoMessage;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

public class FriendsController extends MenuController{
    @FXML
    TextField textFieldSearch;
    @FXML
    Button buttonDel;
    @FXML
    TableView<User> tableViewUsers;
    @FXML
    TableColumn<User, String> tableColumnFirstName;
    @FXML
    TableColumn<User, String> tableColumnLastName;



    ObservableList<User> modelFriendship = FXCollections.observableArrayList();

    public void set(ServiceUser service, ServiceMessage mess,ServiceFriendship serviceFriendshipNew,ServiceFriendshipRequest serviceFriendRequestt,Stage stage,User user) {

        this.serviceUser = service;
        this.serviceMessage = mess;
        this.serviceF = serviceFriendshipNew;

        this.serviceFr = serviceFriendRequestt;
        this.dialogStage = stage;
        this.userLogin = user;

        initModelFriendship();
        textFieldSearch.textProperty().addListener(o -> handleFilter());

    }

    @FXML
    public void initialize() {

        buttonDel.setVisible(false);
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("FirstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("LastName"));

        tableViewUsers.setItems(modelFriendship);


    }

    protected void initModelFriendship() {
        Iterable<User> users = serviceF.friends(userLogin.getId());
        List<User> friendshipsList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        modelFriendship.setAll(friendshipsList);

    }
    public void handleFilter()
    {
        Predicate<User> p1 = n -> n.getFirstName().startsWith(textFieldSearch.getText());
        Iterable<User> users = serviceF.friends(userLogin.getId());
        List<User> friendshipsList = StreamSupport.stream(users.spliterator(), false)
                .filter(p1)
                .collect(Collectors.toList());
        modelFriendship.setAll(friendshipsList);
    }

    public void handleDel()
    {

        //User selected = (User) tableViewUsers.getSelectionModel().getSelectedItem();


        buttonDel.setVisible(true);

    }
    @FXML
    public void handleDeleteFriend() {
        try {
            User selected = (User) tableViewUsers.getSelectionModel().getSelectedItem();

            User found = serviceUser.findOne(selected.getId());
            if (Objects.equals(found.getId(), userLogin.getId()))
                throw new Exception("This is you");
            serviceFr.check_update_deletes(userLogin,found);
            serviceF.deleteFriend(userLogin.getId(), found.getId());




            initModelFriendship();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }
}
