package br.com.iug.entity;

import br.com.iug.entity.enums.Status;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    @Column(name = "ID_ITEM")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "NOME")
    private String nome;

    @Setter
    @OneToOne
    @JoinColumn(name = "ID_PAGAMENTO")
    private Pagamento pagamento;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ID_PARCELA")
    private Parcela parcela;

    @Column(name = "VALOR")
    private double valor;

    @Column(name = "VALOR_RESTANTE")
    private double valorRestante;

    @Column(name = "VALOR_TOTAL")
    private double valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private Status status;

    @Column(name = "PAGO_NO_MES")
    private boolean pagoNoMes = false;

    @Column(name = "CRIADO_EM")
    @CreationTimestamp
    private LocalDateTime criadoEm;

    public boolean isPay() {
        return this.valorRestante == 0;
    }

    public void pay() {
        if (!pagoNoMes) {
            if (this.parcela != null) {
                this.parcela.pay();
                this.valorRestante = this.valor * this.parcela.getQtdRestante();
                this.pagoNoMes = true;
            } else {
                this.valorRestante = 0;
            }
        }
    }

    public void unpay() {
        this.parcela.unpay();
        this.valorRestante = this.valor * this.parcela.getQtdRestante();
    }

    public void finished() {
        status = Status.FINALIZADO;
    }

    public void payingThisMonth() {
        this.pagoNoMes = false;
    }

    public void update(Item item) {
        this.nome = item.getNome();
        this.pagamento.update(item.getPagamento());
        this.valor = item.getValor();
        this.status = item.getStatus();
        if (this.parcela != null) {
            this.parcela.update(item.getParcela());
            this.valorRestante = this.valor * this.parcela.getQtdRestante();
            this.valorTotal = this.valor * (this.parcela.getQtdRestante() + this.parcela.getQtdPaga());
        } else {
            this.valorRestante = item.getValorRestante();
            this.valorTotal = item.getValorRestante();
        }

    }

}
