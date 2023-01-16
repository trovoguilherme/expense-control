package br.com.iug.entity.request;

import br.com.iug.entity.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemRequest {

    @NotBlank
    private String nome;

    private String banco;

    @Positive
    private double valor;

    @NotNull
    private ParcelaRequest parcela;

    public Item toItem() {
        return Item.builder()
                .nome(this.nome)
                .banco(this.banco)
                .parcela(this.parcela.toParcela())
                .valor(this.valor)
                .valorRestante(this.valor * this.parcela.getQtdRestante())
                .valorTotal(this.valor * (this.parcela.getQtdPaga() + this.parcela.getQtdRestante()))
                .build();
    }

}
