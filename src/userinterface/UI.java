package userinterface;

import domain.Entity;
import domain.Message;
import domain.validation.ValidationException;
import graph.Network;
import service.ServiceFriendship;
import service.ServiceMessage;
import service.ServiceUser;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * class UI for the user interface of the project
 * servUser-Service
 */
public class UI {
    ServiceUser servUser;
    ServiceFriendship servFriendship;
    ServiceMessage servMessage;
    UILogin uiLogin;
    /**
     * constructor for UI
     *  @param servUser       the service for the users
     * @param servFriendship the service for the friendships
     * @param servMessage
     */
    public UI(ServiceUser servUser, ServiceFriendship servFriendship, ServiceMessage servMessage) {
        this.servUser = servUser;
        this.servFriendship = servFriendship;
        this.servMessage = servMessage;
    }

    /**
     * show the menu and keep track of the command
     */
    public void show() {
        String cmd = "";
        int nr = 0;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("-----------------------------MENU------------------");
            System.out.println("1-Add user\n2-Update user\n3-Delete user\n4-Add a friendship\n" +
                    "5-Delete a friendship\n6-Print the number of communities\n" +
                    "7-Biggest community\n8-Print users\n9-Print friendships\n10-Show the friendships of a user\n11-Login\n12-Print private chat\nx-Exit");
            cmd = scanner.nextLine();
            if (Objects.equals(cmd, "x"))
                break;
            meniu(cmd);


        }
    }


    /**
     * select the command chosen
     *
     * @param cmd-String
     */
    public void meniu(String cmd) {
        switch (cmd) {
            case "1":
                adduser();
                break;
            case "2":
                updateuser();
                break;
            case "3":
                deleteuser();
                break;
            case "4":
                addfriendship();
                break;
            case "5":
                deletefriendship();
                break;
            case "6":
                communities();
                break;
            case "7":
                biggest_community();
                break;
            case "8":
                printUsers();
                break;
            case "9":
                printFriendships();
                break;
            case "10":
                friendsUser();
                break;
            case "11":
                login();
                break;
            case "12":
                privateChat();
                break;
            default:
                System.out.println("wrong command");


        }
    }

    private void privateChat() {

        Scanner scanner = new Scanner(System.in);
        String idd="";

        try
        {
            System.out.println("id first user:");
             idd = scanner.nextLine();
            long id1=Long.parseLong(idd);
            servUser.userexist(id1);
            System.out.println("id second user:");
            idd = scanner.nextLine();
            long id2=Long.parseLong(idd);
            servUser.userexist(id2);
            List<Message> conversation=servMessage.showPrivateChat(id1,id2);
            conversation.forEach(System.out::println);

        }
        catch (NumberFormatException e)
        {
            System.out.println(idd+" is not a valid id");
        }
        catch (ValidationException e) {
            System.out.println(e.getMessage());
        }


    }


    private void login() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("id:");
        String id = scanner.nextLine();
        try
        {
            long idd=Long.parseLong(id);

            servUser.userexist(idd);
            uiLogin=new UILogin(servMessage,servUser);//start a new ui ,yaay
            uiLogin.setID(idd);
            uiLogin.showUser();

        }
        catch (NumberFormatException e)
        {
            System.out.println(id+" is not a valid id");
        }
        catch (ValidationException e) {
            System.out.println(e.getMessage());
        }




    }

    /**
     * print all the Friendships
     */
    private void printFriendships() {
        servFriendship.printFr().forEach(x -> System.out.println(x.toString()));
    }

    /**
     * print all the Users
     */
    private void printUsers() {
        servUser.printUs().forEach(x -> System.out.println(x.toString()));
    }

    /**
     * create a Network and print the number of connected Components
     */
    private void communities() {
        Network nw = new Network(Math.toIntExact(servUser.get_size()));
        nw.addUsers(servUser.printUs());
        nw.addFriendships(servFriendship.printFr());

        System.out.println("number of communities " + nw.connectedComponents());

    }

    /**
     * create a Network and print the biggest Component
     */
    private void biggest_community() {

        Network nw = new Network(Math.toIntExact(servUser.get_size()));

        nw.addUsers(servUser.printUs());
        nw.addFriendships(servFriendship.printFr());
        List<Long> list = nw.biggestComponent();
        try {

            list.forEach(x -> System.out.println(servUser.findOneUser(x)));
        } catch (ValidationException e) {
            System.out.println("there is no community");
        }

    }

    /**
     * read two ids and delete friendship if it s valid
     * otherwise, catch the exception
     */
    private void deletefriendship() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("id1:");
        String id = scanner.nextLine();
        Long nr1 = 0L;
        Long nr2 = 0L;
        try {
            nr1 = Long.parseLong(id);
            System.out.println("id2:");
            id = scanner.nextLine();
            nr2 = Long.parseLong(id);
            servFriendship.deleteFriend(nr1, nr2);
            System.out.println("Friendship deleted");
        } catch (NumberFormatException e) {
            System.out.println("the id must be an integer number");
        }
         catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * read two ids and add friendship if it s valid
     * otherwise, catch the exception
     */
    private void addfriendship() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("id1:");
        String id = scanner.nextLine();
        Long nr1 = 0L;
        Long nr2 = 0L;
        try {
            nr1 = Long.parseLong(id);
            System.out.println("id2:");
            id = scanner.nextLine();
            nr2 = Long.parseLong(id);
            servFriendship.addFriend(nr1, nr2);
            System.out.println("Friendship added");

        } catch (NumberFormatException e) {
            System.out.println("the id must be an integer number");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * read two strings and if it s valid save the new user
     * otherwise, catch the exception
     */
    private void adduser() {


        Scanner scanner = new Scanner(System.in);

        System.out.println("first name:");
        String first = scanner.nextLine();
        System.out.println("last name:");
        String last = scanner.nextLine();
        try {
            servUser.save(first, last);
            System.out.println("User saved");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * read one id and two strings and if it s valid update the user
     * otherwise, catch the exception
     */
    private void updateuser() {
        long nr;
        nr = 0L;
        Scanner scanner = new Scanner(System.in);
        System.out.println("id:");
        String id = scanner.nextLine();
        try {
            nr = Long.parseLong(id);
            System.out.println("first name:");
            String first = scanner.nextLine();
            System.out.println("last name:");
            String last = scanner.nextLine();
            servUser.update(nr, first, last);
            System.out.println("User updated");

        } catch (NumberFormatException e) {
            System.out.println("the id must be an integer number");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * read one id and if it s valid delete the user
     * otherwise, catch the exception
     */
    private void deleteuser() {
        long nr;
        nr = 0L;
        Scanner scanner = new Scanner(System.in);
        System.out.println("id:");
        String id = scanner.nextLine();
        try {
            nr = Long.parseLong(id);
            Entity deletedUser = servUser.delete(nr);
            System.out.println(deletedUser.toString());
        } catch (NumberFormatException e) {
            System.out.println("the id must be an integer number");
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * print all the friendships of a given user
     */
    private void friendsUser(){
        Long nr;
        Scanner scanner = new Scanner(System.in);
        System.out.println("id user:");
        String id = scanner.nextLine();
        try {
            nr = Long.parseLong(id);
            System.out.println(servUser.findOne(nr).toString() + "\n Friends: ");
            servUser.getFriends(nr).forEach(System.out::println);
        }
        catch (NumberFormatException e) {
            System.out.println("Id must be an integer number");
        }
        catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

}
