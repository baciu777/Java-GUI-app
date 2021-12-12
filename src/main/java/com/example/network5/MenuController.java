package com.example.network5;

import ObserverController.ObserverFriendship;
import ObserverController.ObserverMessage;
import domain.DtoMessage;
import domain.Message;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.ServiceFriendship;
import service.ServiceFriendshipRequest;
import service.ServiceMessage;
import service.ServiceUser;

import java.io.IOException;

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
    private ServiceFriendshipRequest serviceFriendRequest;
    Stage dialogStage;
    User userLogin;
    ObserverFriendship observerFriendship=new ObserverFriendship();
    ObserverMessage observerMessage=new ObserverMessage();
    ObservableList<User> modelFr = FXCollections.observableArrayList();
    ObservableList<DtoMessage> modelMs = FXCollections.observableArrayList();


    public void setService(ServiceUser service, ServiceMessage mess,ServiceFriendship serviceFriendshipNew,ServiceFriendshipRequest serviceFriendRequest,Stage stage,User user) {
        this.servUser = service;
        this.serviceMessage=mess;
        this.serviceFriendship=serviceFriendshipNew;
        this.dialogStage = stage;
        this.userLogin=user;
        this.serviceFriendRequest=serviceFriendRequest;
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

    @FXML
    public void handleFriendRequests()
    {
    showFriendReqEditDialog(userLogin);
    }

    public void showFriendReqEditDialog(User user) {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("friendReq.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Menu");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            FriendshipsReqController controller = loader.getController();
            controller.setService(servUser,serviceFriendRequest, dialogStage, user);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }







}
