package domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Page extends User{


    List<Message> messages;
    List<DtoFriendReq> friendRequestsReceived;
    List<DtoFriendReq> friendRequestsSent;
    List<User> friends;
    /**
     * constructor
     *
     * @param firstName oof the user
     * @param lastName  of the user
     * @param username
     * @param birth
     */
    public Page(String firstName, String lastName, String username, LocalDate birth) {
        super(firstName, lastName, username, birth);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<DtoFriendReq> getFriendRequestsReceived() {
        return friendRequestsReceived;
    }

    public void setFriendRequestsReceived(List<DtoFriendReq> friendRequestsReceived) {
        this.friendRequestsReceived = friendRequestsReceived;
    }

    public List<DtoFriendReq> getFriendRequestsSent() {
        return friendRequestsSent;
    }

    public void setFriendRequestsSent(List<DtoFriendReq> friendRequestsSent) {
        this.friendRequestsSent = friendRequestsSent;
    }
    public void removeFrRequestRec(DtoFriendReq fr)
    {
        this.friendRequestsReceived.remove(fr);
    }
    public void removeFrRequestSent(DtoFriendReq fr)
    {
        this.friendRequestsSent.remove(fr);
    }

    @Override
    public List<User> getFriends() {
        return friends;
    }

    @Override
    public void setFriends(List<User> friends) {
        this.friends = friends;
    }


    public void removeFriend(User fr)
    {
        this.friends.removeIf(frr -> Objects.equals(frr.getId(), fr.getId()));
    }
    public void addRequestSent(DtoFriendReq frRSent)
    {
        friendRequestsSent.add(frRSent);
    }
}
