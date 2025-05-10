package aiss.gitminer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason= "Commit not found")
// Este decorador sirve para indicar que la excepción es un error 404 (no encontrado) y se puede personalizar el mensaje de error
public class CommitNotFoundException extends Exception {
// Como esta clase extiende de Exception, el IDE nos marca esta clase como "Exception class"
}
