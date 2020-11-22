package org.khrypkom.eventsourcing.event.task;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.khrypkom.eventsourcing.Id;
import org.khrypkom.eventsourcing.event.Event;

import java.time.Instant;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Getter
public class Task {

    @Builder.Default private Id id = new Id(UUID.randomUUID().toString());
    @Builder.Default private Status status = Status.IDLE;
    private Event event;
    private Instant createDate;
    private Instant completeDate;
    private Id executorId;

    public enum Status {
        IDLE,
        LOCKED,
        COMPLETED
    }
}
