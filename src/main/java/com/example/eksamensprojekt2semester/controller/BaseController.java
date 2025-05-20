package com.example.eksamensprojekt2semester.controller;

import com.example.eksamensprojekt2semester.model.Role;
import com.example.eksamensprojekt2semester.model.TeamMember;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * BaseController is the base class for controllers in our application.
 * Point of it is to contain common functionality that is often shared across multiple controllers during the flow of the application.
 * Basically, to avoid duplication code and we use it to add Role attributes to models to split functionality  acces based on user's role.**/

@Controller
public class BaseController {

    /**  This ModelAttribute method is used without a return value which makes so that is automatically invoked
     *   before every controller method. It adds the role values in this case  to the model **/
    @ModelAttribute
    public void addRoleAttributesToModel(HttpSession session, Model model) {
        TeamMember teamMember = (TeamMember) session.getAttribute("loggedInUser");
        if (teamMember != null) {
            model.addAttribute("isAdmin", teamMember.getRole() == Role.ADMIN);
            model.addAttribute("isDeveloper", teamMember.getRole() == Role.DEVELOPER);
            model.addAttribute("isPO", teamMember.getRole() == Role.PRODUCT_OWNER);
        }
    }
}
