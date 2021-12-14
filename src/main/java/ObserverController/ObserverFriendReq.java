package ObserverController;

import ChangeEvent.FriendshipChangeEvent;
import ChangeEvent.FriendshipReqChangeEvent;
import domain.*;
import javafx.collections.ObservableList;
import observer.Observer;
import service.ServiceFriendship;
import service.ServiceFriendshipRequest;
import service.ServiceUser;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ObserverFriendReq implements Observer<FriendshipReqChangeEvent> {
    ServiceFriendshipRequest serviceFr;
    ObservableList<DtoFriendReq> modelFriendshipReq ;
    ObservableList<DtoUser> modelUser;
    User user;
    ServiceUser serviceUser;
    ServiceFriendship serviceF;
    @Override
    public void update(FriendshipReqChangeEvent mess) {
        initModelFriendshipReq();
        initModelUser();
    }



    public void setServiceModelFriendshipReq(ServiceUser servUser,ServiceFriendship f,ServiceFriendshipRequest fr, ObservableList<DtoFriendReq> model,ObservableList<DtoUser> model2 , User userLogin) {
        this.serviceUser=servUser;
        serviceFr=fr;
        serviceF = f;
        user=userLogin;
        modelFriendshipReq=model;
        modelUser = model2;
        serviceFr.addObserver(this);
        serviceF.addObserver(this);
        initModelFriendshipReq();
        initModelUser();

    }

    private void initModelFriendshipReq() {
        Iterable<FriendRequest> friendReq = serviceFr.findAll();
        List<DtoFriendReq> friendshipsReqList = StreamSupport.stream(friendReq.spliterator(), false)
                .map(x->
                {
                    if (Objects.equals(x.getId().getRight(), user.getId()))
                        return new DtoFriendReq(serviceUser.findOne(x.getId().getLeft()).getFirstName() + " " + serviceUser.findOne(x.getId().getLeft()).getLastName(), x.getDate(), x.getStatus());

                    if (Objects.equals(x.getId().getLeft(), user.getId()))
                        return new DtoFriendReq(serviceUser.findOne(x.getId().getLeft()).getFirstName() + " " + serviceUser.findOne(x.getId().getLeft()).getLastName(), x.getDate(), x.getStatus());

            return null;
                })
                .collect(Collectors.toList());
        modelFriendshipReq.setAll(friendshipsReqList);

    }
    private void initModelUser() {
        Iterable<User> users = serviceUser.printUs();
        Iterable<Friendship> friendships = serviceF.printFr();
        List<DtoUser> usersList = StreamSupport.stream(users.spliterator(), false)
                .map(x->
                {
                    String fullName = x.getFirstName() + x.getLastName();
                    DtoUser us = new DtoUser(fullName, " not friends");
                    if(serviceF.areFriends(user.getId(), x.getId()))
                        us.setRelation("friend");
                    String reqStatus = serviceFr.get_request_status(user.getId(), x.getId());
                    if(reqStatus!=null)
                        us.setRelation(reqStatus);
                    return us;
                })
                .collect(Collectors.toList());
        modelUser.setAll(usersList);

    }


}
