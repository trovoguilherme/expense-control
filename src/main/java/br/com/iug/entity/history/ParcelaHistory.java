package br.com.iug.entity.history;

import br.com.iug.entity.Parcela;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "PARCELA_HISTORY")
public class ParcelaHistory {

    @Id
    private long id;

    @Column(name = "VALOR")
    private double valor;

    @Column(name = "QTD_PAGA")
    private int qtdPaga;

    @Column(name = "QTD_RESTANTE")
    private int qtdRestante;

    public static ParcelaHistory from(Parcela parcela) {
        return ParcelaHistory.builder()
                .id(parcela.getId())
                .valor(parcela.getValor())
                .qtdPaga(parcela.getQtdPaga())
                .qtdRestante(parcela.getQtdRestante())
                .build();
    }

}
