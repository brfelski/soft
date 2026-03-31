package com.felski.soft.dto.request;

import com.felski.soft.domain.enums.TipoItem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Dados para criação de um produto ou serviço")
public record ProdutoServicoRequest(

        @Schema(description = "Nome do produto ou serviço", example = "Consultoria Técnica")
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @Schema(description = "Preço unitário", example = "150.00")
        @NotNull(message = "Preço é obrigatório")
        @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
        BigDecimal preco,

        @Schema(description = "Tipo do item", example = "SERVICO")
        @NotNull(message = "Tipo do item é obrigatório")
        TipoItem tipoItem
) {}
