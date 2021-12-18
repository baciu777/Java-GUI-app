package repository.database;

import domain.Friendship;
import domain.Message;
import domain.Tuple;
import domain.User;
import domain.validation.MessageValidator;
import domain.validation.Validator;
import repository.Repository;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MessageDbRepository implements Repository<Long, Message> {
    private String url;
    private String username;
    private String password;
    private Validator<Message> validator;

    public MessageDbRepository(String url, String username, String password, Validator<Message> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Message findOne(Long aLong) {
        if (aLong == null)
            throw new IllegalArgumentException("Id must be not null");

        String sql = "SELECT * from messages where messages.id = ?";
        Message mess;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Math.toIntExact(aLong));
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Long id = resultSet.getLong("id");
                LocalDateTime date = LocalDateTime.ofInstant(resultSet.getTimestamp("datem").toInstant(), ZoneOffset.ofHours(0));
                Long fromId =resultSet.getLong("fromm");
                User from=findOneUser(fromId);
                List<User> to=new ArrayList<>();
                List<Long> idList=findTo(id);
                idList.forEach(x->to.add(findOneUser(x)));
                String message=resultSet.getString("messagem");
                Long idreply=resultSet.getLong("replym");
                Message reply=this.findOne(idreply);
                mess=new Message(from,to,message);
                mess.setId(id);
                mess.setDate(date);
                mess.setReply(reply);
                return mess;

            }


        } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        return null;

    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> messages = new HashSet<>();
        Message mess=null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from messages order by datem");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                LocalDateTime date = LocalDateTime.ofInstant(resultSet.getTimestamp("datem").toInstant(), ZoneOffset.ofHours(0));
                Long fromId =resultSet.getLong("fromm");
                User from=findOneUser(fromId);
                List<User> to=new ArrayList<>();
                List<Long> idList=findTo(id);
                idList.forEach(x->to.add(findOneUser(x)));
                String message=resultSet.getString("messagem");
                Long idreply=resultSet.getLong("replym");
                Message reply=this.findOne(idreply);
                mess=new Message(from,to,message);
                mess.setId(id);
                mess.setDate(date);
                mess.setReply(reply);
                messages.add(mess);


            }
            return messages;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    @Override
    public Message save(Message entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must be not null");
        validator.validate(entity);
        Message message=null;
        String sql="insert into messages (fromm,messagem,replym,datem) values(?,?,?,'"+entity.getDate()+"')";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            message = this.findOne(entity.getId());
            if (message != null)
                return message;
            ps.setLong(1, entity.getFrom().getId());

            String sqlChat = "insert into chats (id1,tom) values (?,?)";
            PreparedStatement psChat = connection.prepareStatement(sqlChat);
            List<User> listTo = entity.getTo();
            for (User ur : listTo) {
                psChat.setLong(1, entity.getId());
                psChat.setLong(2,ur.getId());
                psChat.executeUpdate();
            }




            ps.setString(2,entity.getMessage());
            if(entity.getReply()!=null)
            ps.setLong(3,entity.getReply().getId());
            else
                ps.setLong(3,-1L);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Message delete(Long aLong) {
        return null;
    }

    @Override
    public Message update(Message entity) {
        return null;
    }


    /**
     * function that return the user with a specific id
     * @param aLong-id
     * @return one user
     * otherwise, throw exception
     */
    private User findOneUser(Long aLong) {
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
                String usernameU = resultSet.getString("username");
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                LocalDate birth = LocalDate.parse(resultSet.getString("birth"),formatter);
                user = new User(firstName, lastName,username,birth);
                user.setId(aLong);
                user.setFriends(this.findFr(aLong));
                return user;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * find the friends of one user
     * @param id-user id
     * @return list of users
     */
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
                    String usernameU = resultSet.getString("username");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                    LocalDate birth = LocalDate.parse(resultSet.getString("birth"),formatter);
                    User utilizator = new User(firstName, lastName,usernameU,birth);
                    utilizator.setId(idnew);


                    users.add(utilizator);
                }
                return users;
            }} catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return null;
    }
    private List<Long> findTo(Long id) {
        List<Long> toIds = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("select tom \n" +
                     "from chats c inner join messages m on c.id1 = m.id \n" +
                     "where (m.id=?)")) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    Long idNew = resultSet.getLong("tom");


                    toIds.add(idNew);
                }
                return toIds;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
