package aiss.gitminer.controller;

import aiss.gitminer.exception.CommentNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
    @GetMapping
    public List<Comment> findAll(){
        return commentRepository.findAll();
    }
    // GET http://localhost:8080/api/comments/{id}
    @GetMapping("/{id}")
    public Comment findById(@PathVariable String id) throws CommentNotFoundException {
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

