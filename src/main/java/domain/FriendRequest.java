package domain;



public class FriendRequest extends Friendship{
    private Status status;
    public FriendRequest()
    {

    }
    public String getStatus()
    {
        return status.toString();
    }
    public void setStatus(String s)
    {
        status = Status.valueOf(s);
    }

    public String toStringSent() {

        return "request from " + getId().getLeft() + " at " + date + " is " + getStatus();
    }
    public String toStringReceived()
    {
        return "request to " + getId().getRight() + " at " + date + " is " + getStatus();
    }
}
