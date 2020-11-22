package org.khrypkom.eventsourcing.event.dispatcher;

import lombok.AllArgsConstructor;
import org.khrypkom.eventsourcing.event.Event;
import org.khrypkom.eventsourcing.event.handler.EventHandler;
import org.khrypkom.eventsourcing.event.handler.HandleResult;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
public class BaseEventDispatcher implements EventDispatcher {
    private Collection<EventHandler> handlers;

    @Override
    public Collection<DispatchingResult> dispatch(Collection<Event> events) {
        return events.stream()
                .flatMap(event -> doDispatch(event).stream())
                .collect(Collectors.toList());
    }

    protected Collection<DispatchingResult> doDispatch(Event event) {
        return handlers.stream()
                .filter(eventHandler -> eventHandler.isApplicableFor(event))
                .map(eventHandler -> handleEvent(eventHandler, event))
                .collect(Collectors.toList());
    }

    private DispatchingResult handleEvent(EventHandler eventHandler, Event event) {
        HandleResult result = eventHandler.handle(event);
        return new DispatchingResult(event.getId(), result);
    }
}
