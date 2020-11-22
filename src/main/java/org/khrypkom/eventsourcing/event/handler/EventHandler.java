package org.khrypkom.eventsourcing.event.handler;

import org.khrypkom.eventsourcing.event.Event;

public interface EventHandler<E extends Event> {
    boolean isApplicableFor(Event event);
    HandleResult handle(E event);
}
