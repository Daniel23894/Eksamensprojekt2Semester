package com.example.eksamensprojekt2semester.service;

import com.example.eksamensprojekt2semester.model.TeamMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.eksamensprojekt2semester.repository.TeamMemberRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



import java.util.List;

@Service // Marker klassen som en servicekomponent
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public TeamMemberService(TeamMemberRepository teamMemberRepository ,
                             BCryptPasswordEncoder passwordEncoder) {
        this.teamMemberRepository = teamMemberRepository;
        this.passwordEncoder = passwordEncoder;
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

    // Opretter et nyt teammedlem
    @Transactional
    public TeamMember createTeamMember(TeamMember teamMember) {
        String rawPassword = teamMember.getPassword();
        String hashedPassword = passwordEncoder.encode(rawPassword);
        teamMember.setPassword(hashedPassword);
        return teamMemberRepository.save(teamMember);
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
            existing.setRole(updatedData.getRole().toString()); // or .getValue(), depending on your Role enum
        }

        if (updatedData.getHoursPerDay() != null) {
            existing.setHoursPerDay(updatedData.getHoursPerDay());
        }

        String newPassword = updatedData.getPassword();
        if (newPassword != null && !newPassword.isBlank()) {
            existing.setPassword(passwordEncoder.encode(newPassword));
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
