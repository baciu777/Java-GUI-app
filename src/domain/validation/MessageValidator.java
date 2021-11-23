package domain.validation;

import domain.Message;
import domain.User;

public class MessageValidator implements  Validator<Message>{

    @Override
    public void validate(Message entity) throws ValidationException {
        if(entity.getTo().size()==0)
            throw new ValidationException("choose at least one user for the message");

    }
}
