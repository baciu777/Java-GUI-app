package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * define a User that extends Entity
 * firstName-String
 * lastName-String
 * friends-List of Users
 */
public class User extends Entity<Long> {
    /**
     * First and last name of a user
     */
    private String firstName, lastName;
    /**
     * list of all user friends
     */
    private List<User> friends = new ArrayList<>();
    /**
     * constructor
     * @param firstName oof the user
     * @param lastName of the user
     */
    public User(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    /**
     * getter funtion
     * @return the first name of the user
     */
    public String getFirstName() {
        return firstName;
    }
    /**
     * getter function
     * @return the last name of the user
     */
    public String getLastName() {
        return lastName;
    }
    /**
     * getter function for user's friends
     * @return the list of friends for the curent user
     */
    public List<User> getFriends() {
        return friends;
    }
    /**
     * set the first name of an user
     * @param firstName the new first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    /**
     * set the last name of an user
     * @param lastName the new last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    /**
     * setter function
     * @param friends new friends of the curent user
     */
    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    /**
     * add a friend to the list
     * @param friend-User
     */
    public void addFriend(User friend) {
        this.friends.add(friend);
    }

    /**
     * delete a friend from the list
     * @param friend-User
     */
    public void deleteFriend(User friend) {
        this.friends.remove(friend);
    }

    @Override
    public String toString() {
        String s;
        s = "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", friends=";
        for (int i = 0; i < friends.size(); i++)
            s = s + "|"+friends.get(i).getFirstName() + " " + friends.get(i).getLastName();
        s = s + '}';
        return s;
    }
    public String toString2() {
        String s;
        s = "Friend: " +
                 firstName + " " +
                 lastName + " ";
        return s;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(friends, user.friends);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, friends);
    }
}
