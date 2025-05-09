package controller;

import exception.ResourceNotFoundException;
import model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import service.TaskService;
import service.TeamMemberService;
import model.TeamMember;

import java.util.List;

@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TeamMemberService teamMemberService;

    @GetMapping("/tasks/{id}")
    public String viewTask(@PathVariable int id, Model model) {
        Task task = taskService.getTaskById(id);
        if (task == null) {
            throw new ResourceNotFoundException("Task not found with ID: " + id);
        }
        List<TeamMember> members = teamMemberService.getAllMembers();
        List<TeamMember> assigned = taskService.getAssignedMembers(id);
        model.addAttribute("task", task);
        model.addAttribute("teamMembers", members);
        model.addAttribute("assignedMembers", assigned);
        return "taskView";
    }

    @PostMapping("/tasks/assign")
    public String assignMember(@RequestParam int taskId, @RequestParam int memberId) {
        if (!taskService.existsById(taskId)) {
            throw new ResourceNotFoundException("Task not found with ID: " + taskId);
        }
        if (!TeamMemberService.existsById(memberId)) {
            throw new ResourceNotFoundException("Team member not found with ID: " + memberId);
        }
        taskService.assignMemberToTask(taskId, memberId);
        return "redirect:/tasks/" + taskId;
    }

    @PostMapping("/tasks/unassign")
    public String unassignMember(@RequestParam int taskId, @RequestParam int memberId) {
        if (!taskService.existsById(taskId)) {
            throw new ResourceNotFoundException("Task not found with ID: " + taskId);
        }
        if (!teamMemberService.existsById(memberId)) {
            throw new ResourceNotFoundException("Team member not found with ID: " + memberId);
        }
        taskService.unassignMemberFromTask(taskId, memberId);
        return "redirect:/tasks/" + taskId;
    }
}