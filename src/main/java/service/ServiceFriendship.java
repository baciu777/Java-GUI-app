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
public class ServiceFriendship implements Observable<FriendChangeEvent>{
    private Repository<Long, User> repoUser;
    private Repository<Tuple<Long, Long>, Friendship> repoFriends;


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
        notifyObservers(new FriendChangeEvent(fr,"add"));

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
        notifyObservers(new FriendChangeEvent(del,"del"));

    }
    /**
     * @return all the friendships
     */
    public Iterable<Friendship> printFr() {
        return repoFriends.findAll();
    }
    public boolean areFriends(User user1, User user2)
    {
        for(User ur:user2.getFriends())
        {

            if(Objects.equals(ur.getId(), user1.getId()))
                return true;

        }
        return false;

    }

    public Iterable<Friendship> findAll() {
        return repoFriends.findAll();
    }



    public Iterable<User> friends(Long id)
    {
        Iterable<User> friendsList=new ArrayList<>();
        User user=repoUser.findOne(id);
        for(User ur:user.getFriends())
        {

                ((ArrayList<User>) friendsList).add( ur);

        }
        return friendsList;
    }

    private List<Observer<FriendChangeEvent>> observers=new ArrayList<>();
    @Override
    public void addObserver(Observer<FriendChangeEvent> e) {

        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<FriendChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(FriendChangeEvent t) {
        observers.stream().forEach(x->x.update(t));
    }
}
