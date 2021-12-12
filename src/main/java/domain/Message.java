package domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Message class that extends Entity<Long>
 * from - User
 * to - List of users
 * message - String
 * reply - Message
 * date- LocalDateTime
 */
public class Message extends Entity<Long> {
    private User from;
    private List<User> to=new ArrayList<>();;
    private String message;
    private Message reply;
    LocalDateTime date;

    /**
     * first constructor
     * @param from - User
     * @param to - List</User>
     * @param message - String
     */
    public Message(User from, List<User> to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.reply=null;
    }

    /**
     * second constructor
     * @param from - User
     * @param to - List</User>
     * @param message - String
     * @param reply - Message
     */
    public Message(User from, List<User> to, String message, Message reply) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.reply = reply;
    }

    /**
     * get From
     * @return User
     */
    public User getFrom() {
        return from;
    }

    /**
     * set From
     * @param from - User
     */
    public void setFrom(User from) {
        this.from = from;
    }

    /**
     * get to
     * @return List of users
     */
    public List<User> getTo() {
        return to;
    }

    /**
     * get to for a reply message
     * @param userFrom - User
     * @return List of users
     */
    public List<User> getToReply(User userFrom) {
        List<User> result=new ArrayList<>();
        for (User ur:this.to)
        {
            if(!Objects.equals(ur.getId(), userFrom.getId()))
                result.add(ur);
        }
        return result;
    }

    /**
     * set to
     * @param to List of users
     */
    public void setTo(List<User> to) {
        this.to = to;
    }

    /**
     * get message
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     * set message
     * @param message Stirng
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * get reply
     * @return Message
     */
    public Message getReply() {
        return reply;
    }

    /**
     * set Reply
     * @param reply Message
     */
    public void setReply(Message reply) {
        this.reply = reply;
    }

    /**
     * set Date
     * @param date LocalDateTime
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    /**
     * get Date
     * @return LocalDateTime
     */
    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        if(getReply()==null)
        return from.getFirstName()+" "+from.getLastName()+":"+message;
        else
            return from.getFirstName()+" "+from.getLastName()+" (reply to: "+reply.getMessage()+") with: "+message;
    }
}
