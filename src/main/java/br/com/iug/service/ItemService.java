package br.com.iug.service;

import br.com.iug.entity.Item;
import br.com.iug.entity.request.ItemRequest;
import br.com.iug.exception.ItemNotFoundException;
import br.com.iug.repository.ItemRepository;
import jakarta.transaction.Transactional;
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

    public Item findById(long id) throws ItemNotFoundException {
        return itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Item não encontrado!"));
    }

    public double getTotalValue(String banco) {
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

    public void payByParam(String nome, String banco) throws ItemNotFoundException {
        var itens = findAllWithParams(nome, banco);

        if (itens.isEmpty()) {
            if (banco == null && !nome.isBlank()) {
                throw new ItemNotFoundException("Item "+nome+" não encontrado!");
            } else if (nome == null && !banco.isBlank()) {
                throw new ItemNotFoundException("Itens com o banco "+banco+" não encontrado!");
            }
        }

        itens.forEach(i -> {
            i.pay();
            if (i.getParcela().isAllPay()) {
                itemHistoryService.save(i);
                itemRepository.deleteById(i.getId());
            }
        });

        var itensNotPaid = itens.stream().filter(i -> i.getValorRestante() != 0).collect(Collectors.toList());

        itemRepository.saveAll(itensNotPaid);
    }

    public void deleteById(long id) throws ItemNotFoundException {
        findById(id);
        itemRepository.deleteById(id);
    }

}
