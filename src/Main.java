import domain.Friendship;
import domain.Tuple;
import domain.User;
import domain.validation.FriendshipValidator;
import domain.validation.UserValidator;
import repository.Repository;
import repository.database.FriendshipDbRepository;
import repository.database.UserDbRepository;
import service.ServiceFriendship;
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
        //Repository<Long, User> repo = new UserFile("data/text.in", new UserValidator());
        //System.out.println("Friendships:");
        //Repository<Tuple<Long,Long>, Friendship> repofriends = new FriendshipFile("data/friendships.in", new FriendshipValidator());

        Repository<Long, User> repoDb=new UserDbRepository("jdbc:postgresql://localhost:5432/network","postgres","calculator7",new UserValidator());
        repoDb.findAll().forEach(System.out::println);
        Repository<Tuple<Long,Long>, Friendship> repoDbFr=new FriendshipDbRepository("jdbc:postgresql://localhost:5432/network","postgres","calculator7",new FriendshipValidator());
        repoDbFr.findAll().forEach(System.out::println);


        ServiceUser servUser=new ServiceUser(repoDb,repoDbFr);
        ServiceFriendship servFriendship=new ServiceFriendship(repoDb,repoDbFr);
        UI userInterface=new UI(servUser,servFriendship);
        userInterface.show();
    }
}
