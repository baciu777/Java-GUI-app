package observer;


import ChangeEvent.Event;

public interface Observer<E extends Event> {
    void update(E e);
}