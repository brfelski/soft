package com.felski.soft.controller;

import com.felski.soft.domain.enums.TipoItem;
import com.felski.soft.dto.request.ProdutoServicoRequest;
import com.felski.soft.dto.response.ProdutoServicoResponse;
import com.felski.soft.service.ProdutoServicoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/produtos-servicos")
public class ProdutoServicoController {

    private final ProdutoServicoService service;

    public ProdutoServicoController(ProdutoServicoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoServicoResponse>> listar(
            @RequestParam(required = false) Boolean apenasAtivos,
            @RequestParam(required = false) TipoItem tipoItem,
            Pageable pageable) {
        return ResponseEntity.ok(service.listarTodos(apenasAtivos, tipoItem, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoServicoResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ProdutoServicoResponse> criar(@Valid @RequestBody ProdutoServicoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoServicoResponse> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ProdutoServicoRequest request) {
        return ResponseEntity.ok(service.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<ProdutoServicoResponse> desativar(@PathVariable UUID id) {
        return ResponseEntity.ok(service.desativar(id));
    }
}
