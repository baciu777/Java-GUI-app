package ObserverController;

import ChangeEvent.Event;
import domain.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import observer.Observer;
import service.ServiceFriendship;
import service.ServiceFriendshipRequest;
import service.ServiceMessage;
import service.ServiceUser;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public abstract class GenericObserver implements Observer<Event>{

    public ObservableList<DtoUser> modelUser = FXCollections.observableArrayList();
    public ObservableList<User> modelFriendship = FXCollections.observableArrayList();
    public ObservableList<DtoFriendReq> modelFriendshipReq= FXCollections.observableArrayList() ;
    public ObservableList<DtoMessage> modelMessage = FXCollections.observableArrayList();
    protected ServiceFriendship serviceF;
    protected ServiceFriendshipRequest serviceFr;
    protected ServiceUser serviceUser;
    protected ServiceMessage serviceMessage;
    User user;





    public void setService(ServiceUser service, ServiceMessage mess, ServiceFriendship serviceFriendshipNew, ServiceFriendshipRequest serviceFriendRequestt, User user) {
        this.serviceUser = service;
        this.serviceMessage=mess;
        this.serviceF=serviceFriendshipNew;
        this.user=user;
        this.serviceFr=serviceFriendRequestt;
        this.serviceFr.addObserver(this);
        this.serviceF.addObserver(this);
        this.serviceMessage.addObserver(this);
        this.serviceUser.addObserver(this);


    }

    protected void initModelFriendshipReq() {
        Predicate<FriendRequest> certainUserLeft= x->x.getId().getLeft().equals(user.getId());
        Predicate<FriendRequest> certainUserRight=x->x.getId().getRight().equals(user.getId());
        Predicate<FriendRequest> testCompound = certainUserLeft.or(certainUserRight);

        Iterable<FriendRequest> friendReq = serviceFr.findAll();
        List<DtoFriendReq> friendshipsReqList = StreamSupport.stream(friendReq.spliterator(), false)
                .filter(testCompound)
                .map(x->
                {

                    return new DtoFriendReq(serviceUser.findOne(x.getId().getLeft()).getFirstName() + " " + serviceUser.findOne(x.getId().getLeft()).getLastName(),serviceUser.findOne(x.getId().getRight()).getFirstName() + " " + serviceUser.findOne(x.getId().getRight()).getLastName(), x.getDate(), x.getStatus());


                })
                .collect(Collectors.toList());
        modelFriendshipReq.setAll(friendshipsReqList);

    }
    protected void initModelFriendship() {
        Iterable<User> users = serviceF.friends(user);
        List<User> friendshipsList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        modelFriendship.setAll(friendshipsList);

    }
    protected void initModelMessage() {
        Iterable<Message> messages = serviceMessage.userMessages(user);
        List<DtoMessage> messageTaskList = StreamSupport.stream(messages.spliterator(), false)

                .map(x ->
                        {
                            if (x.getReply() == null)
                                return new DtoMessage(x.getFrom().getFirstName() +" "+ x.getFrom().getLastName(), x.getMessage(), x.getDate(), null);

                            else
                                return new DtoMessage(x.getFrom().getFirstName() +" "+ x.getFrom().getLastName(), x.getMessage(), x.getDate(), x.getReply().getId());
                        }

                )
                .collect(Collectors.toList());
        modelMessage.setAll(messageTaskList);

    }
    protected void initModelUser() {
        Iterable<User> users = serviceUser.printUs();
        Iterable<Friendship> friendships = serviceF.printFr();
        List<DtoUser> usersList = StreamSupport.stream(users.spliterator(), false)
                .map(x->
                {
                    String fullName = x.getFirstName() +" "+ x.getLastName();
                    DtoUser us = new DtoUser(fullName, " not friends");
                    if(serviceF.areFriends(user.getId(), x.getId()))
                        us.setRelation("friend");
                    String reqStatus = serviceFr.get_request_status(user.getId(), x.getId());
                    if(reqStatus!=null)
                        us.setRelation(reqStatus);
                    if(Objects.equals(x.getId(), user.getId()))
                        us.setRelation("you");
                    return us;
                })
                .collect(Collectors.toList());
        modelUser.setAll(usersList);

    }


}
