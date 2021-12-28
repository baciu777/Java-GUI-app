package com.example.network5;

import domain.*;
import domain.validation.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class EventsController extends MenuController {
    @FXML
    TableView<Event> tableViewEvents;
    @FXML
    TableColumn<Event, String> tableColumnNameEvent;
    @FXML
    TableColumn<Event, String> tableColumnDateEvent;
    @FXML
    TableColumn<Event, String> tableColumnIdEvent;


    @FXML
    TextField newName;
    @FXML
    DatePicker date;
    @FXML
    Button saveEvent;
    @FXML
    Button buttonGoingNotOnOff;


    ObservableList<Event> modelEvents = FXCollections.observableArrayList();

    public void set(ServiceUser service, ServiceMessage mess, ServiceFriendship serviceFriendshipNew, ServiceFriendshipRequest serviceFriendRequestt, ServiceEvent serviceEvent, Stage stage, Page user) {

        this.serviceUser = service;
        this.serviceMessage = mess;
        this.serviceF = serviceFriendshipNew;

        this.serviceFr = serviceFriendRequestt;
        this.dialogStage = stage;
        this.userLogin = user;
        this.serviceEvent=serviceEvent;
        buttonGoingNotOnOff.setVisible(false);
        initModelEvents();


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

    protected void initModelEvents() {
        List<Event> list = new ArrayList<>();
        serviceEvent.printUs().forEach(list::add);

        modelEvents.setAll(list);
    }
    @FXML
    private void handleSaveEvent(ActionEvent event) {
       String name=newName.getText();
        LocalDate dateNew=date.getValue();
        Map<Long,Long> ids=new HashMap<>();
        ids.put(userLogin.getId(),1L);
        Event ev=new Event(name,dateNew,ids);
        try {
            serviceEvent.save(name, dateNew, ids);

            modelEvents.add(ev);//get 1st user's text from his/her textfield and add message to observablelist
            initModelEvents();
            newName.setText("");//clear 1st user's textfield
            date.getEditor().clear();
        }catch (ValidationException e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
            newName.clear();
            date.getEditor().clear();
        }
        catch (IllegalArgumentException ee)
        {
            MessageAlert.showErrorMessage(null,"id null");
        }



    }
    @FXML
    public void handleGoing()
    {

        Event selected = tableViewEvents.getSelectionModel().getSelectedItem();
        //System.out.println(selected.getId());

        if(  selected!=null && !selected.getIds().containsKey(userLogin.getId()) )
        {buttonGoingNotOnOff.setText("Going");
            buttonGoingNotOnOff.setVisible(true);}
        else if(selected!=null && selected.getIds().get(userLogin.getId()).longValue()==1L)
        {buttonGoingNotOnOff.setText("Notifications off");
            buttonGoingNotOnOff.setVisible(true);
        }
        else if(selected!=null)
        {buttonGoingNotOnOff.setText("Notifications on");
            buttonGoingNotOnOff.setVisible(true);
        }

    }
    @FXML
    public void Going()
    {
        try {
            Event selected =  tableViewEvents.getSelectionModel().getSelectedItem();

            if(Objects.equals(buttonGoingNotOnOff.getText(), "Going")) {
                Map<Long,Long> ids = selected.getIds();

                ids.put(userLogin.getId(), 1L);
                serviceEvent.update(selected.getId(), selected.getName(), selected.getDate(), ids);
                buttonGoingNotOnOff.setText("Notifications off");
            }
            else
            if(Objects.equals(buttonGoingNotOnOff.getText(), "Notifications off")) {
                Map<Long,Long> ids = selected.getIds();

                ids.put(userLogin.getId(), -1L);
                serviceEvent.update(selected.getId(), selected.getName(), selected.getDate(), ids);
                buttonGoingNotOnOff.setText("Notifications on");
            }
            else
            if(Objects.equals(buttonGoingNotOnOff.getText(), "Notifications on")) {
                Map<Long,Long> ids = selected.getIds();

                ids.put(userLogin.getId(), 1L);
                serviceEvent.update(selected.getId(), selected.getName(), selected.getDate(), ids);
                buttonGoingNotOnOff.setText("Notifications off");
            }


            initModelEvents();
        } catch (Exception e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }
}
