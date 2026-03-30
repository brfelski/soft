package com.felski.soft.service.impl;

import com.felski.soft.domain.entity.ProdutoServico;
import com.felski.soft.domain.entity.QProdutoServico;
import com.felski.soft.domain.enums.TipoItem;
import com.felski.soft.dto.request.ProdutoServicoRequest;
import com.felski.soft.dto.response.ProdutoServicoResponse;
import com.felski.soft.exception.BusinessException;
import com.felski.soft.exception.ResourceNotFoundException;
import com.felski.soft.repository.ItemPedidoRepository;
import com.felski.soft.repository.ProdutoServicoRepository;
import com.felski.soft.service.ProdutoServicoService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ProdutoServicoServiceImpl implements ProdutoServicoService {

    private final ProdutoServicoRepository produtoServicoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    public ProdutoServicoServiceImpl(ProdutoServicoRepository produtoServicoRepository,
            ItemPedidoRepository itemPedidoRepository) {
        this.produtoServicoRepository = produtoServicoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    @Override
    public Page<ProdutoServicoResponse> listarTodos(Boolean apenasAtivos, TipoItem tipoItem, Pageable pageable) {
        BooleanBuilder predicate = new BooleanBuilder();
        QProdutoServico q = QProdutoServico.produtoServico;

        if (Boolean.TRUE.equals(apenasAtivos)) {
            predicate.and(q.ativo.isTrue());
        }
        if (tipoItem != null) {
            predicate.and(q.tipoItem.eq(tipoItem));
        }

        return produtoServicoRepository.findAll(predicate, pageable)
                .map(ProdutoServicoResponse::from);
    }

    @Override
    public ProdutoServicoResponse buscarPorId(UUID id) {
        return ProdutoServicoResponse.from(buscarEntidadeOuLancar(id));
    }

    @Override
    @Transactional
    public ProdutoServicoResponse criar(ProdutoServicoRequest request) {
        ProdutoServico entity = new ProdutoServico(request.nome(), request.preco(), request.tipoItem());
        return ProdutoServicoResponse.from(produtoServicoRepository.save(entity));
    }

    @Override
    @Transactional
    public ProdutoServicoResponse atualizar(UUID id, ProdutoServicoRequest request) {
        ProdutoServico entity = buscarEntidadeOuLancar(id);
        entity.setNome(request.nome());
        entity.atualizarPreco(request.preco());
        entity.setTipoItem(request.tipoItem());
        entity.setTipoItem(request.tipoItem());
        return ProdutoServicoResponse.from(produtoServicoRepository.save(entity));
    }

    @Override
    @Transactional
    public void deletar(UUID id) {
        ProdutoServico entity = buscarEntidadeOuLancar(id);

        if (itemPedidoRepository.existsByProdutoServicoId(id)) {
            throw new BusinessException(
                    "Produto/Serviço '%s' não pode ser excluído pois está vinculado a pedidos existentes."
                            .formatted(entity.getNome()));
        }

        produtoServicoRepository.delete(entity);
    }

    @Override
    @Transactional
    public ProdutoServicoResponse desativar(UUID id) {
        ProdutoServico entity = buscarEntidadeOuLancar(id);
        entity.setAtivo(false);
        return ProdutoServicoResponse.from(produtoServicoRepository.save(entity));
    }

    private ProdutoServico buscarEntidadeOuLancar(UUID id) {
        return produtoServicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ProdutoServico", id));
    }
}
