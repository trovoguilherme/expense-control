package br.com.iug.entity.request;

import br.com.iug.entity.Parcela;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class ParcelaRequest {

    @PositiveOrZero
    private int qtdPaga;

    @Positive
    private int qtdRestante;

    public Parcela toParcela() {
        return Parcela.builder()
                .qtdPaga(this.qtdPaga)
                .qtdRestante(this.qtdRestante)
                .build();
    }

}
