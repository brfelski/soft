package com.felski.soft.dto.response;

import com.felski.soft.domain.entity.ItemPedido;

import java.math.BigDecimal;
import java.util.UUID;

public record ItemPedidoResponse(
        UUID id,
        UUID produtoServicoId,
        String nomeProdutoServico,
        int quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal) {
    public static ItemPedidoResponse from(ItemPedido entity) {
        return new ItemPedidoResponse(
                entity.getId(),
                entity.getProdutoServico().getId(),
                entity.getProdutoServico().getNome(),
                entity.getQuantidade(),
                entity.getPrecoUnitario(),
                entity.getValorTotalItem());
    }
}
