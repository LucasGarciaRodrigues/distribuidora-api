package br.ufms.api.distribuidora.models.assembler;

import br.ufms.api.distribuidora.controllers.PedidoController;
import br.ufms.api.distribuidora.models.PedidoModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PedidoRepresentationModelAssembler implements SimpleRepresentationModelAssembler<PedidoModel> {
    @Override
    public void addLinks(EntityModel<PedidoModel> resource) {
        Long id = resource.getContent().getId();
        resource.add(linkTo(methodOn(PedidoController.class).getPedidoById(id)).withSelfRel());
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<PedidoModel>> resources) {
        var pageable = PageRequest.of(0, 10);
        resources.add(linkTo(methodOn(PedidoController.class).getAllPedidos(pageable)).withSelfRel());
    }
}
