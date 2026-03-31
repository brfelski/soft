package com.felski.soft.dto.response;

import com.felski.soft.domain.entity.ProdutoServico;
import com.felski.soft.domain.enums.TipoItem;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Dados de resposta de um produto ou serviço")
public record ProdutoServicoResponse(
        @Schema(description = "Identificador único", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,

        @Schema(description = "Nome do produto ou serviço", example = "Consultoria Técnica")
        String nome,

        @Schema(description = "Preço unitário", example = "150.00")
        BigDecimal preco,

        @Schema(description = "Indica se o item está ativo", example = "true")
        boolean ativo,

        @Schema(description = "Tipo do item", example = "SERVICO")
        TipoItem tipoItem) {
    public static ProdutoServicoResponse from(ProdutoServico entity) {
        return new ProdutoServicoResponse(
                entity.getId(),
                entity.getNome(),
                entity.getPreco(),
                entity.getAtivo(),
                entity.getTipoItem());
    }
}
