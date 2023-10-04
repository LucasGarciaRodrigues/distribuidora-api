package br.ufms.api.distribuidora.models.assembler;

import br.ufms.api.distribuidora.controllers.ProdutoController;
import br.ufms.api.distribuidora.models.ProdutoModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ProdutoRepresentationModelAssembler implements SimpleRepresentationModelAssembler<ProdutoModel> {
    @Override
    public void addLinks(EntityModel<ProdutoModel> resource) {
        Long id = resource.getContent().getId();
        resource.add(linkTo(methodOn(ProdutoController.class).getProdutoById(id)).withSelfRel());
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<ProdutoModel>> resources) {
        var pageable = PageRequest.of(0, 10);
        resources.add(linkTo(methodOn(ProdutoController.class).getAllProdutos(pageable)).withSelfRel());
    }
}
