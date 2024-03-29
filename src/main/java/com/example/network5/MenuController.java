package com.example.network5;

import ChangeEvent.EventChangeEvent;
import ChangeEvent.FrRequestChangeEvent;
import ChangeEvent.FriendChangeEvent;
import ChangeEvent.MessageTaskChangeEvent;
import domain.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import observer.Observable;
import observer.Observer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import service.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MenuController {


    protected ServiceFriendship serviceF;
    protected ServiceFriendshipRequest serviceFr;
    protected ServiceUser serviceUser;
    protected ServiceMessage serviceMessage;
    protected ServiceEvent serviceEvent;

    @FXML
    protected Label idName;


    Stage dialogStage;
    Page userLogin;

    protected Observer<MessageTaskChangeEvent> obsMess = new Observer<MessageTaskChangeEvent>() {
        @Override
        public void update(MessageTaskChangeEvent messageTaskChangeEvent) {

            System.out.println("update from menu controller chat");
            if (!Objects.equals(messageTaskChangeEvent.getData().getFrom().getId(), userLogin.getId())) {
                userLogin.addMessage(serviceMessage.getLastMessSaved());
                System.out.println(serviceMessage.getLastMessSaved());

            }


        }
    };

    protected Observer<FrRequestChangeEvent> obsFrriendReq = new Observer<FrRequestChangeEvent>() {
        @Override
        public void update(FrRequestChangeEvent frRequestChangeEvent) {


            List<User> fr = new ArrayList<>();
            serviceF.friends(userLogin.getId()).forEach(fr::add);
            userLogin.setFriends(fr);
            Predicate<FriendRequest> certainUserRight = x -> x.getId().getRight().equals(userLogin.getId());

            List<DtoFriendReq> friendshipsReqList = StreamSupport.stream(serviceFr.findAllRenew().spliterator(), false)
                    .filter(certainUserRight)
                    .map(x ->
                    {
                        User u1 = serviceUser.findOne(x.getId().getLeft());
                        DtoFriendReq pp = new DtoFriendReq(u1.getFirstName() + " " + u1.getLastName(), userLogin.getFirstName() + " " + userLogin.getLastName(), x.getDate(), x.getStatus());

                        return pp;
                    })
                    .collect(Collectors.toList());

            userLogin.setFriendRequestsReceived(friendshipsReqList);
            Predicate<FriendRequest> certainUserLeft = x -> x.getId().getLeft().equals(userLogin.getId());

            //am facut aici o susta de functie pt findall()
            List<DtoFriendReq> friendshipsReqList2 = StreamSupport.stream(serviceFr.findAllRenew().spliterator(), false)
                    .filter(certainUserLeft)
                    .map(x ->
                    {

                        User u1 = serviceUser.findOne(x.getId().getRight());
                        return new DtoFriendReq(userLogin.getFirstName() + " " + userLogin.getLastName(), u1.getFirstName() + " " + u1.getLastName(), x.getDate(), x.getStatus());

                    })
                    .collect(Collectors.toList());

            userLogin.setFriendRequestsSent(friendshipsReqList2);

        }


    };

    Observer<FriendChangeEvent> frObsFriends = new Observer<FriendChangeEvent>() {
        @Override
        public void update(FriendChangeEvent friendChangeEvent) {
            System.out.println(userLogin.getFriendRequestsReceived().size());
            List<User> fr = new ArrayList<>();
            serviceF.friends(userLogin.getId()).forEach(fr::add);
            userLogin.setFriends(fr);
            //initModelUser();
        }
    };

    public void setService(ServiceUser service, ServiceMessage mess, ServiceFriendship serviceFriendshipNew, ServiceFriendshipRequest serviceFriendRequestt, ServiceEvent servEvent, Stage stage, Page user) {

        this.serviceUser = service;
        this.serviceMessage = mess;
        this.serviceF = serviceFriendshipNew;

        this.serviceFr = serviceFriendRequestt;
        this.dialogStage = stage;
        this.userLogin = user;
        this.serviceEvent = servEvent;
        //this.serviceMessage.addObserver(obsMess);


        setLabelName();


    }

    @FXML
    public void initialize() {
        // TODO


    }


    public void showNotifEditDialog() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("notifications.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            NotificationsController controller = loader.getController();
            controller.set(serviceUser, serviceMessage, serviceF, serviceFr, serviceEvent, dialogStage, userLogin);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showEventsEditDialog() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("events.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            EventsController controller = loader.getController();
            controller.set(serviceUser, serviceMessage, serviceF, serviceFr, serviceEvent, dialogStage, userLogin);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void showPdfExportDialog()
    {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Pdf.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            PdfController control = loader.getController();
            control.set(userLogin);
            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showFriendReqEditDialog() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("friendReq.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            FriendshipsReqController controller = loader.getController();
            controller.set(serviceUser, serviceMessage, serviceF, serviceFr, serviceEvent, dialogStage, userLogin);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showChatsEditDialog() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("chats.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            ChatsController controller = loader.getController();
            controller.set(serviceUser, serviceMessage, serviceF, serviceFr, serviceEvent, dialogStage, userLogin);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showPeopleDialog() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("people.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            PeopleController controller = loader.getController();
            controller.set(serviceUser, serviceMessage, serviceF, serviceFr, serviceEvent, dialogStage, userLogin);


            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showFriendsDialog() {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("friends.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            FriendsController controller = loader.getController();
            controller.set(serviceUser, serviceMessage, serviceF, serviceFr, serviceEvent, dialogStage, userLogin);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showWelcomeEditDialog() {
        dialogStage.close();

    }

    protected void setLabelName() {
        idName.setText(userLogin.toString4());

    }


    @FXML
    public void handleCancel() {
        showWelcomeEditDialog();
    }

    @FXML
    public void handleFriendRequests() {
        showFriendReqEditDialog();
    }

    @FXML
    public void handleFriends() {
        showFriendsDialog();
    }

    @FXML
    public void handlePeople() {

        showPeopleDialog();
    }

    @FXML
    public void handleChats() {


        showChatsEditDialog();
    }

    @FXML
    public void handleEvents() {
        showEventsEditDialog();
    }

    @FXML
    public void handleNotifications() {
        showNotifEditDialog();
    }

    public void setObs() {
        serviceFr.addObserver(obsFrriendReq);
        serviceMessage.addObserver(obsMess);
        serviceF.addObserver(frObsFriends);
    }
}
