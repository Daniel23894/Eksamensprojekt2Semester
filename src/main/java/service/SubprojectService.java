package service;

import model.Subproject;
import org.springframework.stereotype.Service;
import repository.SubprojectRepository;
import java.util.List;

@Service
public class SubprojectService {
    private final SubprojectRepository subprojectRepository;
    private final ProjectService projectService;

    /** Constructor injection for SubprojectRepository **/
    public SubprojectService(SubprojectRepository subprojectRepository, ProjectService projectService){
        this.subprojectRepository = subprojectRepository;
        this.projectService = projectService;
    }

    /** CREATE - method to create a new subproject **/
    public void createSubproject(Subproject subproject){

        /** Check if the subproject name is null or empty **/
        if (subproject.getName() == null || subproject.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Subprojekt navn er påkrævet");
        }

        /** Negative or zero IDs are invalid **/
        if (subproject.getProjectId() <= 0) {
            throw new IllegalArgumentException("Valg af overordnet projekt er påkrævet");
        }

        /** Check if the project with the given projectId exists in the database **/
        if (!projectService.existsById(subproject.getProjectId())) {
            throw new IllegalArgumentException("Det valgte projekt findes ikke");
        }

        /** Check if the completion percentage is within the valid range of 0 to 100. **/
        int completion = subproject.getCompletionPercentage();
        if (completion < 0 || completion > 100) {
            throw new IllegalArgumentException("Færdiggørelsesprocent skal være mellem 0 og 100");
        }

        /** Save the subproject if all validations pass **/
        subprojectRepository.save(subproject);
    }

    /** READ - find a specific subprojekt **/
    public Subproject findById(int id) {
        return subprojectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subprojekt ikke fundet"));
    }

    /** READ - find all subprojects for a projekt **/
    public List<Subproject> findAllByProjectId(int projectId) {
        return subprojectRepository.findAllByProjectId(projectId);
    }

    /** READ - find all subprojects **/
    public List<Subproject> findAll() {
        return subprojectRepository.findAll();
    }


    /** UPDATE - update a existing subproject **/
    public void updateSubproject(Subproject subproject) {

        /** Convert primitives to Integer objects for null checks **/
        Integer id = subproject.getId();
        Integer projectId = subproject.getProjectId();

        /** Check if subproject ID is null or doesn't exist in the repository **/
        if (id == null || !subprojectRepository.existsById(id)) {
            throw new IllegalArgumentException("Subprojektet findes ikke");
        }

        /** Validate projectId **/
        if (projectId == null) {
            throw new IllegalArgumentException("Valg af overordnet projekt er påkrævet");
        }

        /**  Check if the selected project exists **/
        if (!projectService.existsById(projectId)) {
            throw new IllegalArgumentException("Det valgte projekt findes ikke");
        }

        subprojectRepository.update(subproject);
    }


    /** DELETE - delete a subproject **/
    public void deleteById(int id) {
        subprojectRepository.deleteById(id);
    }

}
