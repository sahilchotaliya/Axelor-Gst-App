package com.axelor.contact.db.repo;

import java.time.Duration;
import java.time.LocalDateTime;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import com.axelor.contact.db.Event;

public class ContactListener {
    
    @PostPersist
    @PostUpdate
    public void calculateDuration(Event event) {
        
        LocalDateTime startdate=event.getStartDate();
        LocalDateTime enddate = event.getEndDate();
        long duration = Duration.between(startdate, enddate).toDays();
        event.setDuration(duration);
    }

}