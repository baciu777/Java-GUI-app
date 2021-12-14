package ObserverController;

import ChangeEvent.FriendshipChangeEvent;
import ChangeEvent.FriendshipReqChangeEvent;
import domain.DtoFriendReq;
import domain.FriendRequest;
import domain.Friendship;
import domain.User;
import javafx.collections.ObservableList;
import observer.Observer;
import service.ServiceFriendshipRequest;
import service.ServiceUser;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ObserverFriendReq implements Observer<FriendshipReqChangeEvent> {
    ServiceFriendshipRequest serviceFr;
    ObservableList<DtoFriendReq> modelFriendshipReq ;
    User user;
    ServiceUser serviceUser;
    @Override
    public void update(FriendshipReqChangeEvent mess) {
        initModelFriendshipReq();
    }



    public void setServiceModelFriendshipReq(ServiceUser servUser,ServiceFriendshipRequest fr, ObservableList<DtoFriendReq> model, User userLogin) {
        this.serviceUser=servUser;
        serviceFr=fr;
        user=userLogin;
        modelFriendshipReq=model;
        serviceFr.addObserver(this);

        initModelFriendshipReq();

    }

    private void initModelFriendshipReq() {
        Predicate<FriendRequest> certainUserLeft=x->x.getId().getLeft().equals(user.getId());
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


}
