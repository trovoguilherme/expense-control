package br.com.iug.entity;

import br.com.iug.dto.response.PagamentoResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "PAGAMENTO")
public class Pagamento {

    @Id
    @Column(name = "ID_PAGAMENTO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "CRIADO_EM")
    @CreationTimestamp
    private LocalDateTime criadoEm;

    public void update(Pagamento pagamento) {
        this.nome = pagamento.getNome();
    }

    public PagamentoResponse toPagamentoResponse() {
        return PagamentoResponse.builder()
                .nome(nome)
                .build();
    }

}
