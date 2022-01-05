package com.example.network5;

import domain.*;
import domain.validation.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import service.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ChatsController extends MenuController {

    @FXML
    TableView<Chat> tableViewChat;
    @FXML
    TableColumn<Chat, String> tableColumnNameChat;

    @FXML
    Button sendMessage;

    @FXML
    TextField newMessage;

    @FXML
    private ListView<Message> lvChatWindow;


    ObservableList<Chat> modelChat = FXCollections.observableArrayList();


    ObservableList<Message> chatMessages = FXCollections.observableArrayList();//create observablelist for listview


    public void set(ServiceUser service, ServiceMessage mess, ServiceFriendship serviceFriendshipNew, ServiceFriendshipRequest serviceFriendRequestt, ServiceEvent servEvent, Stage stage, Page user) {

        this.serviceUser = service;
        this.serviceMessage = mess;
        this.serviceF = serviceFriendshipNew;

        this.serviceFr = serviceFriendRequestt;
        this.dialogStage = stage;
        this.serviceEvent= servEvent;
        this.userLogin = user;


        initModelChat();

        setLabelName();

    }

    @FXML
    public void initialize() {


        tableColumnNameChat.setCellValueFactory(new PropertyValueFactory<Chat, String>("name"));
        tableViewChat.setItems(modelChat);



    }


    public List<Chat> initModelChat() {
        Iterable<Message> mess = userLogin.getMessages();


         List<Chat> chats = new ArrayList<>();
        for (Message ms : mess) {
            List<Long> messageInvolved = new ArrayList<>();

            if ((Objects.equals(ms.getFrom().getId(), userLogin.getId()) ||
                    ms.getTo().contains(serviceUser.findOne(userLogin.getId())))) {
                ms.getTo().forEach(x -> messageInvolved.add(x.getId()));
                messageInvolved.add(ms.getFrom().getId());
                List<Long> messageInvolvedSorted = messageInvolved.stream().sorted().collect(Collectors.toList());


                if (!containsPeople(messageInvolvedSorted, chats) && messageInvolved.size() > 2) {
                    Chat chatNew = new Chat("Group " + messageInvolvedSorted, messageInvolvedSorted);
                    chats.add(chatNew);
                }
                if (!containsPeople(messageInvolvedSorted, chats) && messageInvolved.size() == 2) {
                    Long id1 = messageInvolved.get(0);
                    Long id2 = messageInvolved.get(1);
                    Chat chatNew = null;
                    if (messageInvolved.get(0) == userLogin.getId()) {
                        User u2 = serviceUser.findOne(id2);
                        chatNew = new Chat(u2.getFirstName() + " " + u2.getLastName(), messageInvolvedSorted);
                    } else {
                        User u1 = serviceUser.findOne(id1);
                        chatNew = new Chat(u1.getFirstName() + " " + u1.getLastName(), messageInvolvedSorted);

                    }
                    chats.add(chatNew);

                }


            }


        }
        System.out.println(userLogin.getMessages().size());
        modelChat.setAll(chats);
        return chats;
    }

    private boolean containsPeople(List<Long> idsInvolved, List<Chat> chats) {
        for (Chat ch : chats) {
            //System.out.println(ch.getPeople());
            if (ch.getPeople().equals(idsInvolved))
                return true;
        }
        return false;
    }

    public void initializeChat() {
        // TODO

        Chat selected = (Chat) tableViewChat.getSelectionModel().getSelectedItem();
        if (selected == null)
            return;

        chatMessages.setAll(
                serviceMessage.groupChat(userLogin.getMessages(), selected.getPeople()));

        lvChatWindow.setItems(chatMessages);//attach the observable list to the listview
        lvChatWindow.setCellFactory(param -> {
            ListCell<Message> cell = new ListCell<Message>() {
                Label lblUserLeft = new Label();
                Label lblTextLeft = new Label();
                HBox hBoxLeft = new HBox(lblUserLeft, lblTextLeft);

                Label lblUserRight = new Label();
                Label lblTextRight = new Label();
                HBox hBoxRight = new HBox(lblTextRight, lblUserRight);

                {
                    hBoxLeft.setAlignment(Pos.CENTER_LEFT);
                    hBoxLeft.setSpacing(5);
                    hBoxRight.setAlignment(Pos.CENTER_RIGHT);
                    hBoxRight.setSpacing(5);
                }

                @Override
                protected void updateItem(Message item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        System.out.println(item.getFrom());
                        if (!item.getFrom().getId().equals(userLogin.getId())) {
                            lblUserLeft.setText(item.getFrom().getFirstName() + " " + item.getFrom().getLastName() + ":");
                            lblTextLeft.setText(item.getMessage());
                            //lblTextLeft.setTextFill(Color.color(1, 0, 0));
                            setGraphic(hBoxLeft);
                        } else {
                            lblUserRight.setText(":" + item.getFrom().getFirstName() + " " + item.getFrom().getLastName());
                            lblTextRight.setText(item.getMessage());
                            setGraphic(hBoxRight);
                        }
                    }
                }

            };

            return cell;
        });
    }


    @FXML
    private void handleUser1SubmitMessage(ActionEvent event) {
        Chat selected = (Chat) tableViewChat.getSelectionModel().getSelectedItem();
        try {
            serviceMessage.save(userLogin.getId(), takeToWithoutUserLoginIds(selected.getPeople()), newMessage.getText());
            Message newMess = serviceMessage.getLastMessSaved();
                    userLogin.addMessage(newMess);

            chatMessages.add(newMess);//get 1st user's text from his/her textfield and add message to observablelist
            initializeChat();
            newMessage.setText("");//clear 1st user's textfield
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        } catch (IllegalArgumentException ee) {
            MessageAlert.showErrorMessage(null, "id null");
        }

    }

    private List<User> takeToWithoutUserLoginUsers(List<Long> ids) {
        List<User> idsNew = new ArrayList<>();
        for (Long id : ids)
            if (id != userLogin.getId())
                idsNew.add(serviceUser.findOne(id));

        return idsNew;
    }

    protected List<Long> takeToWithoutUserLoginIds(List<Long> ids) {
        List<Long> idsNew = new ArrayList<>();
        for (Long id : ids)
            if (id != userLogin.getId())
                idsNew.add(id);

        return idsNew;
    }
    @FXML
    private void showNewChatEditDialog() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("newchat.fxml"));

            AnchorPane root = (AnchorPane) loader.load();
            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit New Chat");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            //dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);


            NewChatController controller = loader.getController();
            controller.set(serviceUser,serviceMessage,dialogStage,userLogin,this);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
