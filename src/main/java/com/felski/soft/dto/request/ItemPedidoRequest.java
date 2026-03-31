package com.felski.soft.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Dados para adicionar um item ao pedido")
public record ItemPedidoRequest(

        @Schema(description = "ID do produto ou serviço", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotNull(message = "ID do produto/serviço é obrigatório")
        UUID produtoServicoId,

        @Schema(description = "Quantidade solicitada", example = "2")
        @Min(value = 1, message = "Quantidade deve ser no mínimo 1")
        int quantidade
) {}
