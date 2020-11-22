package org.khrypkom.eventsourcing.event.task;

import org.khrypkom.eventsourcing.Id;

import java.util.Collection;
import java.util.List;

public interface TaskRepository {

    void scheduleTasks(Collection<Task> tasks);

    List<Task> lockAndFetchOutstandingTasks(Id executorId);

    void updateTaskStatus(Collection<Task> tasks);

}
