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
        this.friendRequestsReceived.removeIf(x-> Objects.equals(fr.getFrom(), x.getFrom()) && Objects.equals(x.getTo(), fr.getTo()) && Objects.equals(fr.getStatus(), x.getStatus()));
    }
    public void addFriendPage(User f)
    {

        this.friends.add(f);
    }
    public void removeFrRequestSent(DtoFriendReq fr)
    {

        this.friendRequestsSent.removeIf(x-> Objects.equals(fr.getFrom(), x.getFrom()) && Objects.equals(x.getTo(), fr.getTo()) && Objects.equals(fr.getStatus(), x.getStatus()));

 /*
        for(DtoFriendReq x : this.friendRequestsSent)
        {
            if(Objects.equals(x.getFrom(), fr.getFrom()) && Objects.equals(x.getTo(), fr.getTo()))
            {
                this.friendRequestsSent.remove(x);
                break;
            }
        }
*/
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
    public void addRequestRec(DtoFriendReq frRSent)
    {
        friendRequestsReceived.add(frRSent);
    }
    public void delRequestRec(DtoFriendReq frRSent)
    {
        friendRequestsReceived.removeIf(frr->Objects.equals( frr,frRSent));
    }
    public void addMessage(Message mess)
    {
        messages.add(mess);
    }

    //DACA FACEAM OBSERVER IN PAGE NU ERA LA FEL DE EFICIENT, DEOARECE RELUAM TOT DIN BAZA DE DATE CAND SE
    //SCHIMBA CEVA, ASA DOAR ADAUG CATE UN PRIETEN/REQUEST/MESAJ
}
