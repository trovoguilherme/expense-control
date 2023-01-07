package br.com.iug.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

@Getter
public class ResponseException {

    private String method;

    private String path;

    private String errorMessage;

    private Object errorDetail;

    public ResponseException(HttpServletRequest request, String errorMessage, Object errorDetail) {
        this.method = request.getMethod();
        this.path = request.getRequestURI();
        this.errorMessage = errorMessage;
        this.errorDetail = errorDetail;
    }

}
