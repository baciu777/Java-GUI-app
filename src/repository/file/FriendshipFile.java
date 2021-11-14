package repository.file;


import domain.Friendship;
import domain.Tuple;
import domain.validation.Validator;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Friendship Repository
 */
public class FriendshipFile extends AbstractFileRepository<Tuple<Long,Long>,Friendship>{

    /**
     * constructor
     * @param fileName string
     * @param validator validator type friendship
     */
    public FriendshipFile(String fileName, Validator<Friendship> validator) {
        super(fileName, validator);
    }

    /**
     * method for extracting an entity
     * @param attributes list of strings
     * @return a user
     */
    @Override
    protected Friendship extractEntity(List<String> attributes) {
        long id1=Long.parseLong(attributes.get(0));
        long id2=Long.parseLong(attributes.get(1));
        LocalDateTime date=LocalDateTime.parse(attributes.get(2));
        Friendship friendship=new Friendship();

        Tuple t=new Tuple(id1,id2);
        friendship.setId(t);
        friendship.setDate(date);
        return  friendship;
    }
    /**
     * methof for creating an entity as string
     * @param entity-generic type
     * @return the string
     */
    @Override
    protected String createEntityAsString(Friendship entity) {
        return entity.getId().getLeft()+";"+entity.getId().getRight()+";"+entity.getDate();
    }
}
