package com.example.network5;

import domain.Message;
import domain.Page;
import domain.User;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import service.ServiceMessage;
import service.ServiceUser;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PdfController {
    @FXML
    TextField PathText;
    public DatePicker startDate;
    public DatePicker endDate;

    public Page userLogin;

    public void set(Page user) {

        this.userLogin = user;

    }

    @FXML
    public void initialize() {


        PathText.setText("C:/Users/ioana/Documents/GitHub/pdfs/first.pdf");
    }
    @FXML

    public List<Message> getHistoryMess()
    {
        LocalDateTime start = startDate.getValue().atStartOfDay();
        LocalDateTime end = endDate.getValue().atTime(23,59);
        Predicate<Message> p1 = n ->n.getDate().isBefore(end) && n.getDate().isAfter(start);

        List<Message> mess = userLogin.getMessages();
        List<Message> output = mess.stream()
                .filter(p1)
                .collect(Collectors.toList());
        return output;
    }
    public void handleExecute()
    {
        try{
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
            for(User x: friends)
            {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                contentStream.newLineAtOffset(100, indexline);
                String textx = x.toString3();
                contentStream.showText(textx);
                contentStream.endText();
                indexline = indexline - 30;
            }
            for(Message x: getHistoryMess())
            {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
                contentStream.newLineAtOffset(100, indexline);
                String textx = x.toString();
                contentStream.showText(textx);
                contentStream.endText();
                indexline = indexline - 30;
            }
            contentStream.close();

            System.out.println("Content added");

            document.save(PathText.getText());


            System.out.println("PDF created");



            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
