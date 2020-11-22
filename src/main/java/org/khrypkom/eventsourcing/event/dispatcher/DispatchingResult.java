package org.khrypkom.eventsourcing.event.dispatcher;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.khrypkom.eventsourcing.Id;
import org.khrypkom.eventsourcing.event.handler.HandleResult;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class DispatchingResult {
    private Id eventId;
    private HandleResult result;
}
