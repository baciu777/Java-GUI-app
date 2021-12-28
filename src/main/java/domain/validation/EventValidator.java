package domain.validation;

import domain.Event;
import domain.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;

public class EventValidator implements  Validator<Event>{

    @Override
    public void validate(Event entity) throws ValidationException {

        if(entity.getName()=="")
            throw new ValidationException("the name must not be null ");

        if(entity.getDate()==null)
            throw new ValidationException("the date should not be null");
        if(entity.getDate().isBefore(ChronoLocalDate.from(LocalDateTime.now())))
            throw new ValidationException("the date should be in the future");






    }
}