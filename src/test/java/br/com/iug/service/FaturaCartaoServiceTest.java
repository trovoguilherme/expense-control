package br.com.iug.service;

import br.com.iug.dto.request.FaturaCartaoUpdate;
import br.com.iug.entity.FaturaCartao;
import br.com.iug.entity.Pagamento;
import br.com.iug.repository.FaturaCartaoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FaturaCartaoServiceTest {

    @InjectMocks
    private FaturaCartaoService faturaCartaoService;

    @Mock
    private FaturaCartaoRepository faturaCartaoRepository;

    @Mock
    private PagamentoService pagamentoService;

    @Captor
    private ArgumentCaptor<FaturaCartao> captor;

    @Test
    @DisplayName("Deve criar uma Fatura de Cartão")
    void save() {
        when(pagamentoService.findByNome(anyString())).thenReturn(generatePagamento());
        faturaCartaoService.save(generateFaturaCartao());
        verify(faturaCartaoRepository, times(1)).save(any(FaturaCartao.class));
    }

    @Test
    @DisplayName("Deve buscar Fatura de Cartão por nome")
    void findAll() {
        when(faturaCartaoRepository.findAll()).thenReturn(List.of(generateFaturaCartao()));
        faturaCartaoService.findAll();
        verify(faturaCartaoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar Fatura de Cartão por nome")
    void findByName() {
        when(faturaCartaoRepository.findByPagamentoNome(anyString())).thenReturn(Optional.of(generateFaturaCartao()));
        faturaCartaoService.findByName("ITAU");
        verify(faturaCartaoRepository, times(1)).findByPagamentoNome(anyString());
    }

    @Test
    @DisplayName("Deve atualizar o somente o valor da Fatura de Cartão")
    void shouldUpdateOnlyValor() {
        final double VALOR = 299.0;
        var faturaCartaoUpdate = generateFaturaCartaoUpdate(VALOR);

        when(faturaCartaoRepository.findByPagamentoNome(anyString())).thenReturn(Optional.of(generateFaturaCartao()));

        faturaCartaoService.patchValor("ITAU", faturaCartaoUpdate);

        verify(faturaCartaoRepository).save(captor.capture());

        assertEquals(VALOR, captor.getValue().getValor());
    }

    private FaturaCartaoUpdate generateFaturaCartaoUpdate(double valor) {
        return new FaturaCartaoUpdate(valor);
    }

    private FaturaCartao generateFaturaCartao() {
        return FaturaCartao.builder()
                .id(1L)
                .valor(555)
                .pagamento(generatePagamento())
                .criadoEm(LocalDateTime.now())
                .build();
    }

    private Pagamento generatePagamento() {
        return Pagamento.builder()
                .id(1L)
                .nome("ITAÚ")
                .criadoEm(LocalDateTime.now())
                .build();
    }

}
