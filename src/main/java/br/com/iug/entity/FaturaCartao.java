package br.com.iug.entity;

import br.com.iug.dto.request.FaturaCartaoRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "FATURA_CARTAO")
public class FaturaCartao {

    @Id
    @Column(name = "ID_FATURA_CARTAO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    @Column(name = "VALOR")
    private double valor;

    @Column(name = "CRIADO_EM")
    @CreationTimestamp
    private LocalDateTime criadoEm;

    @Setter
    @OneToOne
    @JoinColumn(name = "ID_PAGAMENTO")
    private Pagamento pagamento;

    public static FaturaCartao from(FaturaCartaoRequest faturaCartaoRequest) {
        return FaturaCartao.builder()
                .pagamento(faturaCartaoRequest.getPagamento().toPagamento())
                .valor(faturaCartaoRequest.getValor())
                .build();
    }

}
