package com.example.network5;

import ChangeEvent.EventChangeEvent;
import domain.*;
import domain.validation.ValidationException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import observer.Observer;
import paging.PageR;
import service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EventsController extends MenuController implements Observer<EventChangeEvent>{
    @FXML
    TableView<Event> tableViewEvents;
    @FXML
    TableColumn<Event, String> tableColumnNameEvent;
    @FXML
    TableColumn<Event, String> tableColumnDateEvent;
    @FXML
    TableColumn<Event, String> tableColumnIdEvent;

    private int start = 0;
    private int step = 5;
    @FXML
    TextField newName;
    @FXML
    DatePicker date;
    @FXML
    Button saveEvent;
    @FXML
    Button buttonGoingNotOnOff;
    Event lastEventSelected = null;


    @FXML
    Pagination pagination;

    ObservableList<Event> modelEvents = FXCollections.observableArrayList();



    public void set(ServiceUser service, ServiceMessage mess, ServiceFriendship serviceFriendshipNew, ServiceFriendshipRequest serviceFriendRequestt, ServiceEvent serviceEvent, Stage stage, Page user) {

        this.serviceUser = service;
        this.serviceMessage = mess;
        this.serviceF = serviceFriendshipNew;

        this.serviceFr = serviceFriendRequestt;
        this.dialogStage = stage;
        this.userLogin = user;
        this.serviceEvent = serviceEvent;
        buttonGoingNotOnOff.setVisible(false);
        serviceEvent.addObserver(this);
        //initModelEvents();
        setLabelName();
        pagination.setPageFactory(this::createPage);
        // paging=serviceEvent.crea

    }


    @FXML
    public void initialize() {

        buttonGoingNotOnOff.setVisible(false);
        // tableColumnIdEvent.setCellValueFactory(new PropertyValueFactory<Event, String>("id"));
        //tableColumnIdEvent.setVisible(false);

        tableColumnNameEvent.setCellValueFactory(new PropertyValueFactory<Event, String>("name"));
        tableColumnDateEvent.setCellValueFactory(new PropertyValueFactory<Event, String>("date"));
        tableViewEvents.setItems(modelEvents);


    }

    //eventurile vechi nu se afiseaza
    protected void initModelEvents(int pageIndex) {


        modelEvents.clear();
        Iterable<Event> usersOnPage = serviceEvent.getEventsOnPage(pageIndex);
        List<Event> EvList = StreamSupport.stream(usersOnPage.spliterator(), false).sorted(Comparator.comparing(Event::getDate).reversed()).collect(Collectors.toList());
        System.out.println(usersOnPage);
        for (Event ev : EvList) {
            //if(!ev.getDate().isBefore(LocalDate.now()))
            {
                modelEvents.add(ev);
            }
        }

        //modelEvents.setAll(list);
    }

    private Node createPage(int PageIndex) {
        initModelEvents(PageIndex);
        return new BorderPane(tableViewEvents);
    }

    @FXML
    private void handleSaveEvent(ActionEvent event) {
        String name = newName.getText();
        LocalDate dateNew = date.getValue();
        Map<Long, Long> ids = new HashMap<>();
        ids.put(userLogin.getId(), 1L);
        Event ev = new Event(name, dateNew, ids);
        try {
            serviceEvent.save(name, dateNew, ids);

            modelEvents.add(ev);//get 1st user's text from his/her textfield and add message to observablelist
            pagination.setPageFactory(this::createPage);
            newName.setText("");//clear 1st user's textfield
            date.getEditor().clear();
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
            newName.clear();
            date.getEditor().clear();
        } catch (IllegalArgumentException ee) {
            MessageAlert.showErrorMessage(null, "id null");
        }


    }

    @FXML
    public void handleGoing() {

        Event selected = tableViewEvents.getSelectionModel().getSelectedItem();
        if (selected != null)
            lastEventSelected = selected;

        //System.out.println(selected.getId());
        long nr = ChronoUnit.DAYS.between(LocalDate.now(), lastEventSelected.getDate());
        if (nr >= 0) {
            if (lastEventSelected != null && !lastEventSelected.getIds().containsKey(userLogin.getId())) {
                buttonGoingNotOnOff.setText("Going");
                buttonGoingNotOnOff.setVisible(true);
            } else if (lastEventSelected != null && lastEventSelected.getIds().get(userLogin.getId()).longValue() == 1L) {
                buttonGoingNotOnOff.setText("Notifications off");
                buttonGoingNotOnOff.setVisible(true);
            } else if (lastEventSelected != null) {
                buttonGoingNotOnOff.setText("Notifications on");
                buttonGoingNotOnOff.setVisible(true);
            }
        } else
            buttonGoingNotOnOff.setVisible(false);


    }

    @FXML
    public void Going() {
        try {
            Event selected = tableViewEvents.getSelectionModel().getSelectedItem();
            if (selected != null)
                lastEventSelected = selected;
            long nr = ChronoUnit.DAYS.between(LocalDate.now(), lastEventSelected.getDate());
            if (nr >= 0) {
                if (Objects.equals(buttonGoingNotOnOff.getText(), "Going")) {
                    Map<Long, Long> ids = lastEventSelected.getIds();

                    ids.put(userLogin.getId(), 1L);
                    serviceEvent.update(lastEventSelected.getId(), lastEventSelected.getName(), lastEventSelected.getDate(), ids);
                    buttonGoingNotOnOff.setText("Notifications off");
                } else if (Objects.equals(buttonGoingNotOnOff.getText(), "Notifications off")) {
                    Map<Long, Long> ids = lastEventSelected.getIds();

                    ids.put(userLogin.getId(), -1L);
                    serviceEvent.update(lastEventSelected.getId(), lastEventSelected.getName(), lastEventSelected.getDate(), ids);
                    buttonGoingNotOnOff.setText("Notifications on");
                } else if (Objects.equals(buttonGoingNotOnOff.getText(), "Notifications on")) {
                    Map<Long, Long> ids = lastEventSelected.getIds();

                    ids.put(userLogin.getId(), 1L);
                    serviceEvent.update(lastEventSelected.getId(), lastEventSelected.getName(), lastEventSelected.getDate(), ids);
                    buttonGoingNotOnOff.setText("Notifications off");
                }
            } else
                buttonGoingNotOnOff.setVisible(false);


            //initModelEvents();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }


    @Override
    public void update(EventChangeEvent eventChangeEvent) {
        pagination.setPageFactory(this::createPage);
    }
}
