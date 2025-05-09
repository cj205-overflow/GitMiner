package aiss.gitminer.controller;

import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.repository.CommentRepository;
import aiss.gitminer.repository.IssueRepository;
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

@Tag(name = "Issue", description = "Issue management API")
@RestController
// El decorador @RestController indica que esta clase es un controlador REST, es decir, que maneja peticiones HTTP y devuelve respuestas en formato JSON
@RequestMapping("/gitminer/issues") // El decorador @RequestMapping indica la ruta base para todas las peticiones que maneja este controlador
public class IssueController {
    private final IssueRepository issueRepository;
    private final CommentRepository commentRepository;

    // Constructor de la clase
    @Autowired
    public IssueController(IssueRepository issueRepository, CommentRepository commentRepository) {
        this.issueRepository = issueRepository;
        this.commentRepository = commentRepository;
    }

    // OPERACIONES A REALIZAR

    @Operation(
            summary = "Get all issues",
            description = "Returns a list of all issues of the project",
            tags = { "issue", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All Issues Listed",
                    content = { @Content(array= @ArraySchema(schema= @Schema(implementation = Issue.class)), mediaType= "application/json")}),
            @ApiResponse(responseCode = "404", description = "Issues Not Found",
                    content = { @Content(schema= @Schema())})
    })
    // GET http://localhost:8080/api/issues[?state=open&authorId=12345]
    @GetMapping
    public List<Issue> findAll(@Parameter(description = "ID of the author", example = "12345")
                               @RequestParam(required = false, name= "authorId") String authorId,
                               @Parameter(description = "State of the issue (e.g., open, closed)", example = "open")
                               @RequestParam(required = false, name = "state") String state,
                               @Parameter(description = "Zero-based page index (0..N), default 0")
                               @RequestParam(defaultValue="0") Integer page,
                               @Parameter(description = "Number of issues per page, default = 10")
                               @RequestParam(defaultValue="10") Integer size,
                               @Parameter(description = "Sorting criteria in the format: property(asc|desc). Default sort order is ascending. Sorted by the attribute name")
                               @RequestParam(required=false) String order) {

        Page<Issue> pageIssues;
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
        pageIssues = issueRepository.findAll(paging);

        if(authorId != null) {
            pageIssues = issueRepository.findByAuthorId(authorId, paging);
        }
        if(state != null) {
            pageIssues = issueRepository.findByState(state, paging);
        }
        // Si los dos datos no son null (esto no aparece en las pruebas Postman)
        if(authorId != null && state != null) {
            pageIssues = issueRepository.findByStateAndAuthorId(authorId, state, paging);
        }
        return pageIssues.getContent();
    }

    @Operation(
            summary = "Get issue by ID",
            description = "Returns an issue of the project by its ID",
            tags = { "issue", "get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Issue Found by ID",
                    content = { @Content(schema= @Schema(implementation = Issue.class), mediaType= "application/json")}),
            @ApiResponse(responseCode = "404", description = "Issue Not Found",
                    content = { @Content(schema= @Schema())})
    })
    // GET http://localhost:8080/api/issues/{id}
    @GetMapping("/{id}")
    public Issue findById(@Parameter(description= "ID of the issue to be searched for")@PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = issueRepository.findById(id);
        if(!issue.isPresent()){
            throw new IssueNotFoundException();
        }
        return issue.get();
    }

    @Operation(
            summary = "Get issue comments",
            description = "Returns a list of all comments of the issue by its ID",
            tags = { "issue", "comment","get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All Comments of the Issue Listed",
                    content = { @Content(array=@ArraySchema(schema= @Schema(implementation = Comment.class)), mediaType= "application/json")}),
            @ApiResponse(responseCode = "404", description = "Comments Not Found",
                    content = { @Content(schema= @Schema())})
    })
    // GET http://localhost:8080/api/issues/{id}/comments

    @GetMapping("/{id}/comments")
    public List<Comment> getIssueComments(@Parameter(description="ID of the issue you want to get comments ")
                                          @PathVariable String id,
                                          @Parameter(description = "Zero-based page index (0..N), default 0")
                                          @RequestParam(defaultValue="0") Integer page,
                                          @Parameter(description = "Number of comments per page, default = 10")
                                          @RequestParam(defaultValue="10") Integer size,
                                          @Parameter(description = "Sorting criteria in the format: property(asc|desc). Default sort order is ascending. Sorted by the attribute name")
                                          @RequestParam(required=false) String order) throws IssueNotFoundException {

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
        Optional<Issue> issue = issueRepository.findById(id);
        if(!issue.isPresent()){
            throw new IssueNotFoundException();
        }
        pageComments = commentRepository.findCommentsByIssueId(id, paging);
        return pageComments.getContent();
    }

}
