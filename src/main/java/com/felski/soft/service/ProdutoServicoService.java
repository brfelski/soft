package com.felski.soft.service;

import com.felski.soft.domain.enums.TipoItem;
import com.felski.soft.dto.request.ProdutoServicoRequest;
import com.felski.soft.dto.response.ProdutoServicoResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProdutoServicoService {

    Page<ProdutoServicoResponse> listarTodos(Boolean apenasAtivos, TipoItem tipoItem, Pageable pageable);

    ProdutoServicoResponse buscarPorId(UUID id);

    ProdutoServicoResponse criar(ProdutoServicoRequest request);

    ProdutoServicoResponse atualizar(UUID id, ProdutoServicoRequest request);

    void deletar(UUID id);

    ProdutoServicoResponse desativar(UUID id);
}
