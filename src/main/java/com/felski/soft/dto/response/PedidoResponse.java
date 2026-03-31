package com.felski.soft.dto.response;

import com.felski.soft.domain.entity.Pedido;
import com.felski.soft.domain.enums.StatusPedido;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Schema(description = "Representação de resposta de um pedido")
public record PedidoResponse(
        UUID id,
        StatusPedido statusPedido,
        BigDecimal percentualDesconto,
        BigDecimal valorTotalDesconto,
        BigDecimal valorTotalPedido,
        List<ItemPedidoResponse> itens) {
    public static PedidoResponse from(Pedido entity) {
        List<ItemPedidoResponse> itensResponse = entity.getItens().stream()
                .map(ItemPedidoResponse::from)
                .toList();

        return new PedidoResponse(
                entity.getId(),
                entity.getStatusPedido(),
                entity.getPercentualDesconto(),
                entity.getValorTotalDesconto(),
                entity.getValorTotalPedido(),
                itensResponse);
    }
}
