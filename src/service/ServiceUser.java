package service;

import domain.Entity;
import domain.Friendship;
import domain.Tuple;
import domain.User;
import domain.validation.ValidationException;
import org.w3c.dom.ls.LSOutput;
import repository.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    public List<String> getFriends(Long id) throws ValidationException{
        User resp = repoUser.findOne(id);
           if(resp == null)
                throw new ValidationException("id invalid");

        HashMap<User, LocalDateTime> users= new HashMap();
        List<Friendship> result=new ArrayList<>();
        Iterable<Friendship> friendships=repoFriends.findAll();
        friendships.forEach(result::add);//add all the elements in the list result

        Predicate<Friendship> testfr1= x->x.getId().getLeft().equals(id);
        Predicate<Friendship> testfr2= x->x.getId().getRight().equals(id);
        Predicate<Friendship> testCompound=testfr1.or(testfr2);

        List<String> list = Arrays.asList();
        list=result
                .stream()
                .filter(testCompound)
                .map(x->{
                    String s="";
                    if(x.getId().getLeft().equals(id))//if the id of our user is on the left side we take the user from the right
                    s=   repoUser.findOne(x.getId().getRight()).toString2()+" "+x.getDate().toString();
                    else s=   repoUser.findOne(x.getId().getLeft()).toString2()+" "+x.getDate().toString();
                    return s;
                })
                .collect(Collectors.toCollection(ArrayList::new));


         return list;

    }
    public User findOne(Long nr) {
        if(repoUser.findOne(nr) != null)
            return repoUser.findOne(nr);
        else
            throw new ValidationException(" id invalid");
    }
    public User userexist(Long id)
    {
        for(User ur: repoUser.findAll())
            if(Objects.equals(ur.getId(), id))
                return repoUser.findOne(id);
        throw new ValidationException(" id invalid");
    }


}
