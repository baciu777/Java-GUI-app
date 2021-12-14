package ObserverController;

import ChangeEvent.FriendshipChangeEvent;
import domain.DtoUser;
import domain.Friendship;
import domain.User;
import javafx.collections.ObservableList;
import observer.Observer;
import service.ServiceFriendship;
import service.ServiceFriendshipRequest;
import service.ServiceUser;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ObserverFriendship implements Observer<FriendshipChangeEvent> {
    ServiceFriendship serviceF;
    ServiceFriendshipRequest serviceFr;
    ObservableList<User> modelFriendship ;
    ObservableList<DtoUser> modelUser;
    User user;
    ServiceUser serviceUser;

    @Override
    public void update(FriendshipChangeEvent mess) {

        initModelFriendship();
        initModelUser();
    }

    public void setServiceModelFriendship(ServiceFriendship fr,ServiceUser u,ServiceFriendshipRequest frr, ObservableList<User> model,ObservableList<DtoUser> modelUser,User userLogin) {
        serviceUser = u;
        serviceF =fr;
        serviceFr = frr;
        user=userLogin;
        modelFriendship=model;
        modelUser = modelUser;
        serviceF.addObserver(this);
        initModelFriendship();
        initModelUser();

    }

    private void initModelFriendship() {
        Iterable<User> users = serviceF.friends(user);
        List<User> friendshipsList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        modelFriendship.setAll(friendshipsList);

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
