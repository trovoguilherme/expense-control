package br.com.iug.service;

import br.com.iug.entity.Item;
import br.com.iug.entity.Parcela;
import br.com.iug.entity.request.ItemRequest;
import br.com.iug.entity.request.ParcelaRequest;
import br.com.iug.exception.ItemNotFoundException;
import br.com.iug.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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
    private ItemHistoryService itemHistoryService;

    @Test
    @DisplayName("Deve pagar um item com parcela")
    void shouldPayItemWithParcela() throws ItemNotFoundException {
        final Item item = generateItens().get(2);
        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(item));

        itemService.pay(item.getId());

        assertEquals(item.getValorRestante(), 100);
    }
    @Test
    @DisplayName("Deve pagar um item sem parcela")
    void shouldPayItemWithoutParcela() throws ItemNotFoundException {
        final Item item = generateItens().get(1);

        when(itemRepository.findById(any(Long.class))).thenReturn(Optional.of(item));
        doNothing().when(itemRepository).deleteById(any(Long.class));

        itemService.pay(item.getId());

        assertEquals(item.getValorRestante(), 0);
    }

    @Test
    @DisplayName("Deve pagar itens com banco NUBANK")
    void shouldPayItensByBanco() throws ItemNotFoundException {
        List<Item> itens = generateItens();

        when(itemRepository.findAllByBanco(anyString())).thenReturn(itens);

        itemService.payByBanco("NUBANK");

        assertEquals(itens.get(0).getValorRestante(), 0);
    }

    @Test
    @DisplayName("Deve lançar uma exceção quando não encontrar itens pelo banco")
    void shouldThrowItemNotFoundExceptionWhenNotFoudItensByBanco() throws ItemNotFoundException {
        when(itemRepository.findAllByBanco(anyString())).thenReturn(List.of());

        assertThrows(ItemNotFoundException.class, () -> itemService.payByBanco("BRADESCO"));
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
        final ItemRequest itemRequest = new ItemRequest("Skate", "ITAU", 1200, new ParcelaRequest(1 ,2));

        itemService.save(itemRequest.toItem());

        verify(itemRepository, times(1)).save(any(Item.class));
    }

    private List<Item> generateItens() {
        return List.of(
                new Item(1, "GPU", "NUBANK", new Parcela(1, 2, 1), 100, 100, 300, LocalDateTime.now()),
                new Item(2, "Steam", "NUBANK", null, 150, 150, 150, LocalDateTime.now()),
                new Item(3, "Skate", "NUBANK", new Parcela(2, 2, 2), 100, 200, 400, LocalDateTime.now()),
                new Item(4, "Shape", "ITAU", new Parcela(1, 2, 3), 100, 300, 500, LocalDateTime.now())
        );
    }

}
