package br.com.iug.repository;

import br.com.iug.entity.history.ItemHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemHistoryRepository extends JpaRepository<ItemHistory, Long> {



}
