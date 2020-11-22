package org.khrypkom.eventsourcing.event;


import org.khrypkom.eventsourcing.Aggregate;

import java.util.Collection;

public interface Command<A extends Aggregate> {

    Collection<Event> executeOn(A aggregate);

}
