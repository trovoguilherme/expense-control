package br.com.iug.entity.history;

import br.com.iug.entity.Item;
import br.com.iug.entity.Pagamento;
import br.com.iug.entity.enums.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ITEM_HISTORY")
public class ItemHistory {

    @Id
    @Column(name = "ID_ITEM_HISTORY")
    private long id;

    @Column(name = "NOME")
    private String nome;

    @OneToOne
    @JoinColumn(name = "ID_PAGAMENTO")
    private Pagamento pagamento;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_PARCELA_HISTORY")
    private ParcelaHistory parcela;

    @Column(name = "VALOR")
    private double valor;

    @Column(name = "VALOR_RESTANTE")
    private double valorRestante;

    @Column(name = "VALOR_TOTAL")
    private double valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private Status status;

    @CreationTimestamp
    @Column(name = "CRIADO_EM")
    private LocalDateTime criadoEm;

    public static ItemHistory from(Item item) {
        return ItemHistory.builder()
                .id(item.getId())
                .nome(item.getNome())
                .pagamento(item.getPagamento())
                .parcela(item.getParcela() != null ? ParcelaHistory.from(item.getParcela()) : null)
                .valor(item.getValor())
                .valorRestante(item.getValorRestante())
                .valorTotal(item.getValorTotal())
                .status(item.getStatus())
                .build();
    }

    public Item toItem() {
        return Item.builder()
                .id(this.id)
                .nome(this.nome)
//                .banco(this.banco)
                .parcela(this.parcela.toParcela())
                .valor(this.valor)
                .valorRestante(this.valorRestante)
                .valorTotal(this.valorTotal)
                .criadoEm(this.criadoEm)
                .build();
    }
}
