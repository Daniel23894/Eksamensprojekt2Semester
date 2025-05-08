package service;

import exception.SubprojectNotFoundException;
import model.Task;
import model.TeamMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.SubprojectRepository;
import repository.TaskAssignmentRepository;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskAssignmentRepository taskAssignmentRepo;

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private SubprojectRepository subprojectRepo;

    public void assignMemberToTask(int taskId, int memberId) {
        taskAssignmentRepo.assignMember(taskId, memberId);
    }

    public void unassignMemberFromTask(int taskId, int memberId) {
        taskAssignmentRepo.unassignMember(taskId, memberId);
    }

    public List<TeamMember> getAssignedMembers(int taskId) {
        return taskAssignmentRepo.getAssignedMembers(taskId);
    }

    public Task getTaskById(int id) {
        return taskRepo.getTaskById(id);
    }


    public boolean existsById(int taskId) {
        return TaskRepository.existsById(taskId);
    }

    public void createTask(Task task) {
        /** Check if parent subproject exists **/
        if (!subprojectRepo.existsById(task.getSubprojectId())) {
            throw new SubprojectNotFoundException("Subproject not found with ID: " + task.getSubprojectId());
        }
        taskRepo.save(task);
    }

    public void updateTask(Task task) {
        /** Ensure the task exists **/
        if (!taskRepo.existsById(task.getId())) {
            throw new SubprojectNotFoundException("Task not found with ID: " + task.getId());
        }

        /** Check if parent subproject exists **/
        if (!subprojectRepo.existsById(task.getSubprojectId())) {
            throw new SubprojectNotFoundException("Subproject not found with ID: " + task.getSubprojectId());
        }

        taskRepo.update(task);
    }

    public void deleteTask(int id) {
        /** First remove any team member assignments,to avoid assignments referring to a task that no longer exists **/
        taskAssignmentRepo.removeAllAssignmentsForTask(id);

        /** Then delete the task **/
        taskRepo.deleteById(id);
    }

    /** Get all tasks **/
    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    /** Get all tasks for a specific subproject **/
    public List<Task> getTasksBySubprojectId(int subprojectId) {
        return taskRepo.findBySubprojectId(subprojectId);
    }


}

