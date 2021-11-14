package domain;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Friendship class which extends an Entity
 */
public class Friendship extends Entity<Tuple<Long, Long>> {
    /**
     * LocalDateTime
     */
    LocalDateTime date;

    /**
     * default constructor for object Friendship
     */
    public Friendship() {
    }

    /**
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * set the date time when the friendship was created
     *
     * @param Date-LocalDateTime
     */
    public void setDate(LocalDateTime Date) {
        date = Date;
    }

    @Override
    public String toString() {
        return "Friendship{" + getId().getLeft() + " " + getId().getRight() + " " +
                "date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        Friendship that = (Friendship) o;

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;


        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }
}
