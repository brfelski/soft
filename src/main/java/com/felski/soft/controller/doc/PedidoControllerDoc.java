package com.felski.soft.controller.doc;

import com.felski.soft.domain.enums.StatusPedido;
import com.felski.soft.dto.request.AplicarDescontoRequest;
import com.felski.soft.dto.request.ItemPedidoRequest;
import com.felski.soft.dto.response.PedidoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Pedidos", description = "Endpoints para gerenciamento do fluxo de pedidos")
public interface PedidoControllerDoc {

        @Operation(summary = "Lista todos os pedidos", description = "Retorna uma lista paginada de pedidos, podendo filtrar pelo status atual.")
        ResponseEntity<PagedModel<EntityModel<PedidoResponse>>> listar(
                        @Parameter(description = "Filtra pelos pedidos com status específico (ABERTO ou FECHADO)") StatusPedido status,
                        Pageable pageable);

        @Operation(summary = "Busca detalhes de um pedido por ID", description = "Retorna os dados completos do pedido, incluindo lista de itens, descontos e totais.")
        @ApiResponse(responseCode = "200", description = "Pedido encontrado")
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
        ResponseEntity<EntityModel<PedidoResponse>> buscarPorId(@Parameter(description = "UUID do pedido") UUID id);

        @Operation(summary = "Inicia um novo pedido", description = "Cria um novo pedido com status 'ABERTO' e sem itens. O pedido deve ser preenchido posteriormente.")
        @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso")
        ResponseEntity<EntityModel<PedidoResponse>> criar();

        @Operation(summary = "Adiciona um item ao pedido", description = "Inclui um produto ou serviço na lista do pedido. Caso o item já exista, a quantidade é incrementada.")
        @ApiResponse(responseCode = "200", description = "Item adicionado com sucesso")
        @ApiResponse(responseCode = "400", description = "Produto/serviço inativo ou inválido")
        ResponseEntity<EntityModel<PedidoResponse>> adicionarItem(
                        @Parameter(description = "UUID do pedido") UUID id,
                        ItemPedidoRequest request);

        @Operation(summary = "Remove um item do pedido", description = "Remove integralmente um item da lista do pedido através do ID do item no pedido.")
        @ApiResponse(responseCode = "200", description = "Item removido com sucesso")
        ResponseEntity<EntityModel<PedidoResponse>> removerItem(
                        @Parameter(description = "UUID do pedido") UUID id,
                        @Parameter(description = "UUID do item na lista") UUID itemId);

        @Operation(summary = "Aplica desconto ao pedido", description = "Define o percentual de desconto do pedido. O desconto incide apenas sobre itens do tipo PRODUTO.")
        @ApiResponse(responseCode = "200", description = "Desconto aplicado com sucesso")
        @ApiResponse(responseCode = "400", description = "Percentual de desconto inválido")
        ResponseEntity<EntityModel<PedidoResponse>> aplicarDesconto(
                        @Parameter(description = "UUID do pedido") UUID id,
                        AplicarDescontoRequest request);

        @Operation(summary = "Fecha (finaliza) o pedido", description = "Altera o status do pedido para 'FECHADO'. Após fechado, o pedido não pode mais ser editado.")
        @ApiResponse(responseCode = "200", description = "Pedido finalizado com sucesso")
        @ApiResponse(responseCode = "422", description = "Regras de negócio para fechamento violadas")
        ResponseEntity<EntityModel<PedidoResponse>> fechar(@Parameter(description = "UUID do pedido") UUID id);
}
