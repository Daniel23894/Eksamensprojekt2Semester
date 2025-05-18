package com.example.eksamensprojekt2semester.controller;

import com.example.eksamensprojekt2semester.model.Role;
import com.example.eksamensprojekt2semester.model.TeamMember;
import com.example.eksamensprojekt2semester.repository.TeamMemberRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    private final TeamMemberRepository teamMemberRepository;

    public LoginController(TeamMemberRepository teamMemberRepository) {
        this.teamMemberRepository = teamMemberRepository;
    }

    @GetMapping("/login")
    public String showLoginForm(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", true);
        }
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               HttpSession session) { /** Added session to save the logged user throughout the session when user uses the program **/

        /** Look up the user by email (username) **/
        TeamMember member = teamMemberRepository.findByEmail(username);

        /** Check if user exists and password matches **/
        if (member != null && password.equals(member.getPassword())) {
            session.setAttribute("loggedInUser", member); /** Save the logged user in the session **/

            Role role = member.getRole();
            if (role == Role.ADMIN){
                 return "redirect:/admin/dashboard"; /** Redirect to admin dashboard **/
            } else{
                return "redirect:/projects/overview"; /** Redirect to project overview **/
            }
        }
        /** Login failed **/
        return "redirect:/login?error=true";
    }

    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(HttpSession session, Model model){

        /** session.getAttribute returns an object because it doesn't know what kind of data is stored **/
        TeamMember member = (TeamMember) session.getAttribute("loggedInUser"); /** (TeamMember) specifies the object type**/

        if (member == null || !member.getRole().equals(Role.ADMIN)) {
            return "redirect:/login";   /** Redirect to login if not logged in or not an admin **/
        }

        model.addAttribute("member", member); /** Add the logged user to the model **/
        return "admin_dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); /** Invalidate the session to log out the user **/
        return "redirect:/login";
    }
}