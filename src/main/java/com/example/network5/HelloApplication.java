package com.example.network5;

import domain.*;
import domain.validation.EventValidator;
import domain.validation.FriendshipValidator;
import domain.validation.MessageValidator;
import domain.validation.UserValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import paging.PagingRepository;
import repository.Repository;
import repository.database.*;
import service.*;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {


        Repository<Long, User> repoDb = new UserDbRepository("jdbc:postgresql://localhost:5432/network", "postgres", "ioana", new UserValidator());
        repoDb.findAll().forEach(System.out::println);
        Repository<Tuple<Long, Long>, Friendship> repoDbFr = new FriendshipDbRepository("jdbc:postgresql://localhost:5432/network", "postgres", "ioana", new FriendshipValidator());
        repoDbFr.findAll().forEach(System.out::println);
        Repository<Long, Message> repoDbMs = new MessageDbRepository("jdbc:postgresql://localhost:5432/network", "postgres", "ioana", new MessageValidator());
        Repository<Tuple<Long, Long>, FriendRequest> repoDbFrRq = new FriendRequestDbRepository("jdbc:postgresql://localhost:5432/network", "postgres", "ioana");
        PagingRepository<Long, Event> repoDbEv = new EventDbRepository("jdbc:postgresql://localhost:5432/network", "postgres", "ioana", new EventValidator());
        Repository<Long, Notification> repoDbNf = new NotificationsDbRepository("jdbc:postgresql://localhost:5432/network", "postgres", "ioana", new EventValidator());

        ServiceUser servUser = new ServiceUser(repoDb, repoDbFr);
        ServiceFriendship servFriendship = new ServiceFriendship(repoDb, repoDbFr);
        ServiceMessage servMessage = new ServiceMessage(repoDbMs, repoDb);
        ServiceFriendshipRequest servFriendReq = new ServiceFriendshipRequest(repoDbFrRq, servFriendship, servUser);
        ServiceEvent serviceEvent = new ServiceEvent(repoDbEv,repoDbNf);


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("welcomePage.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 709, 549);
        WelcomeController welcomeController = fxmlLoader.getController();
        welcomeController.setService(servUser, servMessage, servFriendship,servFriendReq,serviceEvent, stage);
        stage.setTitle("Social Life");
        welcomeController.setStage(stage);
        stage.setScene(scene);
        stage.show();


    }

    private void initView(Stage stage) throws IOException {


    }

    public static void main(String[] args) {
        launch();
    }
}