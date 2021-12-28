package com.example.network5;

import domain.Page;
import domain.User;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import service.ServiceFriendship;
import service.ServiceUser;

import java.util.ArrayList;
import java.util.List;

public class OneUserFriendController {
    User userfriend;
    Page userLogged;
    ServiceFriendship serv;
    @FXML
    Button deleteFriend;
    @FXML
    Label userName;
    @FXML
    HBox container;
    @FXML
    ImageView userImage;
    FriendsController control;
    public void set(User userfriend,Page userLogged,ServiceFriendship serv,FriendsController control)
    {
        this.control = control;
        this.serv =serv;
        this.userfriend = userfriend;
        this.userLogged = userLogged;
        setLogic();
        setStyle();
    }
    public void setLogic()
    {
        container = new HBox();
        userName = new Label();
        deleteFriend = new Button("delete");
        deleteFriend.setVisible(false);
        container.setOnMousePressed(event->{deleteFriend.setVisible(true);});
        container.setOnMouseExited(event->{deleteFriend.setVisible(false);});
        deleteFriend.setOnAction(event->{deleteFriend();});
        userName.setText(userfriend.getFirstName()+" "+userfriend.getLastName());

        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        container.getChildren().add(userName);
        container.getChildren().add(region1);
        container.getChildren().add(deleteFriend);
    }
    public void setStyle()
    {

        container.setMinSize(200,50);
        container.setMaxSize(300,50);
        container.setStyle("-fx-background-color: #2d2d31;" +
                "-fx-padding: 10 20 10 20;" +
                "-fx-background-radius: 20");
        deleteFriend.setStyle("-fx-background-color: transparent; -fx-border-color: #e9e3d1;" +
                "-fx-text-fill:#e9e3d1; -fx-font-family: Century Gothic;");
        userName.setStyle("-fx-text-fill:#e9e3d1;" +
                "-fx-font-family: Century Gothic; ");
    }
    public HBox getBox()
    {
        return container;
    }

    public void deleteFriend()
    {
        serv.deleteFriend(userfriend.getId(),userLogged.getId());
       // userLogged.deleteFriend(userfriend);
        List<User> newFR=new ArrayList<>();
        //stergem si din prietenii userului logat(page)
        userLogged.removeFriend(userfriend);
        userLogged.setFriends(newFR);
        control.initModelFriendship();
    }
}
