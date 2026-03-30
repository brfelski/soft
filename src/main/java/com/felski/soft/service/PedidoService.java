package com.felski.soft.service;

import com.felski.soft.domain.enums.StatusPedido;
import com.felski.soft.dto.request.AplicarDescontoRequest;
import com.felski.soft.dto.request.ItemPedidoRequest;
import com.felski.soft.dto.response.PedidoResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PedidoService {

    Page<PedidoResponse> listarTodos(StatusPedido status, Pageable pageable);

    PedidoResponse buscarPorId(UUID id);

    PedidoResponse criar();

    PedidoResponse adicionarItem(UUID pedidoId, ItemPedidoRequest request);

    PedidoResponse removerItem(UUID pedidoId, UUID itemId);

    PedidoResponse aplicarDesconto(UUID pedidoId, AplicarDescontoRequest request);

    PedidoResponse fechar(UUID pedidoId);
}
