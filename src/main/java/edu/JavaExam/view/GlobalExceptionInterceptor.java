package edu.JavaExam.view;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionInterceptor {
    //Intercepte toutes les erreurs de validation du model (@NotNull, @NotBlank, @Size ...)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody // Assure que la réponse est envoyée au format JSON (dans le corp de la réponse)
    public Map<String, Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


    //Intercepte toutes les erreurs de contraintes dans la base de données
    // (contrainte de clé étrangère, champs unique ...)
    // Note : attention attrape également les contraintes nullable = false
    // si l’annotartion @NotNull est manquantes dans le modèle par exemple
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // Code de retour
    @ResponseBody // Assure que la réponse est envoyée au format JSON (dans le corp de la réponse)
    public Map<String, Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return Map.of("message","Une violation de contrainte d'intégrité de données a été détectée");
    }
}