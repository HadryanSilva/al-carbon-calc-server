package br.com.actionlabs.carboncalc.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CalculationException extends ResponseStatusException {

    public CalculationException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

}
