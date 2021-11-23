package userinterface;

import domain.validation.ValidationException;
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

    /**
     * constructor
     * @param servMessage-serviceMessage
     * @param servUser-serviceUser
     */
    public UILogin(ServiceMessage servMessage, ServiceUser servUser) {
        this.servMessage = servMessage;
        this.servUser = servUser;

    }

    /**
     * get the log in id
     * @return id
     */
    public Long getLoginID() {
        return ID;
    }

    /**
     * set the id
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
            System.out.println("1-Send a message\n2-Send a reply\nx-Exit");
            cmd = scanner.nextLine();
            if (Objects.equals(cmd, "x"))
                break;
            meniuUser(cmd);


        }
    }

    /**
     * choose the option
     * @param cmd-string chosen
     */
    private void meniuUser(String cmd) {
        switch (cmd) {
            case "1":
                addMessage();
                break;
            case "2":
                addReply();
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
        System.out.println("to:");
        while (true) {
            System.out.println("Choose a user id\nX-Stop");
            cmd = scanner.nextLine();
            if (Objects.equals(cmd, "x"))
                break;
            try {

                long idd = Long.parseLong(cmd);
                servUser.findOne(idd);
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
        String cmd="";
        Long idReply=0L;
        System.out.println("give id of the message");
        cmd = scanner.nextLine();
            try {
                idReply = Long.parseLong(cmd);
                System.out.println("message");
                String mess = scanner.nextLine();;
                servMessage.saveReply(getLoginID(),mess,idReply);
            } catch (NumberFormatException e) {
                System.out.println(cmd + " is not a valid id");
            }


        }

    }
