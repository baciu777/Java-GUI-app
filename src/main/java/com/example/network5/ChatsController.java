package com.example.network5;

import domain.DtoMessage;
import domain.Message;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ChatsController extends MenuController{

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




    ObservableList<DtoMessage> modelMessage = FXCollections.observableArrayList();


    public void set(ServiceUser service, ServiceMessage mess,ServiceFriendship serviceFriendshipNew,ServiceFriendshipRequest serviceFriendRequestt,Stage stage,User user) {

        this.serviceUser = service;
        this.serviceMessage = mess;
        this.serviceF = serviceFriendshipNew;

        this.serviceFr = serviceFriendRequestt;
        this.dialogStage = stage;
        this.userLogin = user;

        initModelMessage();


    }

    @FXML
    public void initialize()
    {
        tableColumnDesc.setCellValueFactory(new PropertyValueFactory<DtoMessage, String>("message"));
        tableColumnReply.setCellValueFactory(new PropertyValueFactory<DtoMessage, String>("replyIdMess"));

        tableColumnTo.setCellValueFactory(new PropertyValueFactory<DtoMessage, String>("name"));
        tableColumnData.setCellValueFactory(new PropertyValueFactory<DtoMessage, String>("date"));

        tableViewMessage.setItems(modelMessage);
    }

    protected void initModelMessage() {
        Iterable<Message> messages = serviceMessage.userMessages(userLogin);
        List<DtoMessage> messageTaskList = StreamSupport.stream(messages.spliterator(), false)

                .map(x ->
                        {
                            User u1=x.getFrom();
                            if (x.getReply() == null)
                                return new DtoMessage(u1.getFirstName() +" "+ u1.getLastName(), x.getMessage(), x.getDate(), null);

                            else
                                return new DtoMessage(u1.getFirstName() +" "+ u1.getLastName(), x.getMessage(), x.getDate(), x.getReply().getId());
                        }

                )
                .collect(Collectors.toList());
        modelMessage.setAll(messageTaskList);

    }

}
