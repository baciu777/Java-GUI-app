package com.example.network5;

import domain.Chat;
import domain.Message;
import domain.Page;
import domain.User;
import domain.validation.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class NewChatController extends ChatsController {


    @FXML
    TableView<User> tableViewUsers;
    @FXML
    TableColumn<User, String> tableColumnFirstName;
    @FXML
    TableColumn<User, String> tableColumnLastName;
    @FXML
    TextField newMess;
    ObservableList<User> modelNewChat = FXCollections.observableArrayList();
    List<Long> newChatUsers = new ArrayList<>();
    ChatsController controllerChat;

    @FXML
    Button buttonAdd;

    public void set(ServiceUser service, ServiceMessage mess, Stage stage, Page user,ChatsController controller) {

        this.serviceUser = service;
        this.serviceMessage = mess;

        this.dialogStage = stage;
        this.userLogin = user;
        //serviceMessage.addObserver(obsMess);
        controllerChat=controller;
        newChatUsers.add(userLogin.getId());
        initModelNewChat();

    }

    @FXML
    public void initialize() {


        buttonAdd.setVisible(false);

        //populate the new group table
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("FirstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("LastName"));

        tableViewUsers.setItems(modelNewChat);


    }

    @FXML
    public void handleDone() {
        newChatUsers = newChatUsers.stream().sorted().collect(Collectors.toList());
        List<Chat> chats=initModelChat();
        boolean found = false;
        for (Chat ch : chats) {
            if (ch.getPeople().equals(newChatUsers)) {
                Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                informationAlert.setHeaderText("Chat already exists");
                informationAlert.showAndWait();

                found = true;
                break;
            }
        }
        if (!found) {

            try {
                serviceMessage.save(userLogin.getId(), takeToWithoutUserLoginIds(newChatUsers), newMess.getText());
                Message newMessageChat = serviceMessage.getLastMessSaved();
                userLogin.addMessage(newMessageChat);
                chatMessages.add(newMessageChat);//get 1st user's text from his/her textfield and add message to observablelist
                //initializeChat();
                newMess.setText("");//clear 1st user's textfield
                controllerChat.initModelChat();
                Alert informationAlert = new Alert(Alert.AlertType.INFORMATION);
                informationAlert.setHeaderText("Chat created");
                informationAlert.showAndWait();
            } catch (ValidationException e) {
                MessageAlert.showErrorMessage(null, e.getMessage());
            } catch (IllegalArgumentException ee) {
                MessageAlert.showErrorMessage(null, "id null");
            }



        }

        dialogStage.close();


    }


    private boolean containsPeople(List<Long> messageInvolvedSorted, List<Chat> chats) {
        for (Chat g : chats)
            if (g.getPeople().equals(messageInvolvedSorted))
                return true;
        return false;
    }

    protected void initModelNewChat() {
        Iterable<User> users = serviceUser.printUs();
        List<User> listUsers = new ArrayList<>();
        for (User ur : users)
            if (!newChatUsers.contains(ur.getId()))
                listUsers.add(ur);

        modelNewChat.setAll(listUsers);

    }


    public void handleAdd() {

        //User selected = (User) tableViewUsers.getSelectionModel().getSelectedItem();


        buttonAdd.setVisible(true);

    }

    @FXML
    public void handleAddUser() {
        try {
            User selected = (User) tableViewUsers.getSelectionModel().getSelectedItem();

            User found = serviceUser.findOne(selected.getId());
            if (Objects.equals(found.getId(), userLogin.getId()))
                throw new Exception("This is you");


            newChatUsers.add(found.getId());
            initModelNewChat();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }
}
