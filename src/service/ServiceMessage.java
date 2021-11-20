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

public class ServiceMessage {
    private Repository<Long, Message> repoMessage;
    private Repository<Long, User> repoUser;


    public ServiceMessage(Repository<Long, Message> repoMessage,Repository<Long, User> repoUser) {
        this.repoMessage = repoMessage;
        this.repoUser=repoUser;
    }
    public void save( Long fromId, List<Long> toIds, String message,Long reply) {
        User from=repoUser.findOne(fromId);
        List<User> to=new ArrayList<>();
        toIds.forEach(x->to.add(repoUser.findOne(x)));
        Message mess=new Message( from,  to,  message,reply);
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
    public Message findOne(Long nr) {
        if(repoMessage.findOne(nr) != null)
            return repoMessage.findOne(nr);
        else
            throw new ValidationException("message id invalid");
    }
    public List<User> findTo(Long idMessageOld,Long idMessageNew)
    {
        Message messOld=findOne(idMessageOld);
        List<User> listTo=messOld.getToReply(repoUser.findOne(idMessageNew));
        User oldFrom=messOld.getFrom();
        listTo.add(oldFrom);
        return listTo;
    }
    public User checkMessageReply(Long idMessageOld,Long idMessageNew)
    {
        Message mess=findOne(idMessageOld);
        User userFromNew=repoUser.findOne(idMessageNew);
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
