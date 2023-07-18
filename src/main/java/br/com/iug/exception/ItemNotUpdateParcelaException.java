package br.com.iug.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemNotUpdateParcelaException extends Exception {

    private final String message;

}
