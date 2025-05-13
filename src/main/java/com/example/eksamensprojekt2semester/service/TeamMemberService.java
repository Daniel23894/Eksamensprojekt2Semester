package com.example.eksamensprojekt2semester.service;

import com.example.eksamensprojekt2semester.model.TeamMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.eksamensprojekt2semester.repository.TeamMemberRepository;

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

    // Opretter et nyt teammedlem
    public TeamMember createTeamMember(TeamMember teamMember) {
        return teamMemberRepository.save(teamMember);
    }

    // Opdaterer et eksisterende teammedlem
    public TeamMember updateTeamMember(int memberId, TeamMember updatedTeamMember) {
        if (teamMemberRepository.existsById(memberId)) {
            updatedTeamMember.setMemberId(memberId);
            return teamMemberRepository.save(updatedTeamMember);
        } else {
            throw new IllegalArgumentException("Teammedlemmet findes ikke.");
        }
    }

    /** Count amount of team members for a specific project **/
    public int getTeamMemberCountByProjectId(int projectId){
        return teamMemberRepository.countByProjectId(projectId);
    }

    public List<TeamMember> getTeamMembersByProjectId(int projectId){
        return teamMemberRepository.findByProjectId(projectId);
    }
}
