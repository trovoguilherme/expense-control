package br.com.iug.repository;

import br.com.iug.entity.history.ParcelaHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcelaHistoryRepository extends JpaRepository<ParcelaHistory, Long> {



}
