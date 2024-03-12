package br.com.iug.repository;

import br.com.iug.entity.history.ItemHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemHistoryRepository extends JpaRepository<ItemHistory, Long> {

    Optional<ItemHistory> findByNome(String nome);

    List<ItemHistory> findAllByPagamentoNome(String banco);

}
