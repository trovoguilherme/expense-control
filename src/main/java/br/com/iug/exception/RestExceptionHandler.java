package br.com.iug.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @Value("Recurso não encontrado")
    private String resourceNotFound;

    @ExceptionHandler({ItemNotFoundException.class, BancoNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseException notFoundException(HttpServletRequest request, Exception exception) {
        return new ResponseException(request, resourceNotFound, exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseException s(HttpServletRequest request, MethodArgumentNotValidException exception) {
        return new ResponseException(request, "Problemas com a requisição", exception.getBindingResult().getFieldError().getDefaultMessage());
    }

}
