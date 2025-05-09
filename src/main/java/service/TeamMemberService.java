package service;

import model.TeamMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.TeamMemberRepository;

import java.util.List;

@Service
public class TeamMemberService {
    private final TeamMemberRepository teamMemberRepository;

    @Autowired
    public TeamMemberService(TeamMemberRepository teamMemberRepository) {
        this.teamMemberRepository = teamMemberRepository;
    }

    public boolean existsById(int memberId) {
        return teamMemberRepository.existsById(memberId);
    }

    public List<TeamMember> getAllMembers() {
        return teamMemberRepository.findAll();
    }
}