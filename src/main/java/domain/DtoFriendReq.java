package domain;

import java.time.LocalDateTime;

public class DtoFriendReq {
    String from;
    String to;
    LocalDateTime date;
    String status;

    public DtoFriendReq(String from,String to, LocalDateTime date, String status) {
        this.from=from;
        this.to=to;
        this.date = date;
        this.status = status;
    }


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
