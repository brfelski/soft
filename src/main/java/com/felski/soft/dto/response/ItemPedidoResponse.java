package com.felski.soft.dto.response;

import com.felski.soft.domain.entity.ItemPedido;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Dados de resposta de um item do pedido")
public record ItemPedidoResponse(
        @Schema(description = "Identificador único do item no pedido", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "ID do produto ou serviço", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID produtoServicoId,

        @Schema(description = "Nome do produto ou serviço", example = "Notebook")
        String nomeProdutoServico,

        @Schema(description = "Quantidade", example = "2")
        int quantidade,

        @Schema(description = "Preço unitário no momento da inclusão", example = "2500.00")
        BigDecimal precoUnitario,

        @Schema(description = "Subtotal do item (quantidade * precoUnitario)", example = "5000.00")
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
