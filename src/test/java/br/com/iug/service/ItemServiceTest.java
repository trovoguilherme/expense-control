package br.com.iug.service;

import br.com.iug.entity.Item;
import br.com.iug.entity.Pagamento;
import br.com.iug.entity.Parcela;
import br.com.iug.entity.enums.Status;
import br.com.iug.dto.request.ItemRequest;
import br.com.iug.dto.request.PagamentoRequest;
import br.com.iug.dto.request.ParcelaRequest;
import br.com.iug.exception.ItemNotFoundException;
import br.com.iug.exception.ItemNotUpdateParcelaException;
import br.com.iug.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @InjectMocks
    private ItemService itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private PagamentoService pagamentoService;

    @Mock
    private ItemHistoryService itemHistoryService;

    @Captor
    private ArgumentCaptor<Item> captor;

    @Test
    @DisplayName("Deve pagar um item com parcela")
    void shouldPayItemWithParcela() throws ItemNotFoundException {
        final Item item = generateItens().get(2);
        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(item));

        itemService.pay(item.getId());

        assertEquals(100, item.getValorRestante());
    }
    @Test
    @DisplayName("Deve pagar um item sem parcela")
    void shouldPayItemWithoutParcela() throws ItemNotFoundException {
        final Item item = generateItens().get(1);

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(item));
        doNothing().when(itemRepository).deleteById(any(Long.class));

        itemService.pay(item.getId());

        assertEquals(0, item.getValorRestante());
    }

    @Test
    @DisplayName("Deve pagar itens com banco NUBANK")
    void shouldPayItensByBanco() throws ItemNotFoundException {
        List<Item> itens = generateItens();
        final String nomePagemto = "NUBANK";

        when(pagamentoService.findByNome(anyString())).thenReturn(generatePagamento(nomePagemto));
        when(itemRepository.findAllByPagamento(any(Pagamento.class))).thenReturn(itens);

        itemService.payByBanco(nomePagemto);

        assertEquals(0, itens.get(0).getValorRestante());
    }

    @Test
    @DisplayName("Deve lançar uma exceção quando não encontrar itens pelo banco")
    void shouldThrowItemNotFoundExceptionWhenNotFoudItensByBanco() throws ItemNotFoundException {
        when(pagamentoService.findByNome(anyString())).thenReturn(generatePagamento("NUBANK"));
        when(itemRepository.findAllByPagamento(any(Pagamento.class))).thenReturn(List.of());

        assertThrows(ItemNotFoundException.class, () -> itemService.payByBanco("NUBANK"));
    }

    @Test
    @DisplayName("Deve deletar um item pelo id")
    void shouldDeleteItemById() throws ItemNotFoundException {
        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(new Item()));

        itemService.deleteById(1L);

        verify(itemRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deve salvar um item")
    void shouldSaveItem() {
        final ItemRequest itemRequest = new ItemRequest("Skate", new PagamentoRequest("NUBANK"), 1200, new ParcelaRequest(1 ,2), Status.ATIVO);

        when(pagamentoService.findByNome(anyString())).thenReturn(generatePagamento("NUBANK"));

        itemService.save(itemRequest.toItem());

        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    @DisplayName("Deve atualizar um item que tem parcela")
    void shouldUpdateItemWithParcela() throws ItemNotFoundException, ItemNotUpdateParcelaException {
        var item = generateItens().get(0);
        var itemRequest = new ItemRequest("item novo", new PagamentoRequest("NUBANK"), 3000, new ParcelaRequest(9, 3), Status.ATIVO);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        itemService.update(item.getId(), itemRequest);

        verify(itemRepository).save(captor.capture());
        var itemAtual = captor.getValue();

        assertThat(itemAtual.getNome()).isEqualTo("item novo");
        assertThat(itemAtual.getValorRestante()).isEqualTo(9000);
    }

    @Test
    @DisplayName("Deve atualizar um item sem parcela")
    void shouldUpdateItemWithoutParcela() throws ItemNotFoundException, ItemNotUpdateParcelaException {
        var item = generateItens().get(1);
        var itemRequest = new ItemRequest("item novo", new PagamentoRequest("NUBANK"), 3000, null, Status.ATIVO);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        itemService.update(item.getId(), itemRequest);

        verify(itemRepository).save(captor.capture());
        var itemAtual = captor.getValue();

        assertThat(itemAtual.getNome()).isEqualTo("item novo");
        assertThat(itemAtual.getValorRestante()).isEqualTo(3000);
        assertThat(itemAtual.getValorTotal()).isEqualTo(3000);
        assertThat(itemAtual.getValor()).isEqualTo(3000);
        assertThat(itemAtual.getParcela()).isNull();
    }

    @Test
    @DisplayName("Deve pegar o valor restante total de todos os itens")
    void shouldGetValorRestanteTotal() {
        when(itemRepository.findAll()).thenReturn(generateItens());

        var valorRestanteTotal = itemService.getSumParcelasByMonth(null, null);

        assertThat(valorRestanteTotal).isEqualTo(450);
    }

    @Test
    @DisplayName("Deve pegar o valor restante total de todos os itens pelo banco")
    void shouldGetValorRestanteTotalByBanco() {
        List<Item> mutableItens = new ArrayList<>(generateItens());
        mutableItens.remove(3);

        when(pagamentoService.findByNome(anyString())).thenReturn(generatePagamento("NUBANK"));
        when(itemRepository.findAllByPagamento(any(Pagamento.class))).thenReturn(mutableItens);

        var valorRestanteTotal = itemService.getSumParcelasByMonth("NUBANK", null);

        assertThat(valorRestanteTotal).isEqualTo(350);
    }

    @Test
    @DisplayName("Deve retornar o valor dos itens somados pelos ids informado")
    void shouldGetItemValorSumByIds() {
        List<Item> mutableItens = new ArrayList<>(generateItens());
        mutableItens.remove(1);

        when(itemRepository.findAllByIdIn(anyList())).thenReturn(mutableItens);

        var actualValor = itemService.getSumParcelasByMonth(null, List.of(1L, 3L, 4L));

        assertThat(actualValor).isEqualTo(300);
    }

    private List<Item> generateItens() {
        return List.of(
                new Item(1, "GPU", new Pagamento(1, "NUBANK", LocalDateTime.now()), new Parcela(1, 2, 1), 100, 100, 300, Status.ATIVO, false, LocalDateTime.now()),
                new Item(2, "Steam", new Pagamento(1, "NUBANK", LocalDateTime.now()), null, 150, 150, 150, Status.ATIVO, false, LocalDateTime.now()),
                new Item(3, "Skate", new Pagamento(1, "NUBANK", LocalDateTime.now()), new Parcela(2, 2, 2), 100, 200, 400, Status.ATIVO, false, LocalDateTime.now()),
                new Item(4, "Shape", new Pagamento(1, "ITAU", LocalDateTime.now()), new Parcela(1, 2, 3), 100, 300, 500, Status.ATIVO, false, LocalDateTime.now())
        );
    }

    private Pagamento generatePagamento(String nome) {
        return new Pagamento(1, nome, LocalDateTime.now());
    }

}
