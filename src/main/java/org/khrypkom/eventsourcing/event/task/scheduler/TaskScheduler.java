package org.khrypkom.eventsourcing.event.task.scheduler;

import org.khrypkom.eventsourcing.event.task.Task;

import java.util.Collection;

public interface TaskScheduler {
    void schedule(Collection<Task> tasks);
}
