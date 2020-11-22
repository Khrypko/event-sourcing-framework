package org.khrypkom.eventsourcing.event.store;

import lombok.AllArgsConstructor;
import org.khrypkom.eventsourcing.event.Event;
import org.khrypkom.eventsourcing.event.task.Task;
import org.khrypkom.eventsourcing.event.task.scheduler.TaskScheduler;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public abstract class TaskBasedEventStore implements EventStore {
    private TaskScheduler scheduler;

    @Override
    public void saveEvents(Collection<Event> events) {
        doSaveEvents(events);
        scheduleTasks(events);
    }

    protected abstract void doSaveEvents(Collection<Event> events);

    private void scheduleTasks(Collection<Event> events) {
        List<Task> tasks = events.stream()
                .map(this::toTask)
                .collect(Collectors.toList());
        scheduler.schedule(tasks);
    }

    private Task toTask(Event event){
        return Task.builder()
                .createDate(Instant.now())
                .event(event)
                .build();
    }
}
