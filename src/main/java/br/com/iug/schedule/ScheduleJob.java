package br.com.iug.schedule;

import br.com.iug.entity.Item;
import br.com.iug.entity.enums.Status;
import br.com.iug.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleJob {

    private final ItemRepository itemRepository;

    @Scheduled(cron = "0 0 0 1 * *")
    public void resetar() {
        log.info("Resetando pagamentos..., {}", LocalDateTime.now());

        var itens = itemRepository.findAllByStatusAndPagoNoMesIsTrue(Status.ATIVO);

        log.info("Resetando: {} itens", itens.size());
        itens.forEach(Item::payingThisMonth);

        itemRepository.saveAll(itens);
    }

}
