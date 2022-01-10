package com.example.network5;

import domain.Event;
import domain.Notification;
import domain.Page;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import service.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;


import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class NotificationsController extends MenuController {

    public ListView<String> listVIewNotifications;
    ObservableList<String> modelNotifications = FXCollections.observableArrayList();

    public void set(ServiceUser service, ServiceMessage mess, ServiceFriendship serviceFriendshipNew, ServiceFriendshipRequest serviceFriendRequestt, ServiceEvent serviceEvent, Stage stage, Page user) {

        this.serviceUser = service;
        this.serviceMessage = mess;
        this.serviceF = serviceFriendshipNew;

        this.serviceFr = serviceFriendRequestt;
        this.dialogStage = stage;
        this.userLogin = user;
        this.serviceEvent = serviceEvent;
        //serviceMessage.addObserver(obsMess);
        //initModel();
        displayAppointmentNotification();

    }

    @FXML
    public void initialize() {

        Platform.setImplicitExit(false);
        listVIewNotifications.setItems(modelNotifications.sorted(Comparator.reverseOrder()));


    }


    private void displayAppointmentNotification() {
        notifyy();
        List<Event> listEv = new ArrayList<>();
        serviceEvent.printUs().forEach(listEv::add);

        for (Event ev : listEv) {
            long nr = ChronoUnit.DAYS.between(LocalDate.now(), ev.getDate());

            if (nr == 0 && ev.getIds().containsKey(userLogin.getId()) && ev.getIds().get(userLogin.getId()) == 1L
        ) {


                Notifications notificationsBuilder = Notifications.create()
                        .title("New upcoming event")
                        .text(ev.getName() + " it s happening today")
                        .hideAfter(Duration.seconds(5))
                        .position(Pos.BOTTOM_RIGHT);
                notificationsBuilder.darkStyle();
                notificationsBuilder.show();


            }


        }
        initModel();///incercaaam sa fie in ordine
    }


    public void initModel() {
        List<Notification> listNf = new ArrayList<>();
        serviceEvent.printUsNotif().forEach(listNf::add);
        List<Notification> listNfNew = new ArrayList<>();

        modelNotifications.clear();
        listNf.forEach(listNfNew::add);
        listNfNew=  listNfNew.stream().sorted(Comparator.comparing(Notification::getNotif)).collect(Collectors.toList());
     //   Collections.reverse(listNfNew);///fdgsdfgdskdsnskdfng
        for (Notification nf : listNfNew)
        {
            if (Objects.equals(nf.getIduser(), userLogin.getId()))
                modelNotifications.add(nf.getNotif());
        }

    }

    public void notifyy() {

        long delay = ChronoUnit.MILLIS.between(LocalTime.now(), LocalTime.of(0, 0, 1));
        System.out.println(delay);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        //scheduler.schedule(this::startNotifications, delay, TimeUnit.MILLISECONDS);
        scheduler.scheduleAtFixedRate(this::startNotifications, delay, 86400000, TimeUnit.MILLISECONDS);
        initModel();
    }

    public Runnable startNotifications() {

        List<Event> listEv = new ArrayList<>();
        serviceEvent.printUs().forEach(listEv::add);
        for (Event ev : listEv) {
            long nr = ChronoUnit.DAYS.between(LocalDate.now(), ev.getDate());

            if (nr >= 0 && ev.getIds().containsKey(userLogin.getId()) && ev.getIds().get(userLogin.getId()) == 1L
                    && serviceEvent.findOneNotificationIdName(userLogin.getId(), LocalDate.now() + " " + ev.getName() + " days left: " + nr) == null
            ) {


                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //update application thread

                        modelNotifications.add(LocalDate.now() + " " + ev.getName() + " days left: " + nr);

                    }

                });


                serviceEvent.saveNotif(userLogin.getId(), LocalDate.now() + " " + ev.getName() + " days left: " + nr);


            }



        }

        return null;
    }


}
