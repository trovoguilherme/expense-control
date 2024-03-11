package br.com.iug.repository;

import br.com.iug.entity.FaturaCartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FaturaCartaoRepository extends JpaRepository<FaturaCartao, Long> {

    Optional<FaturaCartao> findByPagamentoNome(String nomePagamento);

}
