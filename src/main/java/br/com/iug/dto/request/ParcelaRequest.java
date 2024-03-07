package br.com.iug.dto.request;

import br.com.iug.entity.Parcela;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static br.com.iug.entity.utility.JavaCodes.ERROR_PARCELA_QTD_PAGA_MENOR_QUE_ZERO;
import static br.com.iug.entity.utility.JavaCodes.ERROR_PARCELA_QTD_RESTANTE_MENOR_QUE_ZERO;

@Builder
@Getter
@AllArgsConstructor
public class ParcelaRequest {

    @PositiveOrZero(message = "{"+ ERROR_PARCELA_QTD_PAGA_MENOR_QUE_ZERO +"}")
    private int qtdPaga;

    @Positive(message = "{"+ ERROR_PARCELA_QTD_RESTANTE_MENOR_QUE_ZERO +"}")
    private int qtdRestante;

    public Parcela toParcela() {
        return Parcela.builder()
                .qtdPaga(this.qtdPaga)
                .qtdRestante(this.qtdRestante)
                .build();
    }

}
