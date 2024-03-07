package br.com.iug.dto.request;

import br.com.iug.entity.Pagamento;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PagamentoRequest {

    @NotBlank
    private String nome;

    public Pagamento toPagamento() {
        return Pagamento.builder()
                .nome(nome)
                .build();
    }

}
