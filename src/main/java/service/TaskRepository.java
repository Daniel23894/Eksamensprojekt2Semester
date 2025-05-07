package service;

import java.util.HashMap;
import java.util.Map;

public class TaskRepository {
    private Map<Integer, Task> tasks = new HashMap<>();

    public static boolean existsById(int taskId) {
        // Simulate a check in the database
        return taskId >= 0 && taskId < 100; // Example condition
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }

}