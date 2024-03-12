package br.com.iug.service;

import br.com.iug.entity.Item;
import br.com.iug.entity.history.ItemHistory;
import br.com.iug.exception.ItemNotFoundException;
import br.com.iug.repository.ItemHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemHistoryService {

    private final ItemHistoryRepository itemHistoryRepository;

    public ItemHistory save(Item item) {
        return itemHistoryRepository.save(ItemHistory.from(item));
    }

    public List<ItemHistory> findAll(String banco) {
        if (banco != null) {
            return itemHistoryRepository.findAllByPagamentoNome(banco);
        }
        return itemHistoryRepository.findAll();
    }

    public ItemHistory findByName(String nome) throws ItemNotFoundException {
        return itemHistoryRepository.findByNome(nome).orElseThrow(() -> new ItemNotFoundException("Histórico do item "+nome+" não encontrado"));
    }

    public ItemHistory findById(long id) throws ItemNotFoundException {
        return itemHistoryRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Histórico do item "+id+" não encontrado"));
    }
}
