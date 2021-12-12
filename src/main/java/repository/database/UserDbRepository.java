package repository.database;

import domain.User;
import domain.validation.Validator;
import repository.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDbRepository implements Repository<Long, User> {
    private String url;
    private String username;
    private String password;
    private Validator<User> validator;

    public UserDbRepository(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public User findOne(Long aLong) {
        if (aLong == null)
            throw new IllegalArgumentException("Id must be not null");

        String sql = "SELECT * from users where users.id = ?";
        User user;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Math.toIntExact(aLong));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                user = new User(firstName, lastName);
                user.setId(aLong);
                user.setFriends(this.findFr(aLong));
                return user;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");

                User utilizator = new User(firstName, lastName);
                utilizator.setId(id);
                utilizator.setFriends(this.findFr(id));

                users.add(utilizator);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private List<User> findFr(Long id) {
        List<User> users = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select id, first_name, last_name, date\n" +
                     "from users u inner join friendships f on u.id = f.id1 or u.id=f.id2\n" +
                     "where (f.id1= ? or f.id2 = ? )and u.id!= ?"))
        {statement.setLong(1, id);
             statement.setLong(2, id);
             statement.setLong(3,id);
             try(ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Long idnew = resultSet.getLong("id");
                    String firstName = resultSet.getString("first_name");
                    String lastName = resultSet.getString("last_name");

                    User utilizator = new User(firstName, lastName);
                    utilizator.setId(idnew);


                    users.add(utilizator);
                }
            return users;
        }} catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return null;
}

    @Override
    public User save(User entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must be not null");
        validator.validate(entity);
        String sql = "insert into users (first_name, last_name ) values (?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User delete(Long aLong) {
        int row_count = 0;
        User user = null;
        String sql = "delete from users where id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            user = this.findOne(aLong);
            if (user == null)
                return null;

            ps.setLong(1, user.getId());
            //ps.setString(1, user.getFirstName());
            //ps.setString(2, user.getLastName());
            ps.executeUpdate();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return user;
    }

    @Override
    public User update(User entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must be not null");
        validator.validate(entity);
        String sql = "update users SET first_name=?,last_name=? where ID=?";
        int row_count = 0;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setLong(3, entity.getId());
            row_count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (row_count > 0)
            return null;
        return entity;
    }
}
