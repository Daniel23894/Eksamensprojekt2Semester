package com.example.eksamensprojekt2semester.service;

import com.example.eksamensprojekt2semester.model.Role;
import com.example.eksamensprojekt2semester.model.TeamMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.eksamensprojekt2semester.repository.TeamMemberRepository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service // Marker klassen som en servicekomponent
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;

    @Autowired
    public TeamMemberService(TeamMemberRepository teamMemberRepository) {
        this.teamMemberRepository = teamMemberRepository;
    }

    // Tjekker om en teammedlem findes i databasen ved hjælp af ID
    public boolean existsById(int memberId) {
        return teamMemberRepository.existsById(memberId);
    }

    // Henter alle teammedlemmer fra databasen
    public List<TeamMember> getAllMembers() {
        return teamMemberRepository.findAll();
    }

    // Henter et teammedlem baseret på ID
    public TeamMember getMemberById(int memberId) {
        return teamMemberRepository.findById(memberId);
    }

    /** Creates new team member  **/
    @Transactional
    public TeamMember createTeamMember(TeamMember teamMember) {
        return teamMemberRepository.save(teamMember);
    }

    @Transactional
    public void createAdminIfMissing() {
        String email = "admin@alphasolutions.dk";
        if (!teamMemberRepository.existsByEmail(email)) {
            TeamMember admin = new TeamMember();
            admin.setName("System Admin");
            admin.setEmail(email);
            admin.setPassword(("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setHoursPerDay(BigDecimal.valueOf(8));

            teamMemberRepository.save(admin);
            System.out.println("✅ Admin created");
        } else {
            System.out.println("ℹ️ Admin already exists");
        }
    }

    // Opdaterer et eksisterende teammedlem
    @Transactional
    public TeamMember updateTeamMember(int memberId, TeamMember updatedData) {
        /** Find the existing member **/
        TeamMember existing = teamMemberRepository.findById(memberId);

        if (existing == null) {
            throw new IllegalArgumentException("Teammedlemmet findes ikke.");
        }

        // Update fields (only if new values are given)
        if (updatedData.getName() != null) {
            existing.setName(updatedData.getName());
        }

        if (updatedData.getEmail() != null) {
            existing.setEmail(updatedData.getEmail());
        }

        if (updatedData.getRole() != null) {
            existing.setRole(updatedData.getRole()); // or .getValue(), depending on your Role enum
        }

        if (updatedData.getHoursPerDay() != null) {
            existing.setHoursPerDay(updatedData.getHoursPerDay());
        }

        String newPassword = updatedData.getPassword();
        if (newPassword != null && !newPassword.isBlank()) {
            existing.setPassword((newPassword));
        }

        // Save the updated member
        return teamMemberRepository.save(existing);
    }

    /** Count amount of team members for a specific project **/
    public int getTeamMemberCountByProjectId(int projectId){
        return teamMemberRepository.countByProjectId(projectId);
    }

    public List<TeamMember> getTeamMembersByProjectId(int projectId){
        return teamMemberRepository.findByProjectId(projectId);
    }

    @Transactional
    public void deleteTeamMember(int memberId) {
        if (!existsById(memberId)) {
            throw new IllegalArgumentException("Teammedlemmet findes ikke.");
        }
        teamMemberRepository.deleteById(memberId);
    }
}
