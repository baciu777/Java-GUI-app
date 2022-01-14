package com.example.network5;

import domain.Message;
import domain.Page;
import domain.User;
import domain.validation.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PdfController {
    @FXML
    TextField NameField;
    public DatePicker startDate;
    public DatePicker endDate;
    private String path;
    public Page userLogin;


    public void set(Page user) {

        this.userLogin = user;

    }


    @FXML
    public void initialize() {

        path="D:/school-projects/java-projects/";

        NameField.setText("first.pdf");

    }

    public void handleDirectory() {
        Stage stage = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(new File("src"));
        File selectedDirectory = directoryChooser.showDialog(stage);
        try {



            String s = selectedDirectory.getAbsolutePath();
            s = s.replace('\\', '/');
            s = s + "/";
            path=s;

        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }

    }

    @FXML

    public List<Message> getHistoryMess() {
        LocalDateTime start = startDate.getValue().atStartOfDay();
        LocalDateTime end = endDate.getValue().atTime(23, 59);
        Predicate<Message> p1 = n -> n.getDate().isBefore(end) && n.getDate().isAfter(start);

        List<Message> mess = userLogin.getMessages();
        List<Message> output = mess.stream()
                .filter(p1)
                .collect(Collectors.toList());
        return output;
    }

    public void handleExecute() {
        try {
            LocalDate dateS = startDate.getValue();
            LocalDate dateE = endDate.getValue();

            if(NameField==null)
                throw new ValidationException("select a name for the export file");
            if(!NameField.getText().endsWith(".pdf"))
                throw new ValidationException("select a pdf type");
            if (dateS == null)
                throw new ValidationException("the starting date is null");
            if (dateS.isAfter(LocalDate.now()))
                throw new ValidationException("Starting date must not be in the future");
            if (dateE == null)
                throw new ValidationException("the ending date is null");
            if (dateE.isAfter(LocalDate.now()))
                throw new ValidationException("Ending date must not be in the future");

            PDDocument document = new PDDocument();
            PDPage my_page = new PDPage();
            document.addPage(my_page);


            PDPageContentStream contentStream = new PDPageContentStream(document, my_page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 20);
            contentStream.newLineAtOffset(100, 700);
            String text1 = userLogin.toString3();
            contentStream.showText(text1);
            contentStream.endText();


            List<User> friends = userLogin.getFriends();
            int indexline = 650;
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 15);
            contentStream.newLineAtOffset(100, indexline);
            contentStream.showText("Prieteni:");
            contentStream.endText();
            indexline = indexline - 30;
            for (User x : friends) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                contentStream.newLineAtOffset(120, indexline);
                String textx = x.toString3();
                contentStream.showText(textx);
                contentStream.endText();
                indexline = indexline - 30;
            }
            contentStream.beginText();
            contentStream.setFont(PDType1Font.TIMES_ROMAN, 15);
            contentStream.newLineAtOffset(100, indexline);
            contentStream.showText("Mesaje:");
            contentStream.endText();
            indexline = indexline - 30;
            for (Message x : getHistoryMess()) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                contentStream.newLineAtOffset(120, indexline);
                String textx = x.toString();
                contentStream.showText(textx);
                contentStream.endText();
                indexline = indexline - 30;
            }
            contentStream.close();

            System.out.println("Content added");
            path =path+ NameField.getText();
            document.save(path);


            System.out.println("PDF created");


            document.close();
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
