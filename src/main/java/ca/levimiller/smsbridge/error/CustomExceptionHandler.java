package ca.levimiller.smsbridge.error;

import java.util.HashMap;
import java.util.Map;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CustomExceptionHandler {

  /**
   * Catches the constraint violations and returns a bad request status
   * with a map of the violations.
   * @param ex - constraint violation exception
   * @return - error map
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ConstraintViolationException.class)
  public Map<String, String> handleValidationExceptions(
      ConstraintViolationException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getConstraintViolations().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }
}
