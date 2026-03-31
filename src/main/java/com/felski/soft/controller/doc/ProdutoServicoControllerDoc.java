package com.felski.soft.controller.doc;

import com.felski.soft.domain.enums.TipoItem;
import com.felski.soft.dto.request.ProdutoServicoRequest;
import com.felski.soft.dto.response.ProdutoServicoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Produtos e Serviços", description = "Endpoints para gerenciamento do catálogo de produtos e serviços")
public interface ProdutoServicoControllerDoc {

    @Operation(summary = "Lista todos os produtos e serviços", description = "Retorna uma lista paginada de itens, permitindo filtrar por ativos/inativos e tipo de item.")
    ResponseEntity<PagedModel<EntityModel<ProdutoServicoResponse>>> listar(
            @Parameter(description = "Se true, retorna apenas itens ativos") Boolean apenasAtivos,
            @Parameter(description = "Filtra pelo tipo de item (PRODUTO ou SERVICO)") TipoItem tipoItem,
            Pageable pageable);

    @Operation(summary = "Busca um item por ID", description = "Retorna os detalhes de um produto ou serviço específico através do seu identificador UUID.")
    @ApiResponse(responseCode = "200", description = "Item encontrado com sucesso")
    @ApiResponse(responseCode = "404", description = "Item não encontrado")
    ResponseEntity<EntityModel<ProdutoServicoResponse>> buscarPorId(@Parameter(description = "UUID do item") UUID id);

    @Operation(summary = "Cria um novo produto ou serviço", description = "Realiza o cadastro de um novo item no catálogo. O campo 'ativo' é definido como true por padrão.")
    @ApiResponse(responseCode = "201", description = "Item criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos")
    ResponseEntity<EntityModel<ProdutoServicoResponse>> criar(ProdutoServicoRequest request);

    @Operation(summary = "Atualiza um item existente", description = "Atualiza integralmente os dados de um item. O ID passado na URL deve existir.")
    @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso")
    ResponseEntity<EntityModel<ProdutoServicoResponse>> atualizar(UUID id, ProdutoServicoRequest request);

    @Operation(summary = "Exclui um item", description = "Remove fisicamente o registro do item do banco de dados.")
    @ApiResponse(responseCode = "244", description = "Item excluído com sucesso")
    ResponseEntity<Void> deletar(UUID id);

    @Operation(summary = "Desativa um item", description = "Realiza a desativação lógica (soft delete) do item, alterando seu status para inativo.")
    @ApiResponse(responseCode = "200", description = "Item desativado com sucesso")
    ResponseEntity<EntityModel<ProdutoServicoResponse>> desativar(UUID id);
}
