package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.task.Task;

/**
 * Wraps all data at the Task List level
 */
public class TaskList implements ReadOnlyTaskList {

    private final ObservableList<Task> tasks;

    {
        tasks = FXCollections.observableArrayList();
    }

    public TaskList() {}

    /**
     * Creates the TaskList using Tasks in the {@code toBeCopied}
     */
    public TaskList(ReadOnlyTaskList toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    @Override
    public ObservableList<Task> getTaskList() {
        return tasks;
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the person list with {@code persons}.
     * {@code persons} must not contain duplicate persons.
     */
    public void setTasks(List<Task> tasks) {
        this.tasks.clear();
        this.tasks.addAll(tasks);
    }
    /**
     * Resets the existing data of this {@code TaskList} with {@code newData}.
     */
    public void resetData(ReadOnlyTaskList newData) {
        requireNonNull(newData);

        setTasks(newData.getTaskList());
    }

    /**
     * Adds a task to the task list.
     */
    public void addTask(Task t) {
        tasks.add(t);
    }

    /**
     * Check a task for duplicates
     */
    public boolean hasTask(Task t) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).equals(t)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.tasks.hashCode();
    }
}
