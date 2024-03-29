package com.example.network5;

import ChangeEvent.MessageTaskChangeEvent;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import observer.Observer;
import service.*;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
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
    Label ChatUserName;
    @FXML
    private ListView<Message> lvChatWindow;


    Observer<MessageTaskChangeEvent> obsMessNew = new Observer<MessageTaskChangeEvent>() {
        @Override
        public void update(MessageTaskChangeEvent messageTaskChangeEvent) {

            if (!Objects.equals(messageTaskChangeEvent.getData().getFrom().getId(), userLogin.getId())) {
                initModelChat();
                initializeChat();
            }


        }
    };
    ObservableList<Chat> modelChat = FXCollections.observableArrayList();


    ObservableList<Message> chatMessages = FXCollections.observableArrayList();//create observablelist for listview

    Chat chatSelected=null;

    public void set(ServiceUser service, ServiceMessage mess, ServiceFriendship serviceFriendshipNew, ServiceFriendshipRequest serviceFriendRequestt, ServiceEvent servEvent, Stage stage, Page user) {



        this.serviceUser = service;
        this.serviceMessage = mess;
        this.serviceF = serviceFriendshipNew;
        //serviceMessage.removeObserver(obsMess);
        serviceMessage.addObserver(obsMessNew);
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

        ChatUserName.setText("");
    }


    public List<Chat> initModelChat() {
        Iterable<Message> mess = userLogin.getMessages();


        List<Chat> chats = new ArrayList<>();
        for (Message ms : mess) {
            List<Long> messageInvolved = new ArrayList<>();

            if ((Objects.equals(ms.getFrom().getId(), userLogin.getId()) ||
                    ms.getTo().contains(serviceUser.findOne(userLogin.getId())))) {
                String nameGroup="Group ";
                List<User> listUsersTo=ms.getTo().stream().sorted(Comparator.comparing(User::getId)).collect(Collectors.toList());

                for(User msI:listUsersTo)
                {
                    nameGroup=nameGroup+msI.getUsername()+" ";
                }
                ms.getTo().forEach(x -> messageInvolved.add(x.getId()));
                messageInvolved.add(ms.getFrom().getId());
                List<Long> messageInvolvedSorted = messageInvolved.stream().sorted().collect(Collectors.toList());


                if (!containsPeople(messageInvolvedSorted, chats) && messageInvolved.size() > 2) {

                    Chat chatNew = new Chat(nameGroup, messageInvolvedSorted);
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
    public String newLinesInString(String s, int x)
    {

        StringBuilder c = new StringBuilder(s);
        for(int i = x; i<s.length(); i = i +x)
        {
            while(i<c.length() && c.charAt(i)!=' ')
            {
                i++;
            }
            if(i<c.length())
                c.setCharAt(i,'\n');
        }
        String output = c.toString();
        return output;
    }
    public VBox getReplyLeft(String SenderName, String message, String replied)
    {
        Label Name= new Label(SenderName);
        Label mess = new Label(message);
        Label rep = new Label(replied);
        mess.setText(newLinesInString(mess.getText(),30));
        rep.setMaxSize(120,30);
        rep.setStyle("-fx-background-color:rgb(166, 166, 166);" +
                "-fx-padding: 3 10 3 10;" +
                "-fx-background-radius: 15;");
        VBox container = new VBox();
        container.getChildren().addAll(Name,rep,mess);
        Name.setStyle("-fx-text-fill: #e9e3d1;" +
                "-fx-padding: 0 0 5 13;");
        mess.setStyle("-fx-background-color:  #e9e3d1;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: #2d2d31;" +
                "-fx-padding: 3 10 10 10;");
        container.setAlignment(Pos.CENTER_LEFT);
        container.setOnMousePressed(Event ->{ mess.setStyle(
                "-fx-background-color:  #e9e3d1;" +
                        "-fx-background-radius: 10;" +
                        "-fx-text-fill: #2d2d31;" +
                        "-fx-padding: 3 10 10 10;"+
                        "-fx-border-color: #2d2d31;" +
                        "-fx-border-radius: 10;"+
                        "-fx-border-width: 2;" );});

        return container;
    }
    public VBox getReplyRight(String SenderName, String message, String replied)
    {
        Label Name= new Label(SenderName);
        Label mess = new Label(message);
        mess.setText(newLinesInString(mess.getText(),30));
        Label rep = new Label(replied);
        rep.setMaxSize(120,30);
        rep.setStyle("-fx-background-color:rgb(166, 166, 166);" +
                "-fx-padding: 3 10 3 10;" +
                "-fx-background-radius: 15;");
        VBox container = new VBox();
        container.getChildren().addAll(Name,rep,mess);
        Name.setStyle("-fx-text-fill: #e9e3d1;" +
                "-fx-padding: 0 0 5 13;");
        mess.setStyle("-fx-background-color:  #2d2d31;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: #e9e3d1;" +
                "-fx-padding: 3 10 10 10;");
        container.setAlignment(Pos.CENTER_RIGHT);
        container.setOnMousePressed(Event ->{ mess.setStyle(
                "-fx-background-color:  #2d2d31;" +
                        "-fx-background-radius: 10;" +
                        "-fx-text-fill: #e9e3d1;" +
                        "-fx-padding: 3 10 10 10;"+
                        "-fx-border-color: #e9e3d1;" +
                        "-fx-border-radius: 10;" );});
        return container;
    }
    public VBox getMessLeft(String SenderName, String message)
    {
        Label Name = new Label(SenderName);
        Label mess = new Label(message);
        mess.setText(newLinesInString(mess.getText(),30));
        VBox container = new VBox(Name,mess);
        Name.setStyle("-fx-text-fill: #e9e3d1;" +
                "-fx-padding: 0 0 5 13;");
        mess.setStyle("-fx-background-color:  #e9e3d1;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: #2d2d31;" +
                "-fx-padding: 3 10 10 10;");
        container.setAlignment(Pos.CENTER_LEFT);
        container.setOnMousePressed(Event ->{ mess.setStyle(
                "-fx-background-color:  #e9e3d1;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: #2d2d31;" +
                "-fx-padding: 3 10 10 10;"+
                "-fx-border-color: #2d2d31;" +

                        "-fx-border-radius: 10;" +
                        "-fx-border-width: 2;" );});
        return container;
    }
    public VBox getMessRight(String SenderName, String message)
    {
        Label Name = new Label(SenderName);
        Label mess = new Label(message);
        mess.setText(newLinesInString(mess.getText(),30));
        VBox container = new VBox(Name,mess);
        Name.setStyle("-fx-text-fill: #e9e3d1;" +
                "-fx-padding: 0 0 5 13;");
        mess.setStyle("-fx-background-color:  #2d2d31;" +
                "-fx-background-radius: 10;" +
                "-fx-text-fill: #e9e3d1;" +
                "-fx-padding: 3 10 10 10");
        container.setAlignment(Pos.CENTER_RIGHT);
        container.setOnMousePressed(Event ->{ mess.setStyle(
                "-fx-background-color:  #2d2d31;" +
                        "-fx-background-radius: 10;" +
                        "-fx-text-fill: #e9e3d1;" +
                        "-fx-padding: 3 10 10 10;"+
                        "-fx-border-color: #e9e3d1;" +
                        "-fx-border-radius: 10;" );});
        return container;
    }
    public void initializeChat() {
        // TODO

        Chat selected = (Chat) tableViewChat.getSelectionModel().getSelectedItem();
        if (selected != null) {
            chatSelected = selected;
            ChatUserName.setText(chatSelected.getName());
        }
        if (chatSelected == null)
            return;
        chatMessages.setAll(
                serviceMessage.groupChat(userLogin.getMessages(), chatSelected.getPeople()));


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

                        //AICI INTRA REPLY URILE
                        if(item.getReply()!=null && item.getReply().getId()!=-1L) {
                            if (!item.getFrom().getId().equals(userLogin.getId())) {

                                VBox content = getReplyLeft(item.getFrom().getFirstName() + " " + item.getFrom().getLastName(), item.getMessage(), item.getReply().getMessage());
                                setGraphic(content);
                            } else {
                                VBox content = getReplyRight(item.getFrom().getFirstName() + " " + item.getFrom().getLastName(),item.getMessage(),item.getReply().getMessage());
                                setGraphic(content);
                            }
                        }
                        else
                            //AICI INTRA MESAJELE NORMALE

                            if (!item.getFrom().getId().equals(userLogin.getId())) {
                                VBox content = getMessLeft(item.getFrom().getFirstName() + " " + item.getFrom().getLastName(),item.getMessage());
                                setGraphic(content);


                            /*
                            hBoxLeft.setStyle("-fx-background-color: #2d2d31;" +
                                    "-fx-padding: 10 20 10 20;" +
                                    "-fx-background-radius: 20");*/
                            } else {
                                VBox content = getMessRight(item.getFrom().getFirstName() + " " + item.getFrom().getLastName(),item.getMessage());
                                setGraphic(content);
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
        if (selected != null)
            chatSelected=selected;

        if(chatSelected==null)
            return;
        try {
            Message selectedItem =  lvChatWindow.getSelectionModel().getSelectedItem();
            if(selectedItem!=null)
                serviceMessage.saveReply(userLogin.getId(), newMessage.getText(),selectedItem.getId());
            else
            serviceMessage.save(userLogin.getId(), takeToWithoutUserLoginIds(chatSelected.getPeople()), newMessage.getText());
            Message newMess = serviceMessage.getLastMessSaved();
            System.out.println(newMess.getId());
            userLogin.addMessage(newMess);

            chatMessages.add(newMess);//get 1st user's text from his/her textfield and add message to observablelist
            //initializeChat();
//////verificaa!!!!


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
            controller.set(serviceUser, serviceMessage, dialogStage, userLogin, this);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleCancel() {
        serviceMessage.removeObserver(obsMessNew);

        showWelcomeEditDialog();
    }

    @FXML
    public void handleFriendRequests() {
        serviceMessage.removeObserver(obsMessNew);

        showFriendReqEditDialog();
    }

    @FXML
    public void handleFriends() {
        serviceMessage.removeObserver(obsMessNew);

        showFriendsDialog();
    }

    @FXML
    public void handlePeople() {
        serviceMessage.removeObserver(obsMessNew);


        showPeopleDialog();
    }

    @FXML
    public void handleChats() {
        serviceMessage.removeObserver(obsMessNew);


        showChatsEditDialog();
    }

    @FXML
    public void handleEvents() {
        serviceMessage.removeObserver(obsMessNew);

        showEventsEditDialog();
    }

    @FXML
    public void handleNotifications() {
        serviceMessage.removeObserver(obsMessNew);

        showNotifEditDialog();
    }


}
