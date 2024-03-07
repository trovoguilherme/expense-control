package br.com.iug.service;

import br.com.iug.entity.Item;
import br.com.iug.entity.Parcela;
import br.com.iug.entity.enums.Status;
import br.com.iug.entity.request.ItemRequest;
import br.com.iug.entity.request.ParcelaRequest;
import br.com.iug.exception.ItemNotFoundException;
import br.com.iug.exception.ItemNotUpdateParcelaException;
import br.com.iug.exception.NotFoundException;
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

    private  final PagamentoService pagamentoService;

    public List<Item> findAllWithParams(String nome, String banco, Status status) {
        return itemRepository.findAll();
    }

    public Item findById(long id) throws ItemNotFoundException {
        return itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Item não encontrado!"));
    }

    public double getSumParcelasByMonth(String pagamento, List<Long> idItens) {
        return getItens(pagamento, idItens)
                .stream().mapToDouble(Item::getValor).sum();
    }
    public Double getSumParcelasToPayOff(String pagamento, List<Long> idItens) {
        return getItens(pagamento, idItens)
                .stream().mapToDouble(Item::getValorRestante).sum();
    }

    public Item save(Item itemRequest) throws NotFoundException {
        if (!pagamentoService.existsByNome(itemRequest.getPagamento().getNome())) {
            throw new NotFoundException("O pagamento não existe");
        }
        return itemRepository.save(itemRequest);
    }

    public Item update(long id, ItemRequest itemRequest) throws ItemNotFoundException, ItemNotUpdateParcelaException {
        var itemFound = findById(id);

        verifyUpdateParcela(itemFound.getParcela(), itemRequest.getParcela());

        itemFound.update(itemRequest.toItem());

        return itemRepository.save(itemFound);
    }

    public Item pay(long id) throws ItemNotFoundException {
        var itemFound = findById(id);
        payItemOrSaveInHistory(itemFound);
        return itemFound;
    }

    public List<Item> payByBanco(String pagamento) throws ItemNotFoundException {
        var itens = itemRepository.findAllByPagamento(pagamentoService.findByNome(pagamento));

        if (itens.isEmpty()) {
            throw new ItemNotFoundException("Itens com o pagamento "+pagamento+" não encontrado!");
        }

        itens.forEach(this::payItemOrSaveInHistory);

        return itens;
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

    private List<Item> getItens(String pagamento, List<Long> idItens) {
        List<Item> itens;

        if (pagamento != null && idItens != null) {
            itens = Stream.concat(
                    itemRepository.findAllByPagamento(pagamentoService.findByNome(pagamento)).stream(),
                    itemRepository.findAllByIdIn(idItens).stream()
            ).toList();

        } else if (pagamento != null) {
            itens =  itemRepository.findAllByPagamento(pagamentoService.findByNome(pagamento));

        } else if (idItens != null) {
            itens = itemRepository.findAllByIdIn(idItens);

        } else {
            itens = itemRepository.findAll();
        }

        return itens;
    }

    private void verifyUpdateParcela(Parcela parcela, ParcelaRequest parcelaRequest) throws ItemNotUpdateParcelaException {
        if (parcela == null && parcelaRequest != null) {
            throw new ItemNotUpdateParcelaException("Não é permitido adicionar parcela nesse item");
        } else if (parcela != null && parcelaRequest == null) {
            throw new ItemNotUpdateParcelaException("Não é permitido retirar parcela desse item");
        }
    }

}
