import domain.*;
import domain.validation.FriendshipValidator;
import domain.validation.MessageValidator;
import domain.validation.UserValidator;
import repository.Repository;
import repository.database.FriendRequestDbRepository;
import repository.database.FriendshipDbRepository;
import repository.database.MessageDbRepository;
import repository.database.UserDbRepository;
import service.ServiceFriendship;
import service.ServiceFriendshipRequest;
import service.ServiceMessage;
import service.ServiceUser;
import userinterface.UI;

/**
 * Main class
 */
public class Main {
    /**
     * main method where we start the project
     * @param args array of strings
     */
    public static void main(String[] args)
    {
        System.out.println("baciu");
        System.out.println("ioana");
        //Repository<Long, User> repo = new UserFile("data/text.in", new UserValidator());
        //System.out.println("Friendships:");
        //Repository<Tuple<Long,Long>, Friendship> repofriends = new FriendshipFile("data/friendships.in", new FriendshipValidator());

        Repository<Long, User> repoDb=new UserDbRepository("jdbc:postgresql://localhost:5432/network","postgres","ioana",new UserValidator());
        repoDb.findAll().forEach(System.out::println);
        Repository<Tuple<Long,Long>, Friendship> repoDbFr=new FriendshipDbRepository("jdbc:postgresql://localhost:5432/network","postgres","ioana",new FriendshipValidator());
        repoDbFr.findAll().forEach(System.out::println);
        Repository<Long, Message> repoDbMs=new MessageDbRepository("jdbc:postgresql://localhost:5432/network","postgres","ioana",new MessageValidator());
        Repository<Tuple<Long,Long>, FriendRequest> repoDbFrRq=new FriendRequestDbRepository("jdbc:postgresql://localhost:5432/network","postgres","ioana");

        ServiceUser servUser=new ServiceUser(repoDb,repoDbFr);
        ServiceFriendship servFriendship=new ServiceFriendship(repoDb,repoDbFr);
        ServiceMessage servMessage=new ServiceMessage(repoDbMs,repoDb);
        ServiceFriendshipRequest servFriendReq = new ServiceFriendshipRequest(repoDbFrRq,servFriendship,servUser);
        /*
        int i = 3;
        Long trei = new Long(3);
        Long patru = new Long(4);
        Long unu = new Long(1);
        Long doi= new Long(2);
        servFriendReq.deleteRequest(patru,trei);
        servFriendReq.deleteRequest(unu,trei);
        servFriendReq.deleteRequest(doi,trei);*/
        UI userInterface=new UI(servUser,servFriendship,servMessage,servFriendReq);
        userInterface.show();
    }
}
