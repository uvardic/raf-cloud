package njp.raf.cloud.exception.advice;

import njp.raf.cloud.exception.machine.InvalidMachineInfoException;
import njp.raf.cloud.exception.machine.InvalidMachineSearchRequestException;
import njp.raf.cloud.exception.machine.InvalidMachineStateException;
import njp.raf.cloud.exception.machine.MachineNotFoundException;
import njp.raf.cloud.exception.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler(value = {InvalidMachineInfoException.class})
    public ResponseEntity<ExceptionResponse> handleInvalidMachineInfoException(InvalidMachineInfoException ex) {
        return new ResponseEntity<>(new ExceptionResponse(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InvalidMachineSearchRequestException.class})
    public ResponseEntity<ExceptionResponse> handleInvalidMachineSearchRequestException(
            InvalidMachineSearchRequestException ex
    ) {
        return new ResponseEntity<>(new ExceptionResponse(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InvalidMachineStateException.class})
    public ResponseEntity<ExceptionResponse> handleInvalidMachineStateException(InvalidMachineStateException ex) {
        return new ResponseEntity<>(new ExceptionResponse(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {MachineNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleMachineNotFoundException(MachineNotFoundException ex) {
        return new ResponseEntity<>(new ExceptionResponse(ex), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {InvalidUserInfoException.class})
    public ResponseEntity<ExceptionResponse> handleInvalidUserInfoException(InvalidUserInfoException ex) {
        return new ResponseEntity<>(new ExceptionResponse(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {TokenParsingException.class})
    public ResponseEntity<ExceptionResponse> handleTokenParsingException(TokenParsingException ex) {
        return new ResponseEntity<>(new ExceptionResponse(ex), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UserAuthenticationException.class})
    public ResponseEntity<ExceptionResponse> handleUserAuthenticationException(UserAuthenticationException ex) {
        return new ResponseEntity<>(new ExceptionResponse(ex), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {UserForbiddenException.class})
    public ResponseEntity<ExceptionResponse> handleUserForbiddenException(UserForbiddenException ex) {
        return new ResponseEntity<>(new ExceptionResponse(ex), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(new ExceptionResponse(ex), HttpStatus.NOT_FOUND);
    }

}
