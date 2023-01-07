package br.com.iug.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BancoNotFoundException extends Exception {

    private String message;

}
