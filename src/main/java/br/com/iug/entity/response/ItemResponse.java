package br.com.iug.entity.response;

import br.com.iug.entity.Banco;
import br.com.iug.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {

    private long id;

    private String nome;

    private String banco;

    private ParcelaResponse parcela;

    private double valorRestante;

    private double valorTotal;

    public static ItemResponse from(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .nome(item.getNome())
                .banco(item.getBanco())
                .parcela(item.getParcela().toParcelaRespone())
                .valorRestante(item.getValorRestante())
                .valorTotal(item.getValorTotal())
                .build();
    }

}
