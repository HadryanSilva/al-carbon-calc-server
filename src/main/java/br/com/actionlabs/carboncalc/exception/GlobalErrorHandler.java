package br.com.actionlabs.carboncalc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<DefaultErrorMessage> handleNotFoundException(ResourceNotFoundException e) {
        var response = new DefaultErrorMessage(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<DefaultErrorMessage> handleRuntimeException(RuntimeException e) {
        var response = new DefaultErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
