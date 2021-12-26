package com.example.network5;

import domain.User;
import domain.validation.ValidationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.ServiceFriendship;
import service.ServiceFriendshipRequest;
import service.ServiceMessage;
import service.ServiceUser;

import java.io.IOException;
import java.time.LocalDate;

public class CreateAccountController extends MenuController{




    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private TextField textFieldUsername;
    @FXML
    private TextField textFieldPassword;
    @FXML
    private TextField textFieldPasswordRepeat;
    @FXML
    private DatePicker textFieldBirth;




    public void setService(ServiceUser service, ServiceMessage mess,ServiceFriendship serviceFriendshipNew,ServiceFriendshipRequest serviceFriendRequestt,Stage stage) {

        this.serviceUser = service;
        this.serviceMessage=mess;
        this.serviceF=serviceFriendshipNew;

        this.serviceFr=serviceFriendRequestt;
        this.dialogStage = stage;






    }
    @FXML
    public void handleConfirm()
    {
        //String id = textFieldId.getText();
        //String password = textFieldPassword.getText();
        String username=textFieldUsername.getText();
        String password=textFieldPassword.getText();
        String passwordRepeat=textFieldPasswordRepeat.getText();
        String firstName=textFieldFirstName.getText();
        String lastName=textFieldLastName.getText();
        LocalDate birth=textFieldBirth.getValue();
        try {
            //long idd = Long.parseLong(id);

            //User user=new User(firstName,lastName,username,birth);

            //user.setPassword(password);
            serviceUser.verifyUsername(username);
            serviceUser.verifyPassword(password,passwordRepeat);
            serviceUser.save(firstName,lastName,username,password,birth);
            userLogin = serviceUser.findByUsername(username);
            GoToMenu(userLogin);
        }
        catch (ValidationException e) {
            MessageAlert.showErrorMessage(null,e.getMessage());
            textFieldPassword.clear();//
            textFieldPasswordRepeat.clear();
        }
        catch (IllegalArgumentException ee)
        {
            MessageAlert.showErrorMessage(null,"id null");
        }
    }
    private void GoToMenu(User user)
    {
        try {
            // create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("meniu.fxml"));

            AnchorPane root = (AnchorPane) loader.load();


            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            MenuController menuController = loader.getController();
            menuController.setService(serviceUser,serviceMessage,serviceF,serviceFr, dialogStage, user);

            dialogStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void handleCancel(){

     showWelcomeEditDialog();
    }



}
