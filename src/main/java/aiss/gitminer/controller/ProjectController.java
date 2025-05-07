package aiss.gitminer.controller;

import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Issue;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.ProjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Tag(name = "Project", description = "API for managing projects")
@RestController
// El decorador @RestController indica que esta clase es un controlador REST, es decir, que maneja peticiones HTTP y devuelve respuestas en formato JSON
@RequestMapping("/gitminer/projects") // El decorador @RequestMapping indica la ruta base para todas las peticiones que maneja este controlador
public class ProjectController {
    private final ProjectRepository projectRepository;
    // Constructor de la clase
    @Autowired
    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // OPERACIONES A REALIZAR
    @Operation(
            summary = "Get all projects",
            description = "Returns a list of all projects",
            tags = { "project", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All Projects Listed",
                    content = { @Content(array=@ArraySchema(schema= @Schema(implementation = Project.class)), mediaType= "application/json")}),
            @ApiResponse(responseCode = "404", description = "Projects Not Found",
                    content = { @Content(schema= @Schema())})

    })
    // GET http://localhost:8080/api/projects
    @GetMapping
    public List<Project> findAll(
            @Parameter(description="Zero-based page index (0..N), default 0")
            @RequestParam(defaultValue="0") Integer page,
            @Parameter(description = "Number of projects per page, default = 10")
            @RequestParam(defaultValue="10") Integer size,
            @Parameter(description = "Sorting criteria in the format: property(asc|desc). Default sort order is ascending. Sorted by the attribute name")
            @RequestParam(required=false) String order) {

        Page<Project> pageProjects;
        Pageable paging;
        if(order != null){
            if(order.startsWith("-")) {
                paging = PageRequest.of(page, size, Sort.by(order.substring(1)).descending());
            } else {
                paging = PageRequest.of(page, size, Sort.by(order).ascending());
            }
        } else {
            paging = PageRequest.of(page, size);
        }
        pageProjects = projectRepository.findAll(paging);
        return pageProjects.getContent();
    }

    @Operation(
            summary = "Get project by ID",
            description = "Returns a project by its ID",
            tags = { "project", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project Found by its ID",
                    content = { @Content(schema= @Schema(implementation = Project.class), mediaType= "application/json")}),
            @ApiResponse(responseCode = "404", description = "Project Not Found",
                    content = { @Content(schema= @Schema())})
    })
    // GET http://localhost:8080/api/projects/{id}
    @GetMapping("/{id}")
    public Project findById(@Parameter(description="ID of the Project to be searched")@PathVariable String id) throws ProjectNotFoundException {
        Optional<Project> project = projectRepository.findById(id);
        if(!project.isPresent()){
            throw new ProjectNotFoundException();
        }
        return project.get();
    }
    // Añadir datos de un nuevo proyecto
    @Operation(
            summary = "Create a new project",
            description = "Creates a new project by adding its data in the body of the request, it will be inserted in the database",
            tags = { "project", "post"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Project Created",
                    content = { @Content(schema= @Schema(implementation = Project.class), mediaType= "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad Request",
                    content = { @Content(schema= @Schema())})
    })
    // POST http://localhost:8080/api/projects
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Project create(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Project to be created",
            required = true,
            content = @Content(schema = @Schema(implementation = Project.class)))
        @Valid @RequestBody Project project){
        Project _project = projectRepository.save(
                new Project(project.getId(),project.getName(), project.getWebUrl(), project.getCommits(), project.getIssues())
        );
        return _project;
    }
}
