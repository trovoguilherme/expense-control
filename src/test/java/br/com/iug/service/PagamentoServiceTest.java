package br.com.iug.service;

import br.com.iug.entity.Pagamento;
import br.com.iug.repository.PagamentoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceTest {

    @InjectMocks
    private PagamentoService pagamentoService;

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Test
    @DisplayName("Deve criar um Pagamento")
    void save() {
        pagamentoService.save(generatePagamento());
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));
    }

    @Test
    @DisplayName("Deve buscar todos os Pagamentos")
    void findAll() {
        pagamentoService.findAll();
        verify(pagamentoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar o Pagamento pelo nome")
    void findByName() {
        when(pagamentoRepository.findByNome(anyString())).thenReturn(Optional.of(generatePagamento()));
        pagamentoService.findByNome("NUBANK");
        verify(pagamentoRepository, times(1)).findByNome(anyString());
    }

    @Test
    @DisplayName("Deve apagar o Pagamento pelo nome")
    void deleteByName() {
        when(pagamentoRepository.findByNome(anyString())).thenReturn(Optional.of(generatePagamento()));
        doNothing().when(pagamentoRepository).delete(any(Pagamento.class));

        pagamentoService.deleteByName("NUBANK");
        verify(pagamentoRepository, times(1)).delete(any(Pagamento.class));
    }

    private Pagamento generatePagamento() {
        return Pagamento.builder()
                .id(1L)
                .nome("ITAÚ")
                .criadoEm(LocalDateTime.now())
                .build();
    }

}
