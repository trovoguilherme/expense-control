package br.com.iug.repository;

import br.com.iug.entity.Item;
import br.com.iug.entity.enums.Banco;
import br.com.iug.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("select i from Item i where (:nome is null or i.nome = :nome) and (:banco is null or i.banco = :banco) and (:status is null or i.status = :status) ")
    List<Item> findAllWithParams(String nome, String banco, Status status);

    List<Item> findAllByBanco(Banco banco);

    Optional<Item> findByNome(String nome);

    List<Item> findAllByIdIn(List<Long> ids);
}
