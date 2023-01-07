package br.com.iug.entity;

import br.com.iug.entity.request.ItemRequest;
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
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @Column(name = "VALOR_RESTANTE")
    private double valorRestante;

    @Column(name = "VALOR_TOTAL")
    private double valorTotal;

    @CreationTimestamp
    private LocalDateTime criadoEm;

    public void pay() {
        this.parcela.pay();
        this.valorRestante = this.parcela.getValor() * this.parcela.getQtdRestante();
    }

    public void update(ItemRequest itemRequest) {
        this.nome = itemRequest.getNome();
        this.banco = itemRequest.getBanco();
        this.parcela.update(itemRequest.getParcela());
        this.valorRestante = this.parcela.getValor() * this.parcela.getQtdRestante();
        this.valorTotal = this.valorTotal * (this.parcela.getQtdRestante() + this.parcela.getQtdPaga());
    }

}
