package com.felski.soft.service.impl;

import com.felski.soft.domain.entity.ItemPedido;
import com.felski.soft.domain.entity.Pedido;
import com.felski.soft.domain.entity.ProdutoServico;
import com.felski.soft.domain.entity.QPedido;
import com.felski.soft.domain.enums.StatusPedido;
import com.felski.soft.dto.request.AplicarDescontoRequest;
import com.felski.soft.dto.request.ItemPedidoRequest;
import com.felski.soft.dto.response.PedidoResponse;
import com.felski.soft.exception.BusinessException;
import com.felski.soft.exception.ResourceNotFoundException;
import com.felski.soft.repository.ItemPedidoRepository;
import com.felski.soft.repository.PedidoRepository;
import com.felski.soft.repository.ProdutoServicoRepository;
import com.felski.soft.service.PedidoService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoServicoRepository produtoServicoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository,
                              ProdutoServicoRepository produtoServicoRepository,
                              ItemPedidoRepository itemPedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.produtoServicoRepository = produtoServicoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    @Override
    public Page<PedidoResponse> listarTodos(StatusPedido status, Pageable pageable) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (status != null) {
            predicate.and(QPedido.pedido.statusPedido.eq(status));
        }
        return pedidoRepository.findAll(predicate, pageable)
                .map(PedidoResponse::from);
    }

    @Override
    public PedidoResponse buscarPorId(UUID id) {
        return PedidoResponse.from(buscarComItensOuLancar(id));
    }

    @Override
    @Transactional
    public PedidoResponse criar() {
        return PedidoResponse.from(pedidoRepository.save(new Pedido()));
    }

    @Override
    @Transactional
    public PedidoResponse adicionarItem(UUID pedidoId, ItemPedidoRequest request) {
        Pedido pedido = buscarComItensOuLancar(pedidoId);

        ProdutoServico produtoServico = produtoServicoRepository.findById(request.produtoServicoId())
                .orElseThrow(() -> new ResourceNotFoundException("ProdutoServico", request.produtoServicoId()));

        pedido.adicionarItem(produtoServico, request.quantidade());

        return PedidoResponse.from(pedidoRepository.save(pedido));
    }

    @Override
    @Transactional
    public PedidoResponse removerItem(UUID pedidoId, UUID itemId) {
        Pedido pedido = buscarComItensOuLancar(pedidoId);

        ItemPedido item = itemPedidoRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("ItemPedido", itemId));

        if (!item.getPedido().getId().equals(pedidoId)) {
            throw new BusinessException("Item não pertence ao pedido informado.");
        }

        pedido.removerItem(item);

        return PedidoResponse.from(pedidoRepository.save(pedido));
    }

    @Override
    @Transactional
    public PedidoResponse aplicarDesconto(UUID pedidoId, AplicarDescontoRequest request) {
        Pedido pedido = buscarComItensOuLancar(pedidoId);
        pedido.aplicarDesconto(request.percentualDesconto());
        return PedidoResponse.from(pedidoRepository.save(pedido));
    }

    @Override
    @Transactional
    public PedidoResponse fechar(UUID pedidoId) {
        Pedido pedido = buscarComItensOuLancar(pedidoId);

        if (pedido.getItens().isEmpty()) {
            throw new BusinessException("Não é possível fechar um pedido sem itens.");
        }

        pedido.fechar();
        return PedidoResponse.from(pedidoRepository.save(pedido));
    }

    private Pedido buscarComItensOuLancar(UUID id) {
        return pedidoRepository.findByIdWithItens(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", id));
    }
}
