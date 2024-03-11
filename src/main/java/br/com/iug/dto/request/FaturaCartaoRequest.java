package br.com.iug.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FaturaCartaoRequest {

    @Valid
    private PagamentoRequest pagamento;

    @PositiveOrZero
    private double valor;

}
