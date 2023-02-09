package br.com.iug.entity.response;

import br.com.iug.entity.Item;
import br.com.iug.entity.enums.Banco;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id", "nome", "banco", "valor", "parcela", "valorRestante", "valorTotal"})
public class ItemResponse {

    private long id;

    private String nome;

    private Banco banco;

    private ParcelaResponse parcela;

    private double valor;

    private double valorRestante;

    private double valorTotal;

    public static ItemResponse from(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .nome(item.getNome())
                .banco(item.getBanco())
                .parcela(item.getParcela() != null ? item.getParcela().toParcelaRespone() : null)
                .valor(item.getValor())
                .valorRestante(item.getValorRestante())
                .valorTotal(item.getValorTotal())
                .build();
    }

}
