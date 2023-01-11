package br.com.iug.entity;

import br.com.iug.entity.request.ParcelaRequest;
import br.com.iug.entity.response.ParcelaResponse;
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

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PARCELA")
public class Parcela {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "VALOR")
    private double valor;

    @Column(name = "QTD_PAGA")
    private int qtdPaga;

    @Column(name = "QTD_RESTANTE")
    private int qtdRestante;

    public void pay() {
        this.qtdPaga++;
        this.qtdRestante--;
    }

    public boolean isPay() {
        return this.qtdRestante == 0;
    }

    public void update(ParcelaRequest parcelaRequest) {
        this.valor = parcelaRequest.getValor();
        this.qtdPaga = parcelaRequest.getQtdPaga();
        this.qtdRestante = parcelaRequest.getQtdRestante();
    }

    public ParcelaResponse toParcelaRespone() {
        return ParcelaResponse.builder()
                .id(this.id)
                .valor(this.valor)
                .qtdPaga(this.qtdPaga)
                .qtdRestante(this.qtdRestante)
                .build();
    }
}
