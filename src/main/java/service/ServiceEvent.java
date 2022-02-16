package service;

import ChangeEvent.EventChangeEvent;
import ChangeEvent.FriendChangeEvent;
import domain.Entity;
import domain.Event;
import domain.Notification;
import domain.User;
import domain.validation.ValidationException;
import observer.Observable;
import observer.Observer;
import paging.PageR;
import paging.Pageable;
import paging.PageableImplementation;
import paging.PagingRepository;
import repository.Repository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ServiceEvent implements Observable<EventChangeEvent> {
    private PagingRepository<Long, Event> repoEvents;
    private Repository<Long, Notification> repoNotif;
    private int page = 0;
    private int size = 3;

    public ServiceEvent(PagingRepository<Long, Event> repoEvents, Repository<Long, Notification> repoNotif) {
        this.repoEvents = repoEvents;
        this.repoNotif = repoNotif;
    }

    public void save(String name, LocalDate date, Map<Long,Long> ids) {
        Event ev = new Event(name, date, ids);
        long id = 0L;
        for (Event ur : repoEvents.findAll()) {
            if (ur.getId() > id)
                id = ur.getId();

        }
        id++;

        ev.setId(id);

        Event save = repoEvents.save(ev);
        notifyObservers(new EventChangeEvent(ev));
        if (save != null)
            throw new ValidationException("id already used");


    }


    public void update(Long id, String name, LocalDate date,Map<Long,Long> ids) {
        Event ev = new Event(name, date, ids);
        ev.setId(id);


        Event save = repoEvents.update(ev);
        if (save != null)
            throw new ValidationException("this id does not exit");


    }


    public Entity delete(Long id) {

        Event deleted = repoEvents.delete(id);


        if (deleted == null)
            throw new ValidationException("id invalid");


        return deleted;

    }

    public Iterable<Event> printUs() {

        return repoEvents.findAll();
    }

    public Iterable<Notification> printUsNotif() {

        return repoNotif.findAll();
    }
    public Event findOne(Long nr) {
        if (repoEvents.findOne(nr) != null)
            return repoEvents.findOne(nr);
        else
            throw new ValidationException(" id invalid");
    }

    public Notification findOneNotification(Long nr) {
        if (repoEvents.findOne(nr) != null)
            return repoNotif.findOne(nr);
        else
            throw new ValidationException(" id invalid");
    }
    public Notification findOneNotificationIdName(Long iduser,String notif) {
        for(Notification nf :repoNotif.findAll())
        {
            if(Objects.equals(nf.getIduser(), iduser) && Objects.equals(nf.getNotif(), notif))
                return nf;
        }
        return null;
    }
    public void saveNotif(Long iduser,String name) {
        Notification nf=new Notification(name,iduser);
        long id = 0L;
        for (Notification ur : repoNotif.findAll()) {
            if (ur.getId() > id)
                id = ur.getId();

        }
        id++;

        nf.setId(id);

        Notification save=repoNotif.save(nf);
        if (save != null)
            throw new ValidationException("id already used");


    }


    public Set<Event> getEventsOnPage(int page) {
        this.page=page;
        Pageable pageable = new PageableImplementation(page, this.size);
        PageR<Event> studentPage =repoEvents.findAllPage(pageable);
        return studentPage.getContent().collect(Collectors.toSet());
    }

    private List<Observer<EventChangeEvent>> observers=new ArrayList<>();
    @Override
    public void addObserver(Observer<EventChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<EventChangeEvent> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers(EventChangeEvent t) {
        observers.stream().forEach(x->x.update(t));
    }

    private Pageable pageable;

    public void setPageSize(int size) {
        this.size = size;
    }
    public int getPageSize() {
        return size;
    }
    public void setPageNumber(int pageNumber) {
        this.page = pageNumber;
    }


    public Set<Event> getNextEvents() {

        this.page++;
        return getEventsOnPage(this.page);
    }

}
