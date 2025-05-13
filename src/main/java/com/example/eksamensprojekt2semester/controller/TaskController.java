package com.example.eksamensprojekt2semester.controller;

import com.example.eksamensprojekt2semester.exception.ResourceNotFoundException;
import com.example.eksamensprojekt2semester.exception.SubprojectNotFoundException;
import com.example.eksamensprojekt2semester.model.StateStatus;
import com.example.eksamensprojekt2semester.model.Subproject;
import com.example.eksamensprojekt2semester.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.eksamensprojekt2semester.service.SubprojectService;
import com.example.eksamensprojekt2semester.service.TaskService;
import com.example.eksamensprojekt2semester.service.TeamMemberService;
import com.example.eksamensprojekt2semester.model.TeamMember;

import java.util.List;

@Controller
public class TaskController {
    /** Logger for TaskController **/
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    private SubprojectService subprojectService;

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
            logger.error("Task not found with ID: " + taskId);
            throw new ResourceNotFoundException("Task not found with ID: " + taskId);
        }
        if (!teamMemberService.existsById(memberId)) {
            logger.error("Task not found with ID: " + taskId);
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

    /** Displays the form for creating a new task **/
    @GetMapping("/tasks/create")
    public String showCreateTaskForm(Model model){
        List<Subproject> listOfAllSubprojects = subprojectService.findAll();
        Task task = new Task();
        task.setStatus(StateStatus.NOT_STARTED);

        /** Add list of subprojects to the model and new Task instance **/
        model.addAttribute("subprojects", listOfAllSubprojects);
        model.addAttribute("task", task);

        return "create_task";
    }

    /** Processes the submission of the create task form **/
    @PostMapping("/tasks/create")
    public String createTask(@ModelAttribute("task") Task task, Model model){
        try {
            taskService.createTask(task);
            return "redirect:/subprojects/" + task.getSubprojectId(); /** Redirect to subproject details page **/
        }
        /** Handle case where parent subproject doesn't exist **/
        catch (SubprojectNotFoundException e){
            model.addAttribute("errorMessage", "Det valgte subprojekt findes ikke.");
            model.addAttribute("subprojects", subprojectService.findAll());
            return "create_task";
        }
        /** Generic fallback for unexpected errors **/
        catch (Exception e){
            logger.error("Fejl ved oprettelse af task:", e);
            model.addAttribute("errorMessage", "Der opstod en uventet fejl. Prøv igen senere.");
            model.addAttribute("subprojects", subprojectService.findAll());
            return "create_task";
        }
    }
}