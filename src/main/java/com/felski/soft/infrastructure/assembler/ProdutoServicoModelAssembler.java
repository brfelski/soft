package com.felski.soft.infrastructure.assembler;

import com.felski.soft.controller.ProdutoServicoController;
import com.felski.soft.dto.response.ProdutoServicoResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProdutoServicoModelAssembler
        extends RepresentationModelAssemblerSupport<ProdutoServicoResponse, EntityModel<ProdutoServicoResponse>> {

    @SuppressWarnings("unchecked")
    public ProdutoServicoModelAssembler() {
        super(ProdutoServicoController.class,
                (Class<EntityModel<ProdutoServicoResponse>>) (Class<?>) EntityModel.class);
    }

    @Override
    public EntityModel<ProdutoServicoResponse> toModel(ProdutoServicoResponse response) {
        EntityModel<ProdutoServicoResponse> model = EntityModel.of(response);

        model.add(linkTo(methodOn(ProdutoServicoController.class).buscarPorId(response.id())).withSelfRel());

        model.add(
                linkTo(methodOn(ProdutoServicoController.class).listar(null, null, null)).withRel("produtos-servicos"));
        model.add(linkTo(methodOn(ProdutoServicoController.class).atualizar(response.id(), null)).withRel("atualizar"));

        if (response.ativo()) {
            model.add(linkTo(methodOn(ProdutoServicoController.class).desativar(response.id())).withRel("desativar"));
        }

        return model;
    }
}
