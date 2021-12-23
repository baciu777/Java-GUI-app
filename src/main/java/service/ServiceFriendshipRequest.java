package service;

import ChangeEvent.*;
import domain.*;
import observer.Observable;
import observer.Observer;
import repository.Repository;
import repository.database.FriendRequestDbRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ServiceFriendshipRequest   {
    /**
     * constructor for the service
     *
     * @param repoUser    UserRepository
     * @param repoFriends FriendsRepository
     */

    ServiceFriendship servFriendship;
    ServiceUser servUser;
    Repository<Tuple<Long, Long>, FriendRequest> request_repo;

    public ServiceFriendshipRequest(Repository<Tuple<Long, Long>, FriendRequest> repo_request,
                                    ServiceFriendship servFriendship,
                                    ServiceUser servUser) {

        this.servUser = servUser;
        this.request_repo = repo_request;
        this.servFriendship = servFriendship;
    }

    /**
     * accepts a request sent from id1 to id2
     * @param id1 long
     * @param id2 long
     * @throws Exception if the request doesn't exist
     */
    private void acceptRequest(Long id1,Long id2) throws Exception {
        List<FriendRequest> test = (List<FriendRequest>) findAllTo(findAllFrom(findWithStatus(findAll(),"PENDING"),id1),id2);
        if(test.isEmpty())
            throw new Exception("request does not exist");
        FriendRequest f = new FriendRequest();
        Tuple<Long, Long> longLongTuple =new Tuple<>();
        longLongTuple.setLeft(id1);
        longLongTuple.setRight(id2);
        f.setId(longLongTuple);
        f.setStatus("APPROVED");
        f.setDate(LocalDateTime.now());
        request_repo.update(f);
        servFriendship.addFriend(id1,id2);

    }

    /**
     * sents a request from id1 to id2
     * @param id1 long
     * @param id2 long
     */

    private void sendRequest(Long id1, Long id2)
    {
        FriendRequest f = new FriendRequest();
        Tuple<Long, Long> longLongTuple =new Tuple<>();
        longLongTuple.setLeft(id1);
        longLongTuple.setRight(id2);
        f.setDate(LocalDateTime.now());
        f.setId(longLongTuple);
        f.setStatus("PENDING");
        request_repo.save(f);

    }

    /**
     * rejects a request from id2 to id1
     * @param id1 long
     * @param id2 long
     * @throws Exception if the request does not exist
     */
    public void rejectRequest(Long id1, Long id2) throws Exception {
        servUser.findOne(id1);
        servUser.findOne(id2);
        List<FriendRequest> test = (List<FriendRequest>) findAllTo(findAllFrom(findWithStatus(findAll(),"PENDING"),id2),id1);
        if(test.isEmpty())
        {
            test = (List<FriendRequest>) findAllTo(findAllFrom(findWithStatus(findAll(),"PENDING"),id1),id2);
            if(test.isEmpty())
                throw new Exception("request does not exist");
            else{
                deleteRequest(id1,id2);
                deleteRequest(id2,id1);
            }
        }
        FriendRequest f = new FriendRequest();
        Tuple<Long, Long> longLongTuple =new Tuple<>();
        longLongTuple.setLeft(id2);
        longLongTuple.setRight(id1);
        f.setId(longLongTuple);
        f.setStatus("REJECTED");
        f.setDate(LocalDateTime.now());
        request_repo.update(f);

    }

    /**
     *  handles befriending (send or accept requests)
     * @param id1 long
     * @param id2 long
     * @throws Exception if id1 and id2 are already friends
     */
    public void addFriend(Long id1, Long id2) throws Exception {
        User u1=servUser.findOne(id1);
        User u2=servUser.findOne(id2);
        if(servFriendship.areFriends(u1,u2))
            throw new Exception(" users are already friends");
        Tuple<Long, Long> longLongTuple =new Tuple<>();
        longLongTuple.setLeft(id2);
        longLongTuple.setRight(id1);
        FriendRequest f = request_repo.findOne(longLongTuple);
        if(f==null)
            sendRequest(id1,id2);
        else
        if(Objects.equals(f.getStatus(), "PENDING"))
            acceptRequest(id2,id1);

    }

    /**
     * deletes a request
     * @param id1 long
     * @param id2 long
     */
    public void deleteRequest(Long id1, Long id2)
    {
        Tuple<Long, Long> longLongTuple =new Tuple<>();
        longLongTuple.setLeft(id2);
        longLongTuple.setRight(id1);
        request_repo.delete(longLongTuple);
    }
    public void check_update_deletes(User u1,User u2)
    {

        if(servFriendship.areFriends(u1,u2)) {
            deleteRequest(u1.getId(), u2.getId());
            deleteRequest(u2.getId(),u1.getId());
        }

    }
    /**
     *
     * @return all the requests
     */
    public Iterable<FriendRequest> findAll()
    {
        return request_repo.findAll();
    }

    public Iterable<FriendRequest> findAllRenew()
    {
        Predicate<FriendRequest> certainFilter1 = x -> x.getStatus().equals("PENDING");




        List<FriendRequest> friendshipsReqList = StreamSupport.stream(this.findAll().spliterator(), false)
                .filter(certainFilter1)
                .collect(Collectors.toList());
    return friendshipsReqList;
    }

    /**
     *  finds all the request from an id
     * @param all iterable
     * @param id long
     * @return iterable
     */
    public Iterable<FriendRequest> findAllFrom(Iterable<FriendRequest> all ,Long id)
    {
        if(all == null)
            return null;
        List<FriendRequest> result=new ArrayList<>();
        all.forEach(result::add);
        List<FriendRequest> list = Arrays.asList();
        list = result.stream().filter(x-> Objects.equals(x.getId().getLeft(), id)).collect(Collectors.toCollection(ArrayList::new));
        return list;
    }
    /**
     *  finds all the request to an id
     * @param all iterable
     * @param id long
     * @return iterable
     */
    public Iterable<FriendRequest> findAllTo(Iterable<FriendRequest> all ,Long id)
    {
        if(all == null)
            return null;
        List<FriendRequest> result=new ArrayList<>();
        all.forEach(result::add);
        List<FriendRequest> list = Arrays.asList();
        list = result.stream().filter(x-> Objects.equals(x.getId().getRight(), id)).collect(Collectors.toCollection(ArrayList::new));
        return list;
    }


    /**
     *  finds all the request with a given status
     * @param all iterable
     * @param status String
     * @return iterable
     */
    public Iterable<FriendRequest> findWithStatus(Iterable<FriendRequest> all,String status)
    {
        if(all == null)
            return null;
        List<FriendRequest> result=new ArrayList<>();
        all.forEach(result::add);
        List<FriendRequest> list = Arrays.asList();
        list = result.stream().filter(x-> Objects.equals(x.getStatus(), status)).collect(Collectors.toCollection(ArrayList::new));
        return list;
    }

    /***
     * sees the status of between to users
     * @param me id of first person
     * @param him id of second person
     * @return string: "requested" - first sent the request
     *                  "respond" - second person sent the request
     *                  "rejected" - second person rejected the request
     */
    public String get_request_status(Long me, Long him)
    {
        Iterable<FriendRequest> findAll=request_repo.findAll();
        List<FriendRequest> aux1 = (List<FriendRequest>)findWithStatus(findAll, "PENDING");
        List<FriendRequest> aux2 =(List<FriendRequest>)findAllTo(aux1,him);
        List<FriendRequest> aux3 =(List<FriendRequest>)findAllFrom(aux2,me);
        if(aux3.size()!=0)
        {
            return "requested";
        }
        aux2 = (List<FriendRequest>)findAllTo(aux1,me);
        aux3 = (List<FriendRequest>)findAllFrom(aux2,him);
        if(aux3.size()!=0)
        {
            return "respond";
        }
        aux1 = (List<FriendRequest>)findWithStatus(findAll, "REJECTED");
        aux2 = (List<FriendRequest>)findAllTo(aux1,him);
        aux3 = (List<FriendRequest>)findAllFrom(aux2,me);
        if(aux3.size()!=0)
        {
            return "rejected";
        }
        return null;
    }

    public FriendRequest findOne(Tuple<Long,Long> id) {
        return request_repo.findOne(id);
    }
}
