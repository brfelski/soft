package com.felski.soft.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record AplicarDescontoRequest(

        @NotNull(message = "Percentual de desconto é obrigatório")
        @DecimalMin(value = "0.00", message = "Percentual não pode ser negativo")
        @DecimalMax(value = "100.00", message = "Percentual não pode ultrapassar 100")
        BigDecimal percentualDesconto
) {}
