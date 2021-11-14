package domain.validation;

import domain.Friendship;

import java.util.Objects;

/**
 * Friendship validator where is verified the inputs of a potential friendship
 */
public class FriendshipValidator implements  Validator<Friendship>{


    @Override
    public void validate(Friendship entity) throws ValidationException {
        if(Objects.equals(entity.getId().getLeft(), entity.getId().getRight()))
            throw new ValidationException("the ids must be different");

    }
}
