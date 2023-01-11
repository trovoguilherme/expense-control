package br.com.iug.service;

import br.com.iug.entity.Banco;
import br.com.iug.entity.Item;
import br.com.iug.entity.request.ItemRequest;
import br.com.iug.exception.BancoNotFoundException;
import br.com.iug.exception.ItemNotFoundException;
import br.com.iug.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemHistoryService itemHistoryService;

    public List<Item> findAllWithParams(String nome, String banco) {
        return itemRepository.findAllWithParams(nome, banco);
    }

    public Item findByNome(String nome) throws ItemNotFoundException {
        return itemRepository.findByNome(nome).orElseThrow(() -> new ItemNotFoundException("Item não encontrado!"));
    }

    public Item findById(long id) throws ItemNotFoundException {
        return itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Item não encontrado!"));
    }

    public double getTotalValue(String banco) throws BancoNotFoundException {

        try {
            if (banco != null && Banco.BANCO_ID_MAPPING.containsValue(Banco.valueOf(banco))) {
                return itemRepository.findAllByBanco(banco).stream().mapToDouble(Item::getValorRestante).sum();
            }
        } catch (IllegalArgumentException e) {
            throw new BancoNotFoundException("Banco '"+banco+"' não encontrado");
        }
        return itemRepository.findAll().stream().mapToDouble(Item::getValorRestante).sum();
    }

    public Item save(Item itemRequest) {
        return itemRepository.save(itemRequest);
    }

    public Item update(long id, ItemRequest itemRequest) throws ItemNotFoundException {
        var itemFound = findById(id);

        itemFound.update(itemRequest);

        return itemRepository.save(itemFound);
    }

    public void pay(String nome) throws ItemNotFoundException {
        var itemFound = findByNome(nome);
        payItemOrSaveInHistory(itemFound);
    }

    public void payByBanco(String banco) throws ItemNotFoundException {
        var itens = itemRepository.findAllByBanco(banco);

        if (itens.isEmpty()) {
            throw new ItemNotFoundException("Itens com o banco "+banco+" não encontrado!");
        }

        itens.forEach(this::payItemOrSaveInHistory);
    }

    public void deleteById(long id) throws ItemNotFoundException {
        findById(id);
        itemRepository.deleteById(id);
    }

    private void payItemOrSaveInHistory(Item item) {
        item.getParcela().pay();

        if (item.getParcela().isPay()) {
            itemHistoryService.save(item);
            itemRepository.deleteById(item.getId());
        } else {
            itemRepository.save(item);
        }
    }

}
