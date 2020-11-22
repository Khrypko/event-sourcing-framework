package org.khrypkom.eventsourcing.event.task.executor;

import lombok.RequiredArgsConstructor;
import org.khrypkom.eventsourcing.Id;
import org.khrypkom.eventsourcing.event.Event;
import org.khrypkom.eventsourcing.event.dispatcher.DispatchingResult;
import org.khrypkom.eventsourcing.event.dispatcher.EventDispatcher;
import org.khrypkom.eventsourcing.event.handler.HandleResult;
import org.khrypkom.eventsourcing.event.task.Task;
import org.khrypkom.eventsourcing.event.task.TaskRepository;

import java.time.Instant;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DispatchingEventTaskExecutor implements TaskExecutor {
    private final Id executorId;
    private final TaskRepository taskRepository;
    private final EventDispatcher eventDispatcher;

    @Override
    public void executeOutstandingTasks() {
        System.out.println("Executing scheduled tasks");
        List<Task> tasks = taskRepository.lockAndFetchOutstandingTasks(executorId);
        List<Event> events = tasks.stream()
                .map(Task::getEvent)
                .sorted(Comparator.comparing(Event::getTimestamp))
                .collect(Collectors.toList());
        Collection<DispatchingResult> results = eventDispatcher.dispatch(events);
        finishTasks(results, tasks);
    }

    private void finishTasks(Collection<DispatchingResult> results, List<Task> tasks) {
        Map<Id, Task> tasksByEventId = groupTasksByEventId(tasks);
        List<Task> updatedTasks = results.stream()
                .map(result -> updateTaskStatusBasedOrResult(result, tasksByEventId.get(result.getEventId())))
                .collect(Collectors.toList());

        taskRepository.updateTaskStatus(updatedTasks);

        unlockTasksWithUnsupportedEventHandlers(results, tasks);
    }

    private void unlockTasksWithUnsupportedEventHandlers(Collection<DispatchingResult> results, List<Task> tasks) {
        List<Task> tasksToUnlock = tasks.stream()
                .filter(doesNotHaveResult(results))
                .map(task ->
                        updateTaskStatusBasedOrResult(
                                new DispatchingResult(task.getEvent().getId(), HandleResult.FAILURE),
                                task
                        )
                )
                .collect(Collectors.toList());
        taskRepository.updateTaskStatus(tasksToUnlock);
    }

    private Predicate<Task> doesNotHaveResult(Collection<DispatchingResult> results) {
        return task -> results.stream().noneMatch(result -> result.getEventId().equals(task.getEvent().getId()));
    }

    private Task updateTaskStatusBasedOrResult(DispatchingResult dispatchingResult, Task task) {
        Task.TaskBuilder taskBuilder = Task.builder()
                .id(task.getId())
                .createDate(task.getCreateDate())
                .event(task.getEvent());

        if (dispatchingResult.getResult() == HandleResult.SUCCESS){
            completeTask(task, taskBuilder);
        } else {
            unlockTask(taskBuilder);
        }

        return taskBuilder.build();
    }

    private void unlockTask(Task.TaskBuilder taskBuilder) {
        taskBuilder
                .executorId(null)
                .status(Task.Status.IDLE);
    }

    private void completeTask(Task task, Task.TaskBuilder taskBuilder) {
        taskBuilder
                .executorId(task.getExecutorId())
                .completeDate(Instant.now())
                .status(Task.Status.COMPLETED);
    }

    private Map<Id, Task> groupTasksByEventId(List<Task> tasks) {
        return tasks.stream()
                .collect(Collectors.toMap(
                        (task) -> task.getEvent().getId(),
                        (task) -> task)
                );
    }
}
