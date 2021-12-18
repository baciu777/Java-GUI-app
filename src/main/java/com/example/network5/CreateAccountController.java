package com.example.network5;

import domain.User;
import domain.validation.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import service.ServiceUser;

import java.time.LocalDate;

public class CreateAccountController {

    Stage dialogStage;
    User user;
    ServiceUser servUser;

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


    public void setService(ServiceUser servUserr, Stage dialogStage) {

        this.dialogStage=dialogStage;

        servUser=servUserr;


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
            servUser.verifyUsername(username);
            servUser.verifyPassword(password,passwordRepeat);
            servUser.save(firstName,lastName,username,password,birth);

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

    @FXML
    public void handleCancel(){
        dialogStage.close();
    }

}
