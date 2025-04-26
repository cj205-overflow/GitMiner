package aiss.gitminer.controller;

import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.model.Commit;
import aiss.gitminer.model.Issue;
import aiss.gitminer.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
// El decorador @RestController indica que esta clase es un controlador REST, es decir, que maneja peticiones HTTP y devuelve respuestas en formato JSON
@RequestMapping("/api/issues") // El decorador @RequestMapping indica la ruta base para todas las peticiones que maneja este controlador
public class IssueController {
    private final IssueRepository issueRepository;
    // Constructor de la clase
    @Autowired
    public IssueController(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    // OPERACIONES A REALIZAR
    // GET http://localhost:8080/api/issues
    @GetMapping
    public List<Issue> findAll() {
        return issueRepository.findAll();
    }
    // GET http://localhost:8080/api/issues/{id}
    @GetMapping("/{id}")
    public Issue findById(@PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = issueRepository.findById(id);
        if(!issue.isPresent()){
            throw new IssueNotFoundException();
        }
        return issue.get();
    }

    // GET http://localhost:8080/api/issues/state/{state}
    @GetMapping("/state/{state}")
    public List<Issue> findByState(@PathVariable String state){
        List<Issue> allIssues = issueRepository.findAll();
        return allIssues.stream()
                .filter(issue -> issue.getState().equalsIgnoreCase(state))
                .toList();
    }

    // Obtener una issue por id y por state
    @GetMapping("/state/{state}/{id}")
    public Issue findByStateAndId(@PathVariable String state, @PathVariable String id){
        List<Issue> allIssues = issueRepository.findAll();
        return allIssues.stream()
                .filter(issue -> issue.getState().equalsIgnoreCase(state) && issue.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }
}
