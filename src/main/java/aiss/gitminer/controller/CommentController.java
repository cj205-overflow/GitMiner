package aiss.gitminer.controller;

import aiss.gitminer.exception.CommentNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.repository.CommentRepository;
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

@Tag(name = "Comment", description = "Comment management API")
@RestController // El decorador @RestController indica que esta clase es un controlador REST, es decir, que maneja peticiones HTTP y devuelve respuestas en formato JSON
@RequestMapping("/gitminer/comments") // El decorador @RequestMapping indica la ruta base para todas las peticiones que maneja este controlador
public class CommentController {
    // El decorador @Autowired indica que Spring debe inyectar una instancia de CommitRepository en este controlador
    private final CommentRepository commentRepository;
    // Constructor de la clase
    @Autowired
    public CommentController(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }
    // También se puede inicializar de la siguiente forma
    /*
    @Autowired
    CommitRepository commitRepository;
     */
    // OPERACIONES PARA CONSULTAR LOS DATOS (CRUD)
    // GET http://localhost:8080/api/comments
    @Operation(
            summary = "Get all comments",
            description = "Returns a list of all comments of the project issues",
            tags = { "comment", "get"}
    )

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All Comments Listed",
            content = { @Content(array = @ArraySchema(schema = @Schema(implementation = Comment.class)), mediaType= "application/json")}),
            @ApiResponse(responseCode = "404", description = "Comments Not Found",
            content = { @Content(schema = @Schema())})
    })

    @GetMapping
    public List<Comment> findAll(@Parameter(description="Zero-based page index (0..N), default 0")
                                 @RequestParam(defaultValue="0") Integer page,
                                 @Parameter(description="Number of comments per page, default = 10")
                                 @RequestParam(defaultValue="10") Integer size,
                                 @Parameter(description="Sorting criteria in the format: property(asc|desc). Default sort order is ascending. Sorted by the attribute name")
                                 @RequestParam(required=false) String order){

        Page<Comment> pageComments;
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
        pageComments = commentRepository.findAll(paging);
        return pageComments.getContent();
    }

    @Operation(
            summary = "Get comment by ID",
            description = "Returns a comment of the project issues by its ID",
            tags = { "comment", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode="200", description= "Comment Found by ID",
            content = { @Content(schema = @Schema(implementation = Comment.class), mediaType= "application/json")}),
            @ApiResponse(responseCode="404", description= "Comment Not Found",
            content = { @Content(schema = @Schema())})
    })
    // GET http://localhost:8080/api/comments/{id}
    @GetMapping("/{id}")
    public Comment findById(@Parameter(description= "ID of the comment to be searched for")@PathVariable String id) throws CommentNotFoundException {
        Optional<Comment> comment = commentRepository.findById(id);
        // Gestionamos la excepción si el comentario no existe
        if(!comment.isPresent()){
            throw new CommentNotFoundException();
            // Como en CommentNotFoundException hemos definido el ResponseStatus, nos devolverá un 404 y el mensaje definido en la clase
        }
        return comment.get(); // Si el commit no existe, se lanzará una excepción NoSuchElementException
        // El get se pone para obtener el valor del Optional, si no existe el valor, se lanzará una excepción
    }
}

