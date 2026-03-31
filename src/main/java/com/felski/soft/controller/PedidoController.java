package com.felski.soft.controller;

import com.felski.soft.controller.doc.PedidoControllerDoc;
import com.felski.soft.domain.enums.StatusPedido;
import com.felski.soft.dto.request.AplicarDescontoRequest;
import com.felski.soft.dto.request.ItemPedidoRequest;
import com.felski.soft.dto.response.PedidoResponse;
import com.felski.soft.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.felski.soft.infrastructure.assembler.PedidoModelAssembler;

import java.util.UUID;

@RestController
@RequestMapping("/v1/pedidos")
public class PedidoController implements PedidoControllerDoc {

    private final PedidoService service;
    private final PedidoModelAssembler assembler;
    private final PagedResourcesAssembler<PedidoResponse> pagedResourcesAssembler;

    public PedidoController(PedidoService service, PedidoModelAssembler assembler, PagedResourcesAssembler<PedidoResponse> pagedResourcesAssembler) {
        this.service = service;
        this.assembler = assembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<PedidoResponse>>> listar(
            @RequestParam(required = false) StatusPedido status,
            Pageable pageable) {
        Page<PedidoResponse> page = service.listarTodos(status, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(page, assembler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PedidoResponse>> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(assembler.toModel(service.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<EntityModel<PedidoResponse>> criar() {
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(service.criar()));
    }

    @PostMapping("/{id}/itens")
    public ResponseEntity<EntityModel<PedidoResponse>> adicionarItem(
            @PathVariable UUID id,
            @Valid @RequestBody ItemPedidoRequest request) {
        return ResponseEntity.ok(assembler.toModel(service.adicionarItem(id, request)));
    }

    @DeleteMapping("/{id}/itens/{itemId}")
    public ResponseEntity<EntityModel<PedidoResponse>> removerItem(
            @PathVariable UUID id,
            @PathVariable UUID itemId) {
        return ResponseEntity.ok(assembler.toModel(service.removerItem(id, itemId)));
    }

    @PatchMapping("/{id}/desconto")
    public ResponseEntity<EntityModel<PedidoResponse>> aplicarDesconto(
            @PathVariable UUID id,
            @Valid @RequestBody AplicarDescontoRequest request) {
        return ResponseEntity.ok(assembler.toModel(service.aplicarDesconto(id, request)));
    }

    @PatchMapping("/{id}/fechar")
    public ResponseEntity<EntityModel<PedidoResponse>> fechar(@PathVariable UUID id) {
        return ResponseEntity.ok(assembler.toModel(service.fechar(id)));
    }
}
