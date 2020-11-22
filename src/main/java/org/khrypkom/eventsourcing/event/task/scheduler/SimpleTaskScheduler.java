package org.khrypkom.eventsourcing.event.task.scheduler;

import lombok.AllArgsConstructor;
import org.khrypkom.eventsourcing.event.task.Task;
import org.khrypkom.eventsourcing.event.task.TaskRepository;

import java.util.Collection;

@AllArgsConstructor
public class SimpleTaskScheduler implements TaskScheduler {
    private final TaskRepository repository;

    @Override
    public void schedule(Collection<Task> tasks) {
        repository.scheduleTasks(tasks);
    }
}
