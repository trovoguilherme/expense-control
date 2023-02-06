package br.com.iug.integration.helper.db;

import br.com.iug.entity.Item;
import br.com.iug.entity.Parcela;
import br.com.iug.exception.ItemNotFoundException;
import br.com.iug.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


@Component
@Profile("test")
@RequiredArgsConstructor
public class ItemDBHelper {

    @Autowired
    private ItemRepository itemRepository;


    public Item findByNameOrCreate(String nome) {
        return itemRepository.findByNome(nome).orElseGet(() -> {
            return itemRepository.save(generateItem(nome));
        });
    }

    public Item findByNameOrCreateWithoutParcela(String nome) {
        return itemRepository.findByNome(nome).orElseGet(() -> {
            return itemRepository.save(generateItemWithoutParcela(nome));
        });
    }

    public Item findById(long id) throws ItemNotFoundException {
        return itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Item "+id+" não encontrado"));
    }

    public List<Item> findAllByIdIn(List<Long> ids) {
        return itemRepository.findAllByIdIn(ids);
    }

//    @Transactional
    public List<Item> createItens() {
        return itemRepository.saveAll(generateItens());
    }

    private Item generateItem(String nome) {
        return new Item(1, nome, "NUBANK", new Parcela(1, 2, 2), 100, 100, 300, LocalDateTime.now());
    }

    private Item generateItemWithoutParcela(String nome) {
        return new Item(1, nome, "NUBANK", null, 100, 100, 300, LocalDateTime.now());
    }

    private List<Item> generateItens() {
        return List.of(
                Item.builder().nome("GPU").banco("NUBANK").valor(100).parcela(Parcela.builder().qtdRestante(2).qtdRestante(2).build()).build(),
                Item.builder().nome("Steam").banco("NUBANK").valor(350).parcela(null).build(),
                Item.builder().nome("Skate").banco("NUBANK").valor(200).parcela(Parcela.builder().qtdRestante(2).qtdRestante(2).build()).build(),
                Item.builder().nome("Shape").banco("ITAU").valor(900).parcela(Parcela.builder().qtdRestante(2).qtdRestante(3).build()).build()
        );
    }

}
