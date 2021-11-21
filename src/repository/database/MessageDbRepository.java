package repository.database;

import domain.Friendship;
import domain.Message;
import domain.Tuple;
import domain.User;
import domain.validation.MessageValidator;
import domain.validation.Validator;
import repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
                String to1=resultSet.getString("tom");
                List<String> toId =new ArrayList<String>(Arrays.asList(to1.split(" ")));
                toId.remove(0);// lose the first element cuz it a space there from the stream reduce:(

                List<User> to=new ArrayList<>();
                List<Long> idList=toId.stream()
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
                idList.forEach(x->to.add(findOneUser(x)));
                String message=resultSet.getString("messagem");
                mess=new Message(from,to,message);
                mess.setId(id);
                mess.setDate(date);
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
                String to1=resultSet.getString("tom");
                List<String> toId =new ArrayList<String>(Arrays.asList(to1.split(" ")));
                toId.remove(0);// lose the first element cuz it a space there from the stream reduce:(
                List<User> to=new ArrayList<>();
                List<Long> idList=toId.stream()
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
                idList.forEach(x->to.add(findOneUser(x)));
                String message=resultSet.getString("messagem");
                mess=new Message(from,to,message);
                mess.setId(id);
                mess.setDate(date);
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
        String sql="insert into messages (fromm,tom,messagem,replym,datem) values(?,?,?,?,'"+entity.getDate()+"')";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            message=this.findOne(entity.getId());
            if(message!=null)
                return message;
            ps.setLong(1, entity.getFrom().getId());
            String ss=String.valueOf(entity.getTo()
                    .stream()
                    .map(x->x.getId().toString())
                            .reduce("",(x,y)->x+" "+y));


            ps.setString(2,ss);
            ps.setString(3,entity.getMessage());
            ps.setLong(4,entity.getReply());


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
}
