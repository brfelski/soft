package com.felski.soft.repository;

import com.felski.soft.domain.entity.Pedido;
import com.felski.soft.domain.enums.StatusPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PedidoRepository
        extends JpaRepository<Pedido, UUID>,
                QuerydslPredicateExecutor<Pedido> {

    List<Pedido> findByStatusPedido(StatusPedido statusPedido);

    @Query("SELECT p FROM Pedido p LEFT JOIN FETCH p.itens i LEFT JOIN FETCH i.produtoServico WHERE p.id = :id")
    Optional<Pedido> findByIdWithItens(UUID id);
}
