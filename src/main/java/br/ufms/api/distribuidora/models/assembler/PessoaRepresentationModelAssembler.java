package br.ufms.api.distribuidora.models.assembler;

import br.ufms.api.distribuidora.controllers.PessoaController;
import br.ufms.api.distribuidora.controllers.ProdutoController;
import br.ufms.api.distribuidora.models.PessoaModel;
import br.ufms.api.distribuidora.models.ProdutoModel;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PessoaRepresentationModelAssembler implements SimpleRepresentationModelAssembler<PessoaModel> {
    @Override
    public void addLinks(EntityModel<PessoaModel> resource) {
        Long id = resource.getContent().getId();
        resource.add(linkTo(methodOn(PessoaController.class).getPessoaById(id)).withSelfRel());
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<PessoaModel>> resources) {
        var pageable = PageRequest.of(0, 10);
        resources.add(linkTo(methodOn(PessoaController.class).getAllPessoas(pageable)).withSelfRel());
    }
}
