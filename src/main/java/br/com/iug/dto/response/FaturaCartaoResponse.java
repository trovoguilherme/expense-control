package br.com.iug.dto.response;

import br.com.iug.entity.FaturaCartao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class FaturaCartaoResponse {

    private final PagamentoResponse pagamentoResponse;

    private final double valor;

    private final LocalDateTime criadoEm;

    public static FaturaCartaoResponse from(FaturaCartao faturaCartao) {
        return new FaturaCartaoResponse(faturaCartao.getPagamento().toPagamentoResponse(), faturaCartao.getValor(), faturaCartao.getCriadoEm());
    }

}
