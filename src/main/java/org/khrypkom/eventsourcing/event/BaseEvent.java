package org.khrypkom.eventsourcing.event;

import lombok.*;
import org.khrypkom.eventsourcing.Id;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class BaseEvent implements Event{
    private Id id;
    private Id aggregateId;
    private Instant timestamp;

    public BaseEvent(Id id, Id aggregateId, Instant timestamp) {
        this.id = id == null ? new Id(UUID.randomUUID().toString()) : id;
        this.aggregateId = aggregateId;
        this.timestamp = timestamp;
    }

    public BaseEvent() {
    }
}
