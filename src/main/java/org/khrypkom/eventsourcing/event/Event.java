package org.khrypkom.eventsourcing.event;


import org.khrypkom.eventsourcing.Id;

import java.time.Instant;

public interface Event {
    Id getId();

    Id getAggregateId();

    Instant getTimestamp();
}
