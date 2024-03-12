package br.com.iug.dto.request;

import br.com.iug.entity.Item;
import br.com.iug.entity.enums.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static br.com.iug.utility.JavaCodes.ERROR_ITEM_MENOR_QUE_ZERO;
import static br.com.iug.utility.JavaCodes.ERROR_ITEM_NOME_VAZIO;


@Getter
@AllArgsConstructor
public class ItemRequest {

    @NotBlank(message = "{"+ ERROR_ITEM_NOME_VAZIO +"}")
    private String nome;

    @Valid
    private PagamentoRequest pagamento;

    @Positive(message = "{" + ERROR_ITEM_MENOR_QUE_ZERO + "}")
    private double valor;

    @Valid
    private ParcelaRequest parcela;
    
    private Status status;

    public Item toItem() {
        return Item.builder()
                .nome(this.nome)
                .pagamento(pagamento.toPagamento())
                .parcela(this.parcela != null ? this.parcela.toParcela() : null)
                .valor(this.valor)
                .status(this.status)
                .valorRestante(this.parcela != null ? this.valor * this.parcela.getQtdRestante() : this.valor)
                .valorTotal(this.parcela != null ? this.valor * (this.parcela.getQtdPaga() + this.parcela.getQtdRestante()) : this.valor)
                .build();
    }

}
