package service;

import domain.Entity;
import domain.Friendship;
import domain.Tuple;
import domain.User;
import domain.validation.ValidationException;
import repository.Repository;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * class Service that manage the operations for Users
 * repoUser-Repository for users
 * repoFriends-Repository for friendships
 */
public class ServiceUser {
    private Repository<Long, User> repoUser;
    private Repository<Tuple<Long, Long>, Friendship> repoFriends;

    /**
     * constructor for the service
     * @param RepoUser UserRepository
     * @param RepoFriends FriendsRepository
     */
    public ServiceUser(Repository<Long, User> RepoUser, Repository<Tuple<Long, Long>, Friendship> RepoFriends) {
        repoFriends = RepoFriends;
        repoUser = RepoUser;

    }



    /**
     * save a user, call the repo function
     * if is not valid throw ValidationException
     *
     * @param firstname-String
     * @param lastname-String
     */
    public void save(String firstname, String lastname) {
        User user = new User(firstname, lastname);
        long id = 0L;
        for (User ur : repoUser.findAll()) {
            if (ur.getId() > id)
                id = ur.getId();

        }
        id++;

        user.setId(id);
        User save = repoUser.save(user);
        if (save != null)
            throw new ValidationException("id already used");


    }

    /**
     * update a user, call the repo function
     * if is not valid throw ValidationException
     *
     * @param id-Long
     * @param firstname-String
     * @param lastname-String
     */
    public void update(Long id, String firstname, String lastname) {
        User user = new User(firstname, lastname);
        //user.setFriends(findOneUser(id).getFriends());
        user.setId(id);


        User save = repoUser.update(user);
        if (save != null)
            throw new ValidationException("this id does not exit");


    }

    /**
     * delete a user, call the repo function
     *
     * @param id-Long
     * @return the deleted user
     * otherwise, throw ValidationException
     */
    public Entity delete(Long id) {

        Entity deleted = repoUser.delete(id);


        if (deleted == null)
            throw new ValidationException("id invalid");


        return deleted;

    }


    /**
     * get the maximum id
     *
     * @return the result-Long
     */
    public Long get_size() {
        Long maxim = 0L;
        for (User ur : repoUser.findAll())
            if (ur.getId() > maxim)
                maxim = ur.getId();
        return maxim;
    }

    /**
     * @return all the users
     */
    public Iterable<User> printUs() {
        return repoUser.findAll();
    }



    /**
     * find one user with a specific id
     * @return the user
     * throw exception if is not found
     * @param id-Long
     */
    public User findOneUser(Long id) {

        if (repoUser.findOne(id) == null)
            throw new ValidationException("this id does not belong to the list");
        return repoUser.findOne(id);
    }

    /**
     * Display friends for a given user
     * @param id integer id of a possible user
     * @return the list of users friends with the one given
     * @throws ValidationException if the id for user given is invalid
     */
    public Iterable<User> getFriends(Long id) throws ValidationException{
        try{
            Set<User> users= new HashSet<>();
            User resp = repoUser.findOne(id);
            if(resp == null)
                throw new ValidationException("id invalid");
            else
                for (Friendship fr : repoFriends.findAll()){
                    if(Objects.equals(fr.getId().getLeft(), id))
                        users.add(repoUser.findOne(fr.getId().getRight()));
                    if(Objects.equals(fr.getId().getRight(), id))
                        users.add(repoUser.findOne(fr.getId().getLeft()));
                }
            return users;
        }
        catch (ValidationException exception){
            throw new ValidationException(exception);
        }
    }
    public User findOne(Long nr) {
        if(repoUser.findOne(nr) != null)
            return repoUser.findOne(nr);
        else
            throw new ValidationException(" id invalid");
    }



}
