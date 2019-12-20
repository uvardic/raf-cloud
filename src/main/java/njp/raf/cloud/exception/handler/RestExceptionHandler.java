package njp.raf.cloud.exception.handler;

import njp.raf.cloud.exception.InvalidUserInfoException;
import njp.raf.cloud.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {InvalidUserInfoException.class})
    public ResponseEntity<ExceptionResponse> handleInvalidUserInfoException(InvalidUserInfoException ex) {
        return new ResponseEntity<>(new ExceptionResponse(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(new ExceptionResponse(ex), HttpStatus.NOT_FOUND);
    }

}
