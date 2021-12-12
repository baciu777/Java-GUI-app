package domain;

import java.time.LocalDateTime;
import java.util.List;

public class DtoMessage extends Entity<Long> {
    String name;
    LocalDateTime date;
    String message;
    Long replyIdMess;
    public DtoMessage(String name, String message,LocalDateTime date,Long reply) {
        this.name=name;
        this.date=date;
        this.message = message;
        this.replyIdMess=reply;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getReplyIdMess() {
        return replyIdMess;
    }

    public void setReplyIdMess(Long replyIdMess) {
        this.replyIdMess = replyIdMess;
    }
}
