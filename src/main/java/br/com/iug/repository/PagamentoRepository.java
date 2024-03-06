package br.com.iug.repository;

import br.com.iug.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {

    boolean existsByNome(String nome);

    Optional<Pagamento> findByNome(String nome);

}
