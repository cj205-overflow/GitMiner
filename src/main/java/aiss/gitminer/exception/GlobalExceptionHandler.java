package aiss.gitminer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

@ControllerAdvice // El decorador @ControllerAdvice indica que esta clase es un controlador de excepciones global
// Gracias a este decorador, la aplicación realiza un "escucha global" de todas las excepciones que se lanzan en los controladores
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    // Usamos el decorador anterior para la validación de errores en los modelos
    // Este decorador indica el método que se debe aplicar si se detecta un error del tipo que lleva en el parámetro
    @ResponseBody // Para devolver el mensaje de error en el cuerpo de la respuesta
    // No queremos que devuelva los mensajes de errores predeterminados ya que son más complejos de entender
    // Queremos que nos devuelva los mensajes que nosotros hemos programado en las validaciones, dentro de los propios modelos
    public ResponseEntity<Map<String, List<String>>> handleValidationException(MethodArgumentNotValidException ex){
        // Esta función devuelve un objeto ResponseEntity que contiene el mensaje de error
        // El ResponseEntity está formado por un Map cuyas claves son los nombres de los campos y los valores son listas de mensajes de error
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors(); // Obtenemos la lista de errores de validación
        // Mapeamos cada error a los mensajes de error por defecto que ya hemos definido en los modelos, mediante el parámetro "message"
        List<String> errors = fieldErrors.stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        Map<String,List<String>> res = new HashMap<>();
        res.put("errors", errors); // Añadimos la lista de errores al mapa
        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST); // El segundo parámetro es el código de estado HTTP (400)
        // El cuerpo de la respuesta será un campo "errors" que contendrá una lista de mensajes de error
    }
}
