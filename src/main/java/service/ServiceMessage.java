package service;

import ChangeEvent.ChangeEventType;
import ChangeEvent.Event;

import ChangeEvent.MessageTaskChangeEvent;
import domain.Message;
import domain.Page;
import domain.User;
import domain.validation.ValidationException;
import observer.Observable;
import observer.Observer;
import repository.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service of Message
 * repoMessage-Message Repository
 * repoUser-User Repository
 */
public class ServiceMessage implements Observable<MessageTaskChangeEvent> {
    private Repository<Long, Message> repoMessage;
    private Repository<Long, User> repoUser;
    private List<Observer<MessageTaskChangeEvent>> observers=new ArrayList<>();

    /**
     * constructor
     * @param repoMessage Repository Messages
     * @param repoUser Repository Users
     */
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
        Message mess=new Message( from,  to,  message,null);
        mess.setDate(LocalDateTime.now());
        long id = 0L;


        //mess.setId(id);

        Message save =repoMessage.save(mess);
        if (save != null)
            throw new ValidationException("id already used");
        notifyObservers(new MessageTaskChangeEvent( mess));

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
        Message replyMess=repoMessage.findOne(reply);
        Message mess=new Message( from,to,message,replyMess);
        mess.setDate(LocalDateTime.now());
        long id = 0L;


        Message save =repoMessage.save(mess);

        if (save != null)
            throw new ValidationException("id already used");
        notifyObservers(new MessageTaskChangeEvent( mess));



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
        listTo.add(oldFrom);//add the sender of the old message in listTo
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
                return userFromNew;}//return only if the new id is in the list of receiver of the old message
        throw new ValidationException("you cant reply to a message from a conversation you dont belong") ;


    }

    /**
     * show a private conversation between 2 users
     * @param id1-id of user 1
     * @param id2-id of user 2
     * @return List<Messages>
     */
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

    public List<Message> groupChat(List<Message> messagesUser,List<Long> ids)
    {
        System.out.println("aici");
        System.out.println(messagesUser.get(messagesUser.size()-1).getId());
        List<Message> result=messagesUser.stream()
            .filter(m-> (ids.contains(m.getFrom().getId())))
            .filter(m-> m.getTo().size()==ids.size()-1)
            .filter(m-> (m.getTo().stream().allMatch(x->ids.contains(x.getId()))))
                .sorted(Comparator.comparing(Message::getDate))
                .collect(Collectors.toList());
        return result;


    }
    public List<Message> userMessages(Page user)
    {List<Message> listOfMess=new ArrayList<>();
        for(Message ms:this.findAll())
        {
            System.out.println(ms.getFrom());
            if(Objects.equals(ms.getFrom().getId(), user.getId()))
            {
                listOfMess.add(ms);
            }
            else
                for(User ur:ms.getTo())
                {
                    if(Objects.equals(user.getId(), ur.getId()))
                    {listOfMess.add(ms);
                    break;}
                }
        }
        return  listOfMess;
    }



    public Message getLastMessSaved()
    {
        List<Message> listM=new ArrayList<>();
        return StreamSupport.stream(repoMessage.findAll().spliterator(),false)
                .sorted(new Comparator<Message>() {
                    @Override
                    public int compare(Message o1, Message o2) {
                        return o2.getDate().compareTo(o1.getDate());
                    }
                })
                        .toList().get(0);


    }

    public Iterable<Message> findAll()
    {
        return repoMessage.findAll();
    }


    @Override
    public void addObserver(Observer<MessageTaskChangeEvent> e) {


        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<MessageTaskChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(MessageTaskChangeEvent t) {
        observers.stream().forEach(x->x.update(t));
    }
}
