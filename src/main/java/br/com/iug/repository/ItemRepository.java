package br.com.iug.repository;

import br.com.iug.entity.Item;
import br.com.iug.entity.Pagamento;
import br.com.iug.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

//    @Query("select i from Item i where (:nome is null or i.nome = :nome) and (:banco is null or i.banco = :banco) and (:status is null or i.status = :status) ")
//    List<Item> findAllWithParams(String nome, String banco, Status status);

    List<Item> findAllByPagamento(Pagamento pagamento);

    Optional<Item> findByNome(String nome);

    List<Item> findAllByIdIn(List<Long> ids);

    List<Item> findAllByStatusAndPagoNoMesIsTrue(Status status);

}
