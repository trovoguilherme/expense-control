package br.com.iug.service;

import br.com.iug.entity.Item;
import br.com.iug.entity.Parcela;
import br.com.iug.entity.enums.Banco;
import br.com.iug.entity.enums.Status;
import br.com.iug.entity.request.ItemRequest;
import br.com.iug.entity.request.ParcelaRequest;
import br.com.iug.exception.ItemNotFoundException;
import br.com.iug.exception.ItemNotUpdateParcelaException;
import br.com.iug.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemHistoryService itemHistoryService;

    public List<Item> findAllWithParams(String nome, String banco, Status status) {
        return itemRepository.findAllWithParams(nome, banco, status);
    }

    public Item findById(long id) throws ItemNotFoundException {
        return itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Item não encontrado!"));
    }

    public double getTotalValue(Banco banco, List<Long> idItens) {
        return itensSumByParam(banco, idItens);
    }

    public Item save(Item itemRequest) {
        return itemRepository.save(itemRequest);
    }

    public Item update(long id, ItemRequest itemRequest) throws ItemNotFoundException, ItemNotUpdateParcelaException {
        var itemFound = findById(id);

        verifyUpdateParcela(itemFound.getParcela(), itemRequest.getParcela());

        itemFound.update(itemRequest.toItem());

        return itemRepository.save(itemFound);
    }

    //TODO Adicionar validação para saber se o item está pago no mês e um schedule para resetar esse campo para poder ser pago novamente
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
                item.finished();
                itemHistoryService.save(item);
                itemRepository.deleteById(item.getId());
            } else {
                itemRepository.save(item);
            }
        } else {
            item.finished();
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

    private void verifyUpdateParcela(Parcela parcela, ParcelaRequest parcelaRequest) throws ItemNotUpdateParcelaException {
        if (parcela == null && parcelaRequest != null) {
            throw new ItemNotUpdateParcelaException("Não é permitido adicionar parcela nesse item");
        } else if (parcela != null && parcelaRequest == null) {
            throw new ItemNotUpdateParcelaException("Não é permitido retirar parcela desse item");
        }
    }

}
