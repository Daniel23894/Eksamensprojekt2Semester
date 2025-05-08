package service;

import model.Task;
import model.TeamMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.TaskAssignmentRepository;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskAssignmentRepository taskAssignmentRepo;

    @Autowired
    private TaskRepository taskRepo;

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
}

