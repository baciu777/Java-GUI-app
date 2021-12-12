package ObserverController;

import ChangeEvent.MessageChangeEvent;
import domain.DtoMessage;
import domain.Message;
import domain.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import observer.Observer;
import service.ServiceMessage;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ObserverMessage implements Observer<MessageChangeEvent> {
    ServiceMessage serviceMessage;
    ObservableList<DtoMessage> modelMessage;
    User user;

    @Override
    public void update(MessageChangeEvent messageChangeEvent) {
        initModelMessage();
    }

    public void setServiceModelMessage(ServiceMessage mess, ObservableList<DtoMessage> model,User userLogin) {
        serviceMessage = mess;
        serviceMessage.addObserver(this);
        modelMessage = model;
        user=userLogin;
        initModelMessage();
    }

    private void initModelMessage() {
        Iterable<Message> messages = serviceMessage.userMessages(user);
        List<DtoMessage> messageTaskList = StreamSupport.stream(messages.spliterator(), false)

                .map(x ->
                        {
                            if (x.getReply() == null)
                                return new DtoMessage(x.getFrom().getFirstName() +" "+ x.getFrom().getLastName(), x.getMessage(), x.getDate(), null);

                            else
                               return new DtoMessage(x.getFrom().getFirstName() +" "+ x.getFrom().getLastName(), x.getMessage(), x.getDate(), x.getReply().getId());
                        }

                )
                .collect(Collectors.toList());
        modelMessage.setAll(messageTaskList);

    }


}
