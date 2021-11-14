package repository.database;

import domain.Friendship;
import domain.Tuple;
import domain.validation.Validator;
import repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

public class FriendshipDbRepository implements Repository<Tuple<Long, Long>, Friendship> {
    private String url;
    private String username;
    private String password;
    private Validator<Friendship> validator;

    public FriendshipDbRepository(String url, String username, String password, Validator<Friendship> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }

    @Override
    public Friendship findOne(Tuple<Long, Long> longLongTuple) {
        if (longLongTuple == null)
            throw new IllegalArgumentException("Id must be not null");

        String sql = "SELECT * from friendships where friendships.id1 = ? and friendships.id2 = ?";
        Friendship fr;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, longLongTuple.getLeft());
            statement.setLong(2, longLongTuple.getRight());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                LocalDateTime date = LocalDateTime.ofInstant(resultSet.getTimestamp("date").toInstant(), ZoneOffset.ofHours(0));

                fr = new Friendship();

                fr.setDate(date);
                fr.setId(longLongTuple);
                return fr;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
         sql = "SELECT * from friendships where friendships.id2 = ? and friendships.id1 = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, longLongTuple.getLeft());
            statement.setLong(2, longLongTuple.getRight());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {


                fr = new Friendship();
                LocalDateTime date = LocalDateTime.ofInstant(resultSet.getTimestamp("date").toInstant(), ZoneOffset.ofHours(0));

                fr.setDate(date);
                fr.setId(longLongTuple);
                return fr;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Friendship> findAll() {
        Set<Friendship> friendships = new HashSet<>();
        Friendship fr=null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                LocalDateTime date = LocalDateTime.ofInstant(resultSet.getTimestamp("date").toInstant(), ZoneOffset.ofHours(0));

                fr=new Friendship();
                Tuple<Long,Long> t=new Tuple<>(id1,id2);
                fr.setId(t);
                fr.setDate(date);
                friendships.add(fr);


            }

            return friendships;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friendships;
    }

    @Override
    public Friendship save(Friendship entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must be not null");
        validator.validate(entity);
        Friendship friendship=null;
        String sql="insert into friendships(id1,id2,date) values (?,?,'"+entity.getDate()+"')";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            friendship=this.findOne(entity.getId());
            if(friendship!=null)
                return friendship;
            ps.setLong(1, entity.getId().getLeft());
            ps.setLong(2, entity.getId().getRight());


            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Friendship delete(Tuple<Long, Long> longLongTuple) {
        int row_count = 0;
        Friendship friendship=null;
        String sql = "delete from friendships where (friendships.id1 = ? AND friendships.id2 = ?) or (friendships.id1 = ? AND friendships.id2 = ?) ";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            friendship=this.findOne(longLongTuple);
            if(friendship==null)
                return null;

            ps.setLong(1, friendship.getId().getLeft());
            ps.setLong(2,friendship.getId().getRight());
            ps.setLong(3, friendship.getId().getRight());
            ps.setLong(4,friendship.getId().getLeft());
            ps.executeUpdate();


        }  catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return friendship;
    }

    @Override
    public Friendship update(Friendship entity) {
        return null;
    }
}
