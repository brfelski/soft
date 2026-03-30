package com.felski.soft.repository;

import com.felski.soft.domain.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.UUID;

public interface ItemPedidoRepository
        extends JpaRepository<ItemPedido, UUID>,
                QuerydslPredicateExecutor<ItemPedido> {

    boolean existsByProdutoServicoId(UUID produtoServicoId);
}
