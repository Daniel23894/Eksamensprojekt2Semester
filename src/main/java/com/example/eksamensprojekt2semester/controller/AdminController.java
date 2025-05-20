package com.example.eksamensprojekt2semester.controller;


import com.example.eksamensprojekt2semester.model.TeamMember;
import com.example.eksamensprojekt2semester.service.TeamMemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final TeamMemberService teamMemberService;

    /** Dependency injection for TeamMemberService **/
    public AdminController(TeamMemberService teamMemberService){
        this.teamMemberService = teamMemberService;
    }

    @GetMapping("/overview")
    public String showTeamMembersOverview(HttpSession session, Model model){
        if (session.getAttribute("loggedInUser") == null){
            return "redirect:/login"; /** Redirect to login page if user isn't logged in already **/
        } else {
            List<TeamMember> listOfTeamMembers = teamMemberService.getAllMembers();
            model.addAttribute("teamMembers", listOfTeamMembers);
            return "team_member_overview"; /** Return team member overview page **/
        }
    }
}
