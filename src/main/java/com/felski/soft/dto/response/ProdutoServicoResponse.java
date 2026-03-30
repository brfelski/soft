package com.felski.soft.dto.response;

import com.felski.soft.domain.entity.ProdutoServico;
import com.felski.soft.domain.enums.TipoItem;

import java.math.BigDecimal;
import java.util.UUID;

public record ProdutoServicoResponse(
        UUID id,
        String nome,
        BigDecimal preco,
        boolean ativo,
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
