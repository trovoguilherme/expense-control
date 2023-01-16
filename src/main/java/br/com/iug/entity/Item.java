package br.com.iug.entity;

import br.com.iug.entity.request.ItemRequest;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ITEM")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "NOME")
    private String nome;

//    @Enumerated(EnumType.STRING)
    @Column(name = "banco")
    private String banco;

    @OneToOne(cascade = CascadeType.ALL)
    private Parcela parcela;

    @Column(name = "VALOR")
    private double valor;

    @Column(name = "VALOR_RESTANTE")
    private double valorRestante;

    @Column(name = "VALOR_TOTAL")
    private double valorTotal;

    @CreationTimestamp
    private LocalDateTime criadoEm;

    public void pay() {
        this.parcela.pay();
        this.valorRestante = this.valor * this.parcela.getQtdRestante();
    }

    public void unpay() {
        this.parcela.unpay();
        this.valorRestante = this.valor * this.parcela.getQtdRestante();
    }

    public void update(Item item) {
        this.nome = item.getNome();
        this.banco = item.getBanco();
        if (this.parcela != null) {
            this.parcela.update(item.getParcela());
            this.valorRestante = this.valor * this.parcela.getQtdRestante();
            this.valorTotal = this.valor * (this.parcela.getQtdRestante() + this.parcela.getQtdPaga());
        } else {
            this.valor = item.getValor();
            this.valorRestante = item.getValorRestante();
            this.valorTotal = item.getValorRestante();
        }

    }

}
