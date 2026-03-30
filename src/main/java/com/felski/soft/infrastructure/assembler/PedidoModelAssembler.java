package com.felski.soft.infrastructure.assembler;

import com.felski.soft.controller.PedidoController;
import com.felski.soft.dto.response.PedidoResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PedidoModelAssembler
        extends RepresentationModelAssemblerSupport<PedidoResponse, EntityModel<PedidoResponse>> {

    public PedidoModelAssembler() {
        super(PedidoController.class, (Class<EntityModel<PedidoResponse>>) (Class<?>) EntityModel.class);
    }

    @Override
    public EntityModel<PedidoResponse> toModel(PedidoResponse response) {
        EntityModel<PedidoResponse> model = EntityModel.of(response);

        model.add(linkTo(methodOn(PedidoController.class).buscarPorId(response.id())).withSelfRel());

        model.add(linkTo(methodOn(PedidoController.class).listar(null, null)).withRel("pedidos"));

        if (response.statusPedido().name().equals("ABERTO")) {
            model.add(linkTo(methodOn(PedidoController.class).adicionarItem(response.id(), null))
                    .withRel("adicionar-item"));
            model.add(linkTo(methodOn(PedidoController.class).fechar(response.id())).withRel("fechar-pedido"));
            model.add(linkTo(methodOn(PedidoController.class).aplicarDesconto(response.id(), null))
                    .withRel("aplicar-desconto"));
        }

        return model;
    }
}
