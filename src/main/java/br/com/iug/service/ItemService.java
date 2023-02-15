package br.com.iug.service;

import br.com.iug.entity.Item;
import br.com.iug.entity.enums.Banco;
import br.com.iug.entity.request.ItemRequest;
import br.com.iug.exception.ItemNotFoundException;
import br.com.iug.repository.ItemRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemHistoryService itemHistoryService;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${topic.name}")
    private String topicName;

    public List<Item> findAllWithParams(String nome, String banco) {
        return itemRepository.findAllWithParams(nome, banco);
    }

    public Item findById(long id) throws ItemNotFoundException {
        return itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Item não encontrado!"));
    }

    public double getTotalValue(Banco banco, List<Long> idItens) {
        return itensSumByParam(banco, idItens);
    }

    @Transactional
    public Item save(Item itemRequest) {
        var itemSaved = itemRepository.save(itemRequest);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            kafkaTemplate.send(topicName, objectMapper.writeValueAsString(itemSaved));
            log.info("m=saveItem, id={}", itemSaved.getId());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return itemSaved;
    }

    public Item update(long id, ItemRequest itemRequest) throws ItemNotFoundException {
        var itemFound = findById(id);

        itemFound.update(itemRequest.toItem());

        return itemRepository.save(itemFound);
    }

    public void pay(long id) throws ItemNotFoundException {
        var itemFound = findById(id);
        payItemOrSaveInHistory(itemFound);
    }

    public void payByBanco(Banco banco) throws ItemNotFoundException {
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
        item.pay();

        if (item.getParcela() != null) {
            if (item.isPay()) {
                itemHistoryService.save(item);
                itemRepository.deleteById(item.getId());
            } else {
                itemRepository.save(item);
            }
        } else {
            itemHistoryService.save(item);
            itemRepository.deleteById(item.getId());
        }

    }

    private double itensSumByParam(Banco banco, List<Long> idItens) {
        if (banco != null && idItens != null) {
            return Stream.concat(itemRepository.findAllByBanco(banco).stream(),
                            itemRepository.findAllByIdIn(idItens).stream())
                    .distinct().mapToDouble(Item::getValor).sum();

        } else if (banco != null && idItens == null) {
            return itemRepository.findAllByBanco(banco).stream().mapToDouble(Item::getValor).sum();

        } else if (banco == null && idItens != null) {
            return itemRepository.findAllByIdIn(idItens).stream().mapToDouble(Item::getValor).sum();

        } else {
            return itemRepository.findAll().stream().mapToDouble(Item::getValor).sum();
        }
    }

}
