package com.felski.soft.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ItemPedidoRequest(

        @NotNull(message = "ID do produto/serviço é obrigatório")
        UUID produtoServicoId,

        @Min(value = 1, message = "Quantidade deve ser no mínimo 1")
        int quantidade
) {}
