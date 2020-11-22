package org.khrypkom.eventsourcing.event.store;

import org.khrypkom.eventsourcing.Id;
import org.khrypkom.eventsourcing.event.Event;

import java.util.Collection;

public interface EventStore {

    Collection<Event> getEvents(Id aggregateId);
    void saveEvents(Collection<Event> events);

}
