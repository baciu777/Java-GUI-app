package ObserverController;

import ChangeEvent.FriendshipChangeEvent;
import domain.Friendship;
import domain.User;
import javafx.collections.ObservableList;
import observer.Observer;
import service.ServiceFriendship;
import service.ServiceUser;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ObserverFriendship implements Observer<FriendshipChangeEvent> {
    ServiceFriendship serviceFr;
    ObservableList<User> modelFriendship ;
    User user;

    @Override
    public void update(FriendshipChangeEvent mess) {
        initModelFriendship();
    }

    public void setServiceModelFriendship(ServiceFriendship fr, ObservableList<User> model,User userLogin) {

        serviceFr=fr;
        user=userLogin;
        modelFriendship=model;
        serviceFr.addObserver(this);

        initModelFriendship();

    }

    private void initModelFriendship() {
        Iterable<User> users = serviceFr.friends(user);
        List<User> friendshipsList = StreamSupport.stream(users.spliterator(), false)
                .collect(Collectors.toList());
        modelFriendship.setAll(friendshipsList);

    }
}
