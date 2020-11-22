package org.khrypkom.eventsourcing.event.dispatcher;


import org.khrypkom.eventsourcing.event.Event;

import java.util.Collection;

public interface EventDispatcher {
    Collection<DispatchingResult> dispatch(Collection<Event> events);
}
