package repository.database;

import domain.User;
import domain.validation.Validator;
import repository.Repository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
                String usernameU = resultSet.getString("usernameu");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                LocalDate birth = LocalDate.parse(resultSet.getString("birth"),formatter);

                user = new User(firstName, lastName,usernameU,birth );
                user.setId(aLong);
                user.setFriends(this.findFr(aLong,connection));
                String passwordU = findtPass(usernameU,connection);
                user.setPassword(passwordU);

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
                String usernameU=resultSet.getString("usernameu" );
                String passwordU = findtPass(usernameU,connection);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                LocalDate birth = LocalDate.parse(resultSet.getString("birth"),formatter);
                User utilizator = new User(firstName, lastName,usernameU,birth);
                utilizator.setId(id);

                utilizator.setFriends(this.findFr(id,connection));
                utilizator.setPassword(passwordU);

                users.add(utilizator);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private String findtPass(String usern,Connection connection) {
        String passwordu = null;

        try (  PreparedStatement statement = connection.prepareStatement("select * \n" +
                     "from passwords p \n" +
                     "where (p.usernameu=?)")) {
            statement.setString(1, usern);
            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {

                    passwordu=resultSet.getString("passwordu");

                }
                return passwordu;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;

    }

    private List<User> findFr(Long id,Connection connection) {
        List<User> users = new ArrayList<>();
        try ( PreparedStatement statement = connection.prepareStatement("select id, first_name, last_name,usernameu, birth\n" +
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
                    String usernameU=resultSet.getString("usernameu" );
                    String passwordU = findtPass(usernameU,connection);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                    LocalDate birth = LocalDate.parse(resultSet.getString("birth"),formatter);
                    User utilizator = new User(firstName, lastName,usernameU,birth);
                    //pui set la password baciu???

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
        String sql = "insert into users (first_name, last_name,usernameu,birth) values (?, ?,?,?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)
        ) {

            ps.setString(1, entity.getFirstName());
            ps.setString(2, entity.getLastName());
            ps.setString(3, entity.getUsername());
            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("d/MM/yyyy");
            String text = entity.getBirth().format(formatters);
            ps.setString(4,  text);

            String sqlChat = "insert into passwords (usernameu,passwordu) values (?,?)";
 //



            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(entity.getPassword().getBytes());
            String stringHash = new String(messageDigest.digest());
            ps.executeUpdate();
            PreparedStatement psPass= connection.prepareStatement(sqlChat);


            psPass.setString(1, entity.getUsername());
            psPass.setString(2,stringHash);
            psPass.executeUpdate();


        } catch (SQLException | NoSuchAlgorithmException e) {
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
//UPDATE UL NU A FOST MODIFICAT
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
