package br.com.iug.dto.request;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FaturaCartaoUpdate {

    @PositiveOrZero(message = "Valor deve ser positivo ou zero")
    private double valor;

}
