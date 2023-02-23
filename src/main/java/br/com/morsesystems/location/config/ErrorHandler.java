package br.com.morsesystems.location.config;

import br.com.morsesystems.location.application.exception.NotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = RuntimeException.class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    public Error handleRuntimesException(Exception exception){
        return new Error(exception.getMessage());
    }

    @ExceptionHandler(value = NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    public Error handleNotFoundException(Exception exception){
        return new Error(exception.getMessage());
    }

}
