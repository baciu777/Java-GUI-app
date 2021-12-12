package domain;

import java.time.LocalDateTime;

public class DtoFriendReq {
    String name;
    LocalDateTime date;
    String status;

    public DtoFriendReq(String name, LocalDateTime date, String status) {
        this.name = name;
        this.date = date;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
