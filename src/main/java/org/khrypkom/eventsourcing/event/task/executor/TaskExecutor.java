package org.khrypkom.eventsourcing.event.task.executor;

public interface TaskExecutor {
    void executeOutstandingTasks();
}
