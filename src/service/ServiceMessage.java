package service;

import domain.Message;
import domain.User;
import domain.validation.ValidationException;
import org.w3c.dom.ls.LSOutput;
import repository.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service of Message
 * repoMessage-Message Repository
 * repoUser-User Repository
 */
public class ServiceMessage {
    private Repository<Long, Message> repoMessage;
    private Repository<Long, User> repoUser;


    public ServiceMessage(Repository<Long, Message> repoMessage,Repository<Long, User> repoUser) {
        this.repoMessage = repoMessage;
        this.repoUser=repoUser;
    }

    /**
     * save one message
     * @param fromId-id of the user that sends the message
     * @param toIds-list of user's ids
     * @param message-string
     */
    public void save( Long fromId, List<Long> toIds, String message) {
        User from=repoUser.findOne(fromId);
        List<User> to=new ArrayList<>();
        toIds.forEach(x->to.add(repoUser.findOne(x)));
        Message mess=new Message( from,  to,  message,-1L);
        mess.setDate(LocalDateTime.now());
        long id = 0L;
        for (Message ms:  repoMessage.findAll()) {
            if (ms.getId() > id)
                id = ms.getId();

        }
        id++;

        mess.setId(id);

        Message save =repoMessage.save(mess);
        if (save != null)
            throw new ValidationException("id already used");


    }

    /**
     * save a reply of a message
     * @param fromId-the user id that sends the reply
     * @param message-string
     * @param reply-the id of the old message
     */
    public void saveReply( Long fromId, String message,Long reply) {
        User from=repoUser.findOne(fromId);
        List<User> to=new ArrayList<>();
        to =findTo(reply,fromId);
        checkMessageReply(reply,fromId);
        Message mess=new Message( from,to,message,reply);
        mess.setDate(LocalDateTime.now());
        long id = 0L;
        for (Message ms:  repoMessage.findAll()) {
            if (ms.getId() > id)
                id = ms.getId();

        }
        id++;

        mess.setId(id);

        Message save =repoMessage.save(mess);
        if (save != null)
            throw new ValidationException("id already used");


    }

    /**
     * find one message
     * @param nr-id of the message
     * @return the message if exists
     * otherwise, throw exception
     */
    public Message findOne(Long nr) {
        if(repoMessage.findOne(nr) != null)
            return repoMessage.findOne(nr);
        else
            throw new ValidationException("message id invalid");
    }

    /**
     * find the receivers of a reply
     * @param idMessageOld-id of old message
     * @param idMessageNew-id of new message
     * @returnthe list of users
     */
    public List<User> findTo(Long idMessageOld,Long idMessageNew)
    {
        Message messOld=findOne(idMessageOld);
        List<User> listTo=messOld.getToReply(repoUser.findOne(idMessageNew));
        User oldFrom=messOld.getFrom();
        listTo.add(oldFrom);
        return listTo;
    }

    /**
     * check if the old message was sent to the user that wants to send a reply
     * @param idMessageOld-id of old message
     * @param idUserNew-id of the user that wants to send a reply
     * @returnthe user if is founded
     * throw exception otherwise
     */
    public User checkMessageReply(Long idMessageOld,Long idUserNew)
    {
        Message mess=findOne(idMessageOld);
        User userFromNew=repoUser.findOne(idUserNew);
        if(Objects.equals(userFromNew.getId(), mess.getFrom().getId()))
            throw new ValidationException("you cant reply to yourself dude") ;

        for(User ur:mess.getTo())
        {
            if(Objects.equals(userFromNew.getId(), ur.getId()))
                return userFromNew;}
        throw new ValidationException("you cant reply to a message from a conversation you dont belong") ;


    }


    public List<Message> showPrivateChat(Long id1,Long id2) {
        List<Message> conversation=new ArrayList<>();
        User user1=repoUser.findOne(id1);
        User user2=repoUser.findOne(id2);
        List<Message> messages=new ArrayList<>();
        repoMessage.findAll().forEach(messages ::add);
        List<Message> sortedMessages=messages
                .stream()
                .sorted((x,y)->x.getId().compareTo(y.getId()))
                .collect(Collectors.toList());

        for(Message mess:sortedMessages)
        {
            if((Objects.equals(id1, mess.getFrom().getId()) && mess.getTo().contains(user2) && mess.getTo().size()==1)
            || (Objects.equals(id2, mess.getFrom().getId()) && mess.getTo().contains(user1)) && mess.getTo().size()==1)
                conversation.add(mess);

        }
        return conversation;
    }
}
