package com.felski.soft.repository;

import com.felski.soft.domain.entity.ProdutoServico;
import com.felski.soft.domain.enums.TipoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.UUID;

public interface ProdutoServicoRepository
        extends JpaRepository<ProdutoServico, UUID>,
                QuerydslPredicateExecutor<ProdutoServico> {

    List<ProdutoServico> findByAtivoTrue();

    List<ProdutoServico> findByTipoItem(TipoItem tipoItem);

    List<ProdutoServico> findByAtivoTrueAndTipoItem(TipoItem tipoItem);
}
