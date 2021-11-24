package userinterface;

import domain.FriendRequest;
import domain.validation.ValidationException;
import service.ServiceFriendshipRequest;
import service.ServiceMessage;
import service.ServiceUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * UI for the login
 * ID- the ID of the user logged in
 * servMessage - service of messages
 * servUser - service of users
 */
public class UILogin {
    private Long ID;
    ServiceMessage servMessage;
    ServiceUser servUser;
    ServiceFriendshipRequest servRequest;

    /**
     * constructor
     *
     * @param servMessage-serviceMessage
     * @param servUser-serviceUser
     */
    public UILogin(ServiceMessage servMessage, ServiceUser servUser, ServiceFriendshipRequest servRequest) {
        this.servMessage = servMessage;
        this.servUser = servUser;
        this.servRequest = servRequest;
    }

    /**
     * get the log in id
     *
     * @return id
     */
    public Long getLoginID() {
        return ID;
    }

    /**
     * set the id
     *
     * @param ID long
     */
    public void setID(Long ID) {
        this.ID = ID;
    }

    /**
     * start the ui+ menu
     */
    public void showUser() {
        String cmd = "";
        int nr = 0;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("-----------------------------MENU------------------");
            System.out.println("1-Send a message\n2-Send a reply" +
                    "\n3-Send friend request\n4-Accept friend request" +
                    "\n5-Reject friend request\nx-Exit");
            cmd = scanner.nextLine();
            if (Objects.equals(cmd, "x"))
                break;
            meniuUser(cmd);


        }
    }

    /**
     * choose the option
     *
     * @param cmd-string chosen
     */
    private void meniuUser(String cmd) {
        switch (cmd) {
            case "1":
                addMessage();
                break;
            case "2":
                addReply();
            case "3":
                addRequest();
                break;
            case "4":
                acceptRequest();
                break;
            case "5":
                rejectRequest();
                break;
            default:
                System.out.println("wrong command");


        }
    }

    /**
     * read the data and try to add message
     */
    private void addMessage() {
        Scanner scanner = new Scanner(System.in);


        List<Long> to = new ArrayList<>();
        System.out.println("message");
        String mess = scanner.nextLine();
        String cmd = "";
        List<Long> toList=new ArrayList<>();
        System.out.println("to:");
        while (true) {
            System.out.println("Choose a user id\nX-Stop");
            cmd = scanner.nextLine();
            if (Objects.equals(cmd, "x"))
                break;
            try {

                long idd = Long.parseLong(cmd);
                servUser.findOne(idd);
                if(toList.contains(idd))
                    throw new ValidationException("this id was chosen");
                toList.add(idd);
                if (getLoginID() == idd)
                    throw new ValidationException("you cant send a message to yourself");
                to.add(idd);

            } catch (NumberFormatException e) {
                System.out.println(cmd + " is not a valid id");
            } catch (ValidationException e) {
                System.out.println(e.getMessage());
            }
        }

        try {
            servMessage.save(getLoginID(), to, mess);
            System.out.println("Message saved");

        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * read the data and try to save a reply message
     */
    private void addReply() {
        Scanner scanner = new Scanner(System.in);
        String cmd = "";
        Long idReply = 0L;
        System.out.println("give id of the message");
        cmd = scanner.nextLine();
        try {
            idReply = Long.parseLong(cmd);
            System.out.println("message");
            String mess = scanner.nextLine();
            ;
            servMessage.saveReply(getLoginID(), mess, idReply);
        } catch (NumberFormatException e) {
            System.out.println(cmd + " is not a valid id");
        }


    }

    private void addRequest() {
        try {
            List<FriendRequest> req = (List<FriendRequest>) servRequest.findAllFrom(servRequest.findAll(), this.ID);
            if (req != null)
                req.forEach(x -> System.out.println(x.toStringReceived()));
            Scanner scanner = new Scanner(System.in);
            Long id;
            System.out.println("send request to(user id):");

            id = Long.parseLong(scanner.nextLine());
            List<FriendRequest> req2 =
                    (List<FriendRequest>) servRequest.findAllFrom(servRequest.findAllTo(servRequest.findWithStatus(servRequest.findAll(), "PENDING"), this.ID), id);
            boolean executeAdd = true;
            if (req2.size()!=0) {
                System.out.println("there a request from this id. Do you want to accept it?");
                System.out.println("1-Yes");
                System.out.println("2-No");
                Long answer = Long.parseLong(scanner.nextLine());
                if (answer == 2)
                    executeAdd = false;
            }
            if (executeAdd)
                servRequest.addFriend(this.ID, id);
            System.out.println("Request sent");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void acceptRequest() {
        try {
            List<FriendRequest> req = (List<FriendRequest>) servRequest.findAllTo(servRequest.findWithStatus(servRequest.findAll(), "PENDING"), this.ID);
            req.forEach(x -> System.out.println(x.toStringSent()));
            Scanner scanner = new Scanner(System.in);
            Long id;
            System.out.println("accept request from(user id):");

            id = Long.parseLong(scanner.nextLine());
            req = (List<FriendRequest>) servRequest.findAllFrom(req, id);
            boolean executeAdd = true;
            if (req.isEmpty()) {
                System.out.println("there is no request from this id. Do you want to send one?");
                System.out.println("1-Yes");
                System.out.println("2-No");
                Long answer = Long.parseLong(scanner.nextLine());
                if (answer == 2)
                    executeAdd = false;
            }
            if (executeAdd)
                servRequest.addFriend(this.ID, id);
            System.out.println("Request accepted");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void rejectRequest() {
        try {
            List<FriendRequest> req = (List<FriendRequest>) servRequest.findAllTo(servRequest.findWithStatus(servRequest.findAll(), "PENDING"), this.ID);
            req.forEach(x -> System.out.println(x.toStringSent()));
            Scanner scanner = new Scanner(System.in);
            Long id;
            System.out.println("reject request from(user id):");

            id = Long.parseLong(scanner.nextLine());
            servRequest.rejectRequest(this.ID, id);
            System.out.println("Request rejected");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
