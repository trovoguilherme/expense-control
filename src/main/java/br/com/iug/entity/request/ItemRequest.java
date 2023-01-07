package br.com.iug.entity.request;

import br.com.iug.entity.Banco;
import br.com.iug.entity.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemRequest {

    @NotBlank
    private String nome;

    private String banco;

    @NotNull
    private ParcelaRequest parcela;

    public Item toItem() {
        return Item.builder()
                .nome(this.nome)
                .banco(this.banco)
                .parcela(this.parcela.toParcela())
                .valorRestante(this.parcela.getValor() * this.parcela.getQtdRestante())
                .valorTotal(this.parcela.getValor() * (this.parcela.getQtdPaga() + this.parcela.getQtdRestante()))
                .build();
    }

}
