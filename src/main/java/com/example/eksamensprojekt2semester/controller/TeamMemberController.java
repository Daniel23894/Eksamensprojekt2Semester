package com.example.eksamensprojekt2semester.controller;

import com.example.eksamensprojekt2semester.model.Role;
import com.example.eksamensprojekt2semester.model.TeamMember;
import com.example.eksamensprojekt2semester.service.TeamMemberService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;

@Controller
@RequestMapping("/team-members")
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

    private static final Logger logger = LoggerFactory.getLogger(TeamMemberController.class);

    public TeamMemberController(TeamMemberService teamMemberService) {
        this.teamMemberService = teamMemberService;
    }

    /**
     * Shows a list of all team members.
     */
//    @GetMapping("/list")
//    public String showAllTeamMembers(Model model, HttpSession session) {
//        if (session.getAttribute("employee") == null) {
//            return "redirect:/login";
//        }
//
//        List<TeamMember> teamMembers = teamMemberService.getAllMembers();
//        model.addAttribute("teamMembers", teamMembers);
//        return "team_member_list";
//    }

    /**
     * Shows a form for creating a new team member.
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("teamMember", new TeamMember());
        model.addAttribute("roles", Role.values());
        return "create_team_member";
    }

    /**
     * Handles submission of a new team member.
     */
    @PostMapping("/create")
    public String createTeamMember(@ModelAttribute("teamMember") TeamMember teamMember, Model model) {
        try {
            teamMemberService.createTeamMember(teamMember);
            return "redirect:/admin/overview";
        } catch (Exception e) {
            logger.error("Fejl under oprettelse af team member", e);
            model.addAttribute("errorMessage", "Noget gik galt. Prøv igen.");
            model.addAttribute("roles", Role.values());
            return "create_team_member";
        }
    }

    /**
     * Displays a form to edit an existing team member.
     */
//    @GetMapping("/edit/{id}")
//    public String showEditForm(@PathVariable int id, Model model) {
//        TeamMember teamMember = teamMemberService.getMemberById(id);
//        if (teamMember == null) {
//            return "redirect:/admin/overview";
//        }
//        model.addAttribute("teamMember", teamMember);
//        model.addAttribute("roles", Role.values());
//        return "team_member_form";
//    }
//
//    /**
//     * Handles updating a team member.
//     */
//    @PostMapping("/edit")
//    public String updateTeamMember(@ModelAttribute("teamMember") TeamMember teamMember, Model model) {
//        try {
//            teamMemberService.updateTeamMember(teamMember.getMemberId(), teamMember); ;
//            return "redirect:/team-members/list";
//        } catch (Exception e) {
//            logger.error("Fejl ved opdatering af team member", e);
//            model.addAttribute("errorMessage", "Kunne ikke opdatere team medlem.");
//            model.addAttribute("roles", Role.values());
//            return "team_member_form";
//        }
//    }
//
//    /**
//     * Deletes a team member by ID.
//     */
//    @GetMapping("/delete/{id}")
//    public String deleteTeamMember(@PathVariable int id) {
//        teamMemberService.deleteTeamMember(id);
//        return "redirect:/team-members/list";
//    }
}
