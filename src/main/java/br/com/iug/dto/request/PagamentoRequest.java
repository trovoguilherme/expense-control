package br.com.iug.dto.request;

import br.com.iug.entity.Pagamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Pattern(regexp = "[A-Z0-9]+")
    private String nome;

    public Pagamento toPagamento() {
        return Pagamento.builder()
                .nome(nome)
                .build();
    }

}
