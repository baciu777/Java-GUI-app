package domain.validation;

import domain.User;

/**
 * User validator where is verified the inputs of a potential user
 */
public class UserValidator implements  Validator<User>{

    @Override
    public void validate(User entity) throws ValidationException {
        String first=entity.getFirstName();
        String last= entity.getLastName();
        Long id= entity.getId();
        if(!first.matches("^.[a-z]{0,24}$"))
            throw new ValidationException("the first name must contain only small letters[25 max], except the first one ");
        if(!first.matches("^[A-Z].*"))
            throw new ValidationException("the first name must start with a big letter");
        if(!last.matches("^.[a-z]{0,24}$"))
            throw new ValidationException("the last name must contain only small letters[25 max], except the first one");
        if(!last.matches("^[A-Z].*"))
            throw new ValidationException("the last name must start with a big letter");




    }
}
