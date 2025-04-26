package aiss.gitminer.controller;

import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Issue;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
// El decorador @RestController indica que esta clase es un controlador REST, es decir, que maneja peticiones HTTP y devuelve respuestas en formato JSON
@RequestMapping("/api/projects") // El decorador @RequestMapping indica la ruta base para todas las peticiones que maneja este controlador
public class ProjectController {
    private final ProjectRepository projectRepository;
    // Constructor de la clase
    @Autowired
    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // OPERACIONES A REALIZAR
    // GET http://localhost:8080/api/projects
    @GetMapping
    public List<Project> findAll() {
        return projectRepository.findAll();
    }
    // GET http://localhost:8080/api/projects/{id}
    @GetMapping("/{id}")
    public Project findById(@PathVariable String id) throws ProjectNotFoundException {
        Optional<Project> project = projectRepository.findById(id);
        if(!project.isPresent()){
            throw new ProjectNotFoundException();
        }
        return project.get();
    }
    // Añadir datos de un nuevo proyecto
    // POST http://localhost:8080/api/projects
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Project create(@Valid @RequestBody Project project){
        Project _project = projectRepository.save(
                new Project(project.getId(),project.getName(), project.getWebUrl(), project.getCommits(), project.getIssues())
        );
        return _project;
    }
}
