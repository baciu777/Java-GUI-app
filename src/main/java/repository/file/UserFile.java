package repository.file;

import domain.User;
import domain.validation.Validator;

import java.util.List;

/**
 * Class for User Repository
 */
public class UserFile extends AbstractFileRepository<Long, User> {
    /**
     * constructor
     * @param fileName string
     * @param validator validator type user
     */
    public UserFile(String fileName, Validator<User> validator) {
        super(fileName, validator);
    }

    /**
     * method for extracting an entity
     * @param attributes list of strings
     * @return a user
     */
    @Override
    protected User extractEntity(List<String> attributes) {
        User user = new User(attributes.get(1), attributes.get(2));
        user.setId(Long.parseLong(attributes.get(0)));
        return user;
    }

    /**
     * methof for creating an entity as string
     * @param entity-generic type
     * @return the string
     */
    @Override
    protected String createEntityAsString(User entity) {
        return entity.getId() + ";" + entity.getFirstName() + ";" + entity.getLastName();

    }
}
