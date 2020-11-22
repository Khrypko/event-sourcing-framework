package org.khrypkom.eventsourcing.event.dispatcher;

import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;
import org.khrypkom.eventsourcing.Id;
import org.khrypkom.eventsourcing.event.Event;
import org.khrypkom.eventsourcing.event.handler.EventHandler;
import org.khrypkom.eventsourcing.event.handler.HandleResult;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BaseEventDispatcherTest {

    @Test
    public void testEventAreDispatchedToProperHandlers() {
        //given
        EventHandler<Event1> handler1 = (EventHandler<Event1>) mock(EventHandler.class);
        when(handler1.isApplicableFor(any(Event.class))).thenAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return args[0] instanceof Event1;
            }
        });
        when(handler1.handle(any(Event1.class))).thenReturn(HandleResult.SUCCESS);

        EventHandler<Event2> handler2 = (EventHandler<Event2>) mock(EventHandler.class);
        when(handler2.isApplicableFor(any(Event.class))).thenAnswer(new Answer() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return args[0] instanceof Event2;
            }
        });
        when(handler2.handle(any(Event2.class))).thenReturn(HandleResult.SUCCESS);

        List<Event> events = List.of(
                new Event1(new Id("1"), new Id("1"), Instant.now()),
                new Event2(new Id("2"), new Id("2"), Instant.now())
        );

        BaseEventDispatcher eventDispatcher = new BaseEventDispatcher(List.of(handler1, handler2));

        //when
        Collection<DispatchingResult> dispatch = eventDispatcher.dispatch(events);


        verify(handler1, times(1)).handle(any(Event1.class));
        verify(handler2, times(1)).handle(any(Event2.class));
        assertThat(dispatch).containsAll(
                List.of(
                        new DispatchingResult(new Id("1"), HandleResult.SUCCESS),
                        new DispatchingResult(new Id("1"), HandleResult.SUCCESS)
                )
        );
    }

    public static class Event1 implements Event {
        private Id id;
        private Id aggregateId;
        private Instant timestamp;

        public Event1(Id id, Id aggregateId, Instant timestamp) {
            this.id = id;
            this.aggregateId = aggregateId;
            this.timestamp = timestamp;
        }

        public Id getId() {
            return id;
        }

        public Id getAggregateId() {
            return aggregateId;
        }

        public Instant getTimestamp() {
            return timestamp;
        }
    }

    public static class Event2 implements Event {
        private Id id;
        private Id aggregateId;
        private Instant timestamp;

        public Event2(Id id, Id aggregateId, Instant timestamp) {
            this.id = id;
            this.aggregateId = aggregateId;
            this.timestamp = timestamp;
        }

        @Override
        public Id getId() {
            return id;
        }

        @Override
        public Id getAggregateId() {
            return aggregateId;
        }

        @Override
        public Instant getTimestamp() {
            return timestamp;
        }
    }


}