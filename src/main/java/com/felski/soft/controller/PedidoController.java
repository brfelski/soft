package com.felski.soft.controller;

import com.felski.soft.domain.enums.StatusPedido;
import com.felski.soft.dto.request.AplicarDescontoRequest;
import com.felski.soft.dto.request.ItemPedidoRequest;
import com.felski.soft.dto.response.PedidoResponse;
import com.felski.soft.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<PedidoResponse>> listar(
            @RequestParam(required = false) StatusPedido status,
            Pageable pageable) {
        return ResponseEntity.ok(service.listarTodos(status, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<PedidoResponse> criar() {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar());
    }

    @PostMapping("/{id}/itens")
    public ResponseEntity<PedidoResponse> adicionarItem(
            @PathVariable UUID id,
            @Valid @RequestBody ItemPedidoRequest request) {
        return ResponseEntity.ok(service.adicionarItem(id, request));
    }

    @DeleteMapping("/{id}/itens/{itemId}")
    public ResponseEntity<PedidoResponse> removerItem(
            @PathVariable UUID id,
            @PathVariable UUID itemId) {
        return ResponseEntity.ok(service.removerItem(id, itemId));
    }

    @PatchMapping("/{id}/desconto")
    public ResponseEntity<PedidoResponse> aplicarDesconto(
            @PathVariable UUID id,
            @Valid @RequestBody AplicarDescontoRequest request) {
        return ResponseEntity.ok(service.aplicarDesconto(id, request));
    }

    @PatchMapping("/{id}/fechar")
    public ResponseEntity<PedidoResponse> fechar(@PathVariable UUID id) {
        return ResponseEntity.ok(service.fechar(id));
    }
}
