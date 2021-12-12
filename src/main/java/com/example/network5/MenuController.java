package com.example.network5;

import ObserverController.ObserverFriendship;
import ObserverController.ObserverMessage;
import domain.DtoMessage;
import domain.Message;
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
import service.ServiceMessage;
import service.ServiceUser;

public class MenuController  {
    @FXML
    private TextField textFieldId;

    @FXML
    TableView<User> tableViewUsers;
    @FXML
    TableColumn<User, String> tableColumnFirstName;
    @FXML
    TableColumn<User, String> tableColumnLastName;
    @FXML
    TableView<DtoMessage> tableViewMessage;
    @FXML
    TableColumn<DtoMessage,String> tableColumnDesc;
    @FXML
    TableColumn<DtoMessage,String> tableColumnReply;
    @FXML
    TableColumn<DtoMessage,String> tableColumnTo;
    @FXML
    TableColumn<DtoMessage,String> tableColumnData;

    private ServiceUser servUser;
    private ServiceMessage serviceMessage;
    private ServiceFriendship serviceFriendship;
    Stage dialogStage;
    User userLogin;
    ObserverFriendship observerFriendship=new ObserverFriendship();
    ObserverMessage observerMessage=new ObserverMessage();
    ObservableList<User> modelFr = FXCollections.observableArrayList();
    ObservableList<DtoMessage> modelMs = FXCollections.observableArrayList();


    public void setService(ServiceUser service, ServiceMessage mess,ServiceFriendship serviceFriendshipNew,Stage stage,User user) {
        this.servUser = service;
        this.serviceMessage=mess;
        this.serviceFriendship=serviceFriendshipNew;
        this.dialogStage = stage;
        this.userLogin=user;
        if (null != user) {
            setFields(user);
            textFieldId.setEditable(false);
        }
        observerFriendship.setServiceModelFriendship(serviceFriendship,modelFr,userLogin);
        observerMessage.setServiceModelMessage(serviceMessage,modelMs,userLogin);



    }
    @FXML
    public void initialize() {
        // TODO
        tableColumnDesc.setCellValueFactory(new PropertyValueFactory<DtoMessage, String>("message"));
        tableColumnReply.setCellValueFactory(new PropertyValueFactory<DtoMessage, String>("replyIdMess"));

        tableColumnTo.setCellValueFactory(new PropertyValueFactory<DtoMessage, String>("name"));
        tableColumnData.setCellValueFactory(new PropertyValueFactory<DtoMessage, String>("date"));

        tableViewMessage.setItems(modelMs);

        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<User, String>("FirstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<User, String>("LastName"));

        tableViewUsers.setItems(modelFr);

    }
    private void setFields(User s)
    {

        textFieldId.setText(s.getId().toString());

    }
    @FXML
    public void handleCancel(){
        dialogStage.close();
    }







}
