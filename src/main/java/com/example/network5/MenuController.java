package com.example.network5;

import ChangeEvent.FrRequestChangeEvent;
import ChangeEvent.FriendChangeEvent;
import ChangeEvent.MessageTaskChangeEvent;
import domain.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
                userLogin.addMessage(messageTaskChangeEvent.getData());

            }


        }
    };
    protected Observer<FrRequestChangeEvent> obsFrriendReq = new Observer<FrRequestChangeEvent>() {
        @Override
        public void update(FrRequestChangeEvent frRequestChangeEvent) {

           /* FriendRequest frR = frRequestChangeEvent.getData();
            if (!Objects.equals(frRequestChangeEvent.getType(), "unsend")) {
                if (Objects.equals(frR.getId().getRight(), userLogin.getId()) && Objects.equals(frR.getStatus(), "APPROVED")) {
                    User u1 = serviceUser.findOne(frR.getId().getLeft());
                    DtoFriendReq pp = new DtoFriendReq(u1.getFirstName() + " " + u1.getLastName(), userLogin.getFirstName() + " " + userLogin.getLastName(), frR.getDate(), frR.getStatus());
                    System.out.println(userLogin.getFriendRequestsReceived().size());
                    userLogin.removeFrRequestRec(pp);
                    System.out.println(userLogin.getFriendRequestsReceived().size());
                    List<User> friends = userLogin.getFriends();
                    friends.add(u1);

                    userLogin.setFriends(friends);


                }
                if (Objects.equals(frR.getId().getLeft(), userLogin.getId()) && Objects.equals(frR.getStatus(), "APPROVED")) {
                    User u1 = serviceUser.findOne(frR.getId().getRight());
                    DtoFriendReq pp = new DtoFriendReq(userLogin.getFirstName() + " " + userLogin.getLastName(), u1.getFirstName() + " " + u1.getLastName(), frR.getDate(), frR.getStatus());
                    System.out.println("dadadada");
                    userLogin.removeFrRequestSent(pp);
                    List<User> friends = userLogin.getFriends();
                    friends.add(u1);

                    userLogin.setFriends(friends);



                }
                if (Objects.equals(frR.getStatus(), "PENDING") && Objects.equals(frR.getId().getRight(), userLogin.getId())) {
                    User u1 = serviceUser.findOne(frR.getId().getLeft());
                    DtoFriendReq pp = new DtoFriendReq(u1.getFirstName() + " " + u1.getLastName(), userLogin.getFirstName() + " " + userLogin.getLastName(), frR.getDate(), frR.getStatus());

                    userLogin.addRequestRec(pp);

                }
                if (Objects.equals(frR.getStatus(), "REJECTED") && Objects.equals(frR.getId().getRight(), userLogin.getId())) {
                    User u1 = serviceUser.findOne(frR.getId().getLeft());
                    DtoFriendReq pp = new DtoFriendReq(u1.getFirstName() + " " + u1.getLastName(), userLogin.getFirstName() + " " + userLogin.getLastName(), frR.getDate(), frR.getStatus());

                    userLogin.removeFrRequestRec(pp);
                }
                if (Objects.equals(frR.getStatus(), "REJECTED") && Objects.equals(frR.getId().getLeft(), userLogin.getId())) {
                    User u1 = serviceUser.findOne(frR.getId().getRight());
                    DtoFriendReq pp = new DtoFriendReq(userLogin.getFirstName() + " " + userLogin.getLastName(), u1.getFirstName() + " " + u1.getLastName(), frR.getDate(), frR.getStatus());

                    userLogin.removeFrRequestSent(pp);
                }
            }
           // if (Objects.equals(frR.getId().getLeft(), userLogin.getId()) && Objects.equals(frRequestChangeEvent.getType(), "unsend")) {
               // User u1 = serviceUser.findOne(frR.getId().getRight());
              //  DtoFriendReq pp = new DtoFriendReq(userLogin.getFirstName() + " " + userLogin.getLastName(), u1.getFirstName() + " " + u1.getLastName(), frR.getDate(), frR.getStatus());


              //  userLogin.removeFrRequestRec(pp);
            //}
            else
            if (Objects.equals(frR.getId().getRight(), userLogin.getId()) && Objects.equals(frRequestChangeEvent.getType(), "unsend")) {


                User u1 = serviceUser.findOne(frR.getId().getLeft());
                DtoFriendReq pp = new DtoFriendReq(u1.getFirstName() + " " + u1.getLastName(), userLogin.getFirstName() + " " + userLogin.getLastName(), frR.getDate(), frR.getStatus());

                userLogin.removeFrRequestRec(pp);
            }*/
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
    public void handlePDF()
    {
        try{
        //Creating PDF document object
        PDDocument document = new PDDocument();
        PDPage my_page = new PDPage();
        document.addPage(my_page);
        //Saving the document
        document.save("C:/Users/ioana/Documents/GitHub/pdfs/second.pdf");
            PDPageContentStream contentStream = new PDPageContentStream(document, my_page);

            //Begin the Content stream
            contentStream.beginText();

            //Setting the font to the Content stream
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);

            //Setting the position for the line
            contentStream.newLineAtOffset(25, 500);

            String text = "This is the sample document and we are adding content to it.";

            //Adding text in the form of string
            contentStream.showText(text);

            //Ending the content stream
            contentStream.endText();

            System.out.println("Content added");

            //Closing the content stream
            contentStream.close();
        System.out.println("PDF created");

        //Closing the document

            document.close();
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
        //am schimbat aici
        //try {

        // create a new stage for the popup dialog.
        // FXMLLoader loader = new FXMLLoader();
        // loader.setLocation(getClass().getResource("welcomePage.fxml"));

        // AnchorPane root = (AnchorPane) loader.load();


        // Scene scene = new Scene(root);
        //dialogStage.setScene(scene);

        // WelcomeController controller = loader.getController();
        //controller.setService(serviceUser,serviceMessage,serviceF,serviceFr,serviceEvent,dialogStage);

        //dialogStage.show();

        //} catch (IOException e) {
        // e.printStackTrace();
        // }

    }

    protected void setLabelName() {
        idName.setText(userLogin.toString3());
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
