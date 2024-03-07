package br.com.iug.dto.response;

import br.com.iug.entity.Item;
import br.com.iug.entity.enums.Status;
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

    private PagamentoResponse pagamento;

    private ParcelaResponse parcela;

    private double valor;

    private double valorRestante;

    private double valorTotal;

    private Status status;

    private boolean pagoNoMes;

    public static ItemResponse from(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .nome(item.getNome())
                .pagamento(item.getPagamento().toPagamentoResponse())
                .parcela(item.getParcela() != null ? item.getParcela().toParcelaRespone() : null)
                .valor(item.getValor())
                .valorRestante(item.getValorRestante())
                .valorTotal(item.getValorTotal())
                .status(item.getStatus())
                .pagoNoMes(item.isPagoNoMes())
                .build();
    }

}
