package domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Message extends Entity<Long> {
    private User from;
    private List<User> to=new ArrayList<>();;
    private String message;
    private Long reply;
    LocalDateTime date;

    public Message(User from, List<User> to, String message) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.reply=-1L;
    }

    public Message(User from, List<User> to, String message, Long reply) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.reply = reply;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public List<User> getTo() {
        return to;
    }
    public List<User> getToReply(User userFrom) {
        List<User> result=new ArrayList<>();
        for (User ur:this.to)
        {
            if(!Objects.equals(ur.getId(), userFrom.getId()))
                result.add(ur);
        }
        return result;
    }

    public void setTo(List<User> to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getReply() {
        return reply;
    }

    public void setReply(Long reply) {
        this.reply = reply;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDate() {
        return date;
    }

    @Override
    public String toString() {
        return from.getFirstName()+" "+from.getLastName()+":"+message;
    }
}
