package domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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
    private String username,password;
    private LocalDate birth=null;
    /**
     * list of all user friends
     */
    private List<User> friends = new ArrayList<>();
    /**
     * constructor
     * @param firstName oof the user
     * @param lastName of the user
     * @param birth
     */
    public User(String firstName, String lastName, String username, LocalDate birth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;

        this.birth = birth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(LocalDate birth) {
        this.birth = birth;
    }

    /**
     * getter function
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
     * @return the list of friends for the current user
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
     * @param friends new friends of the current user
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
    public String toString3()
    {
        return this.getId() +" " + this.getFirstName() +" "+ this.getLastName();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
