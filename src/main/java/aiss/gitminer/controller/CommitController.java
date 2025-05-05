package aiss.gitminer.controller;

import aiss.gitminer.exception.CommitNotFoundException;
import aiss.gitminer.model.Commit;
import aiss.gitminer.repository.CommitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
    // GET http://localhost:8080/api/commits
    @GetMapping
    public List<Commit> findAll(){
        return commitRepository.findAll();
    }
    // GET http://localhost:8080/api/commits/{id}
    @GetMapping("/{id}")
    public Commit findById(@PathVariable String id) throws CommitNotFoundException {
        Optional<Commit> commit = commitRepository.findById(id);
        if(!commit.isPresent()){
            throw new CommitNotFoundException();
            // Como en CommitNotFoundException hemos definido el ResponseStatus, nos devolverá un 404 y el mensaje definido en la clase
        }
        return commit.get(); // Si el commit no existe, se lanzará una excepción NoSuchElementException
        // El get se pone para obtener el valor del Optional, si no existe el valor, se lanzará una excepción
    }
}
