package service;

import ChangeEvent.Event;

import domain.*;
import domain.validation.ValidationException;
import observer.Observer;
import repository.Repository;
import utils.BCrypt;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * class Service that manage the operations for Users
 * repoUser-Repository for users
 * repoFriends-Repository for friendships
 */
public class ServiceUser  {
    private Repository<Long, User> repoUser;
    private Repository<Tuple<Long, Long>, Friendship> repoFriends;


    /**
     * constructor for the service
     *
     * @param RepoUser    UserRepository
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
    public void save(String firstname, String lastname, String username, String password, LocalDate birth) {
        User user = new User(firstname, lastname, username, birth);
        //long id = 0L;

        user.setPassword(password);
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
    public void update(Long id, String firstname, String lastname, String username, String password, LocalDate birth) {
        User user = new User(firstname, lastname, username, birth);

        user.setId(id);

        user.setPassword(password);
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

        User deleted = repoUser.delete(id);


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
     * returns list of friendships for a user
     *
     * @param id id of the user
     * @return List<Friendship></Friendship>
     */
    private List<Friendship> getFriendsAsFriendships(Long id) {
        User resp = repoUser.findOne(id);
        if (resp == null)
            throw new ValidationException("id invalid");

        HashMap<User, LocalDateTime> users = new HashMap();
        List<Friendship> result = new ArrayList<>();
        Iterable<Friendship> friendships = repoFriends.findAll();
        friendships.forEach(result::add);//add all the elements in the list result

        Predicate<Friendship> testfr1 = x -> x.getId().getLeft().equals(id);
        Predicate<Friendship> testfr2 = x -> x.getId().getRight().equals(id);
        Predicate<Friendship> testCompound = testfr1.or(testfr2);

        List<Friendship> list = Arrays.asList();
        list = result
                .stream()
                .filter(testCompound)
                .collect(Collectors.toCollection(ArrayList::new));


        return list;

    }

    /**
     * Display friends for a given user
     *
     * @param id integer id of a possible user
     * @return the list of users friends with the one given
     * @throws ValidationException if the id for user given is invalid
     */
    public List<String> getFriends(Long id) throws ValidationException {

        List<Friendship> result = getFriendsAsFriendships(id);
        List<String> list = Arrays.asList();
        list = result.stream().map(x -> {
            String s = "";
            if (x.getId().getLeft().equals(id))//if the id of our user is on the left side we take the user from the right
                s = repoUser.findOne(x.getId().getRight()).toString2() + " " + x.getDate().toString();
            else s = repoUser.findOne(x.getId().getLeft()).toString2() + " " + x.getDate().toString();
            return s;
        }).collect(Collectors.toCollection(ArrayList::new));
        return list;
    }

    /**
     * gets all the friends made in a month for a user
     *
     * @param id    : Long , the user id
     * @param Month : Long
     * @return List<String>
     */
    public List<String> getFriendsByMonth(Long id, Long Month) {

        Predicate<Friendship> checkMonth = x -> x.getDate().getMonthValue() == Month;
        List<Friendship> result = getFriendsAsFriendships(id);
        List<String> list = Arrays.asList();
        list = result.stream().filter(checkMonth).map(x -> {
            String s = "";
            if (x.getId().getLeft().equals(id))//if the id of our user is on the left side we take the user from the right
                s = repoUser.findOne(x.getId().getRight()).toString2() + " " + x.getDate().toString();
            else s = repoUser.findOne(x.getId().getLeft()).toString2() + " " + x.getDate().toString();
            return s;
        }).collect(Collectors.toCollection(ArrayList::new));
        return list;
    }

    /**
     * Find one user
     *
     * @param nr-id of user
     * @return the user if exists
     * otherwise, throw exception
     */
    public User findOne(Long nr) {
        if (repoUser.findOne(nr) != null)
            return repoUser.findOne(nr);
        else
            throw new ValidationException(" id invalid");
    }

    public List<User> findbyName(String name) {
        String[] splited = name.split(" ", 2);
        Iterable<User> aux = repoUser.findAll();
        List<User> result = new ArrayList<>();
        aux.forEach(result::add);
        return result.stream().filter(x -> {
                    if (Objects.equals(x.getFirstName(), splited[0]) && Objects.equals(x.getLastName(), splited[1]))
                        return true;
                    return false;
                }

        ).collect(Collectors.toCollection(ArrayList::new));
    }

    public User findbyNameFirst(String name) throws Exception {
        List<User> l = findbyName(name);
        if (l.size() == 0)
            throw new Exception("User not found");
        return l.get(0);
    }


    public Page findByUsername(String username) {
        Iterable<User> l = repoUser.findAll();
        for (User ur : l) {
            if (Objects.equals(ur.getUsername(), username)) {
                Page x=new Page(ur.getFirstName(), ur.getLastName(),ur.getUsername(),ur.getBirth());
                x.setPassword(ur.getPassword());
                x.setId(ur.getId());
                return x;
            }

        }
       throw new ValidationException("this username doesn't exist");
    }

    public boolean verifyUsername(String username) {
        Iterable<User> l = repoUser.findAll();
        for (User ur : l) {
            if (Objects.equals(ur.getUsername(), username))
                throw new ValidationException("Username already exists");

        }
        return true;


    }


    public void verifyPassword(String password, String passwordRepeat) {
        if(!Objects.equals(password, passwordRepeat))
            throw new ValidationException("Confirmation password incorrect");

    }

    public void verifyPasswordUser(String passwordUser,User user) {



        if(!BCrypt.checkpw(passwordUser,user.getPassword()))
            throw new ValidationException("Password incorrect");

    }
}
