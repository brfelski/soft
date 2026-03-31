package com.felski.soft.controller;

import com.felski.soft.controller.doc.ProdutoServicoControllerDoc;
import com.felski.soft.domain.enums.TipoItem;
import com.felski.soft.dto.request.ProdutoServicoRequest;
import com.felski.soft.dto.response.ProdutoServicoResponse;
import com.felski.soft.service.ProdutoServicoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.felski.soft.infrastructure.assembler.ProdutoServicoModelAssembler;

import java.util.UUID;

@RestController
@RequestMapping("/v1/produtos-servicos")
public class ProdutoServicoController implements ProdutoServicoControllerDoc {

    private final ProdutoServicoService service;
    private final ProdutoServicoModelAssembler assembler;
    private final PagedResourcesAssembler<ProdutoServicoResponse> pagedResourcesAssembler;

    public ProdutoServicoController(ProdutoServicoService service, ProdutoServicoModelAssembler assembler, PagedResourcesAssembler<ProdutoServicoResponse> pagedResourcesAssembler) {
        this.service = service;
        this.assembler = assembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<ProdutoServicoResponse>>> listar(
            @RequestParam(required = false) Boolean apenasAtivos,
            @RequestParam(required = false) TipoItem tipoItem,
            Pageable pageable) {
        Page<ProdutoServicoResponse> page = service.listarTodos(apenasAtivos, tipoItem, pageable);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(page, assembler));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProdutoServicoResponse>> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(assembler.toModel(service.buscarPorId(id)));
    }

    @PostMapping
    public ResponseEntity<EntityModel<ProdutoServicoResponse>> criar(@Valid @RequestBody ProdutoServicoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(service.criar(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ProdutoServicoResponse>> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ProdutoServicoRequest request) {
        return ResponseEntity.ok(assembler.toModel(service.atualizar(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<EntityModel<ProdutoServicoResponse>> desativar(@PathVariable UUID id) {
        return ResponseEntity.ok(assembler.toModel(service.desativar(id)));
    }
}
