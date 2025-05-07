package aiss.gitminer.controller;

import aiss.gitminer.exception.CommitNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Commit;
import aiss.gitminer.repository.CommitRepository;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Commit", description = "Commit management API")
@RestController // El decorador @RestController indica que esta clase es un controlador REST, es decir, que maneja peticiones HTTP y devuelve respuestas en formato JSON
@RequestMapping("/gitminer/commits") // El decorador @RequestMapping indica la ruta base para todas las peticiones que maneja este controlador

public class CommitController {
    // El decorador @Autowired indica que Spring debe inyectar una instancia de CommitRepository en este controlador
    private final CommitRepository commitRepository;
    // Constructor de la clase
    @Autowired
    public CommitController(CommitRepository commitRepository){
        this.commitRepository = commitRepository;
    }
    // También se puede inicializar de la siguiente forma
    /*
    @Autowired
    CommitRepository commitRepository;
     */
    // OPERACIONES PARA CONSULTAR LOS DATOS (CRUD)
    @Operation(
            summary = "Get all commits",
            description = "Returns a list of all commits of the project",
            tags = { "commit", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All Commits Listed",
            content = { @Content(array = @ArraySchema(schema= @Schema(implementation = Commit.class)), mediaType= "application/json")}),
            @ApiResponse(responseCode = "404", description = "Commits Not Found",
            content = { @Content(schema= @Schema())})
    })
    // GET http://localhost:8080/api/commits
    @GetMapping
    public List<Commit> findAll(@Parameter(description="Zero-based page index (0..N), default 0")
                                @RequestParam(defaultValue="0") Integer page,
                                @Parameter(description="Number of commits per page, default = 10")
                                @RequestParam(defaultValue="10") Integer size,
                                @Parameter(description="Sorting criteria in the format: property(asc|desc). Default sort order is ascending. Sorted by the attribute name")
                                @RequestParam(required=false) String order){

        Page<Commit> pageCommits;
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
        pageCommits = commitRepository.findAll(paging);
        return pageCommits.getContent();
    }

    @Operation(
            summary = "Get commit by ID",
            description = "Returns a commit of the project by its ID",
            tags = { "commit", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Commit Found by ID",
            content = { @Content(schema= @Schema(implementation = Commit.class), mediaType= "application/json")}),
            @ApiResponse(responseCode = "404", description = "Commit Not Found",
            content = { @Content(schema= @Schema())})
    })
    // GET http://localhost:8080/api/commits/{id}
    @GetMapping("/{id}")
    public Commit findById(@Parameter(description="ID of the commit to be searched for")@PathVariable String id) throws CommitNotFoundException {
        Optional<Commit> commit = commitRepository.findById(id);
        if(!commit.isPresent()){
            throw new CommitNotFoundException();
            // Como en CommitNotFoundException hemos definido el ResponseStatus, nos devolverá un 404 y el mensaje definido en la clase
        }
        return commit.get(); // Si el commit no existe, se lanzará una excepción NoSuchElementException
        // El get se pone para obtener el valor del Optional, si no existe el valor, se lanzará una excepción
    }
}
