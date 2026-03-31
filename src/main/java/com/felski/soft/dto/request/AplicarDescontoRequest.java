package com.felski.soft.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Schema(description = "Dados para aplicar desconto a um pedido")
public record AplicarDescontoRequest(

        @Schema(description = "Percentual de desconto a ser aplicado", example = "10.00")
        @NotNull(message = "Percentual de desconto é obrigatório")
        @DecimalMin(value = "0.00", message = "Percentual não pode ser negativo")
        @DecimalMax(value = "100.00", message = "Percentual não pode ultrapassar 100")
        BigDecimal percentualDesconto
) {}
