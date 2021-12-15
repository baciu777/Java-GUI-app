package service;

import ChangeEvent.ChangeEventType;

import domain.*;
import domain.validation.ValidationException;
import observer.Observable;
import observer.Observer;
import repository.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import ChangeEvent.*;
/**
 * class ServiceFriendship that manage the operations for Friendships
 * repoUser-Repository for users
 * repoFriends-Repository for friendships
 */
public class ServiceFriendship implements Observable<Event> {
    private Repository<Long, User> repoUser;
    private Repository<Tuple<Long, Long>, Friendship> repoFriends;
    private List<Observer<Event>> observers=new ArrayList<>();

    /**
     * constructor for the service
     * @param repoUser UserRepository
     * @param repoFriends FriendsRepository
     */
    public ServiceFriendship(Repository<Long, User> repoUser, Repository<Tuple<Long, Long>, Friendship> repoFriends) {
        this.repoUser = repoUser;
        this.repoFriends = repoFriends;


    }
    /**
     * method called in constructor to add the friends for users
     */
    //private void friends_list() {
        //for (Friendship fr : repoFriends.findAll()) {
            //User u1 = repoUser.findOne(fr.getId().getLeft());
            //User u2 = repoUser.findOne(fr.getId().getRight());
            //u1.addFriend(u2);
            //u2.addFriend(u1);
        //}
    //}
    /**
     * add a friendship, call the repo function
     * if is not valid throw ValidationException
     *
     * @param id1-Long
     * @param id2-Long
     */
    public void addFriend(Long id1, Long id2) {
        Friendship fr = new Friendship();
        Tuple t = new Tuple(id1, id2);
        fr.setId(t);
        fr.setDate(LocalDateTime.now());
        if (repoUser.findOne(id1) == null)
            throw new ValidationException("first id does not exist");
        if (repoUser.findOne(id2) == null)
            throw new ValidationException("second id does not exist");
        Friendship save = repoFriends.save(fr);
        if (save != null)
            throw new ValidationException("ids are already used");

        notifyObservers(new Event(ChangeEventType.ADD,save));
    }

    /**
     * delete a friendship, call the repo function
     * if is not valid throw ValidationException
     *
     * @param id1-Long
     * @param id2-Long
     */
    public void deleteFriend(Long id1, Long id2) {

        Tuple t = new Tuple(id1, id2);
        if (repoUser.findOne(id1) == null)
            throw new ValidationException("first id does not exist");
        if (repoUser.findOne(id2) == null)
            throw new ValidationException("second id does not exist");
        Friendship del = repoFriends.delete(t);
        if (del == null)
            throw new ValidationException("you are not in a friendship");

        notifyObservers(new Event(ChangeEventType.DELETE,del));
    }
    /**
     * @return all the friendships
     */
    public Iterable<Friendship> printFr() {
        return repoFriends.findAll();
    }
    public boolean areFriends(Long id1, Long id2)
    {
        Tuple t1 = new Tuple(id1, id2);
        return repoFriends.findOne(t1)!=null;
    }

    public Iterable<Friendship> findAll() {
        return repoFriends.findAll();
    }



    public Iterable<User> friends(User user)
    {
        Iterable<User> friendsList=new ArrayList<>();
        for(Friendship fr:repoFriends.findAll())
        {
            if(Objects.equals(fr.getId().getLeft(), user.getId()))
                ((ArrayList<User>) friendsList).add( repoUser.findOne(fr.getId().getRight()));
            if(Objects.equals(fr.getId().getRight(), user.getId()))
                ((ArrayList<User>) friendsList).add( repoUser.findOne(fr.getId().getLeft()));

        }
        return friendsList;
    }

    @Override
    public void addObserver(Observer<Event> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<Event> e) {

    }

    @Override
    public void notifyObservers(Event t) {
        observers.stream().forEach(x->x.update(t));
    }
}
