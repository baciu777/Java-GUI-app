package repository.database;

import domain.FriendRequest;
import domain.Friendship;
import domain.Tuple;
import domain.validation.Validator;
import repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Set;

public class FriendRequestDbRepository implements Repository<Tuple<Long, Long>, FriendRequest> {
    private String url;
    private String username;
    private String password;


    public FriendRequestDbRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;

    }

    @Override
    public FriendRequest findOne(Tuple<Long, Long> longLongTuple) {
        if (longLongTuple == null)
            throw new IllegalArgumentException("Id must be not null");

        String sql = "SELECT * from friendrequests where friendrequests.id1 = ? and friendrequests.id2 = ?";
        FriendRequest fr;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, longLongTuple.getLeft());
            statement.setLong(2, longLongTuple.getRight());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                LocalDateTime date = LocalDateTime.ofInstant(resultSet.getTimestamp("date").toInstant(), ZoneOffset.ofHours(0));
                String st=resultSet.getString("status");
                fr = new FriendRequest();

                fr.setDate(date);
                fr.setId(longLongTuple);
                fr.setStatus(st);
                return fr;

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    @Override
    public Iterable<FriendRequest> findAll() {
        Set<FriendRequest> friend_requests = new HashSet<>();
        FriendRequest fr=null;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from friendrequests");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id1 = resultSet.getLong("id1");
                Long id2 = resultSet.getLong("id2");
                LocalDateTime date = LocalDateTime.ofInstant(resultSet.getTimestamp("date").toInstant(), ZoneOffset.ofHours(0));
                String st=resultSet.getString("status");
                fr=new FriendRequest();
                Tuple<Long,Long> t=new Tuple<>(id1,id2);
                fr.setId(t);
                fr.setDate(date);
                fr.setStatus(st);
                friend_requests.add(fr);


            }

            return friend_requests;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friend_requests;
    }


    @Override
    public FriendRequest save(FriendRequest entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must be not null");
        FriendRequest friend_request=null;
        String sql="insert into friendrequests(id1,id2,date,status) values (?,?,'"+entity.getDate()+"',?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)
        ) {
            friend_request=this.findOne(entity.getId());
            if(friend_request!=null)
                return friend_request;
            ps.setLong(1, entity.getId().getLeft());
            ps.setLong(2, entity.getId().getRight());
            ps.setString(3, entity.getStatus());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public FriendRequest delete(Tuple<Long, Long> longLongTuple) {
        int row_count = 0;
        FriendRequest friend_request=null;
        String sql = "delete from friendrequests where (friendrequests.id1 = ? AND friendrequests.id2 = ?)  ";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            friend_request=this.findOne(longLongTuple);
            if(friend_request==null)
                return null;

            ps.setLong(1, friend_request.getId().getLeft());
            ps.setLong(2,friend_request.getId().getRight());
            ps.executeUpdate();


        }  catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return friend_request;
    }

    @Override
    public FriendRequest update(FriendRequest entity) {
        if (entity == null)
            throw new IllegalArgumentException("Entity must be not null");
        String sql = "update friendrequests SET status=? where id1=? and id2 =?";
        int row_count = 0;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, entity.getStatus());
            ps.setLong(2, entity.getId().getLeft());
            ps.setLong(3, entity.getId().getRight());
            row_count = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (row_count > 0)
            return null;
        return entity;
    }


}
