package service;

import domain.Entity;
import domain.Friendship;
import domain.Tuple;
import domain.User;
import domain.validation.ValidationException;
import repository.Repository;

import java.time.LocalDateTime;

/**
 * class ServiceFriendship that manage the operations for Friendships
 * repoUser-Repository for users
 * repoFriends-Repository for friendships
 */
public class ServiceFriendship  {
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
        Entity save = repoFriends.save(fr);
        if (save != null)
            throw new ValidationException("ids are already used");


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
        Entity del = repoFriends.delete(t);
        if (del == null)
            throw new ValidationException("ids are not used in a friendship");


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
}
