package br.com.iug.service;

import br.com.iug.entity.Item;
import br.com.iug.entity.history.ItemHistory;
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

    public List<ItemHistory> findAll() {
        return itemHistoryRepository.findAll();
    }
    
}
