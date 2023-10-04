package br.ufms.api.distribuidora.controllers;


import br.ufms.api.distribuidora.dtos.PedidoDto;
import br.ufms.api.distribuidora.dtos.ProdutoDto;
import br.ufms.api.distribuidora.models.EstadoPedidoEnum;
import br.ufms.api.distribuidora.models.PedidoModel;
import br.ufms.api.distribuidora.models.PessoaModel;
import br.ufms.api.distribuidora.models.ProdutoModel;
import br.ufms.api.distribuidora.models.assembler.ProdutoRepresentationModelAssembler;
import br.ufms.api.distribuidora.services.DistribuidoraFacade;
import br.ufms.api.distribuidora.services.ProdutoService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/produto")
public class ProdutoController {
    private final DistribuidoraFacade distribuidoraFacade;
    private final ProdutoRepresentationModelAssembler produtoModelAssembler;
    private final PagedResourcesAssembler pagedResourcesAssembler;
    private final Pageable pageable = PageRequest.of(0,10);

    public ProdutoController(DistribuidoraFacade distribuidoraFacade, ProdutoRepresentationModelAssembler produtoModelAssembler, PagedResourcesAssembler pagedResourcesAssembler){
        this.distribuidoraFacade = distribuidoraFacade;
        this.produtoModelAssembler = produtoModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ProdutoModel>> getProdutoById(@PathVariable(value = "id") Long id){
        return distribuidoraFacade.produtoFindById(id)
                .map(produtoModel -> {
                    EntityModel<ProdutoModel> produto = produtoModelAssembler.toModel(produtoModel)
                            .add(linkTo(methodOn(ProdutoController.class).getAllProdutos(pageable)).withRel("todos os produtos"));
                    return ResponseEntity.ok(produto);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi possível encontrar o produto."));
    }

    @GetMapping
    public ResponseEntity<PagedModel<ProdutoModel>> getAllProdutos(
            @PageableDefault(page = 0, size = 10, sort = "id",
                    direction = Sort.Direction.ASC) Pageable pageable) {
        var pagina =  distribuidoraFacade.produtoFindAll(pageable);
        PagedModel<ProdutoModel> collModel = pagedResourcesAssembler.toModel(pagina, produtoModelAssembler);
        return new ResponseEntity<>(collModel, HttpStatus.OK);
    }

    @GetMapping("/search/{nome}")
    public ResponseEntity<PagedModel<PedidoModel>> searchProdutoByFilter(@PathVariable String nome){
        var produtoDto = new ProdutoDto();
        produtoDto.setNome(nome);

        var pagina = distribuidoraFacade.produtoFindByFilter(produtoDto, pageable);
        PagedModel<PedidoModel> collModel = pagedResourcesAssembler.toModel(pagina, produtoModelAssembler);

        return new ResponseEntity<>(collModel, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> saveProduto(@RequestBody @Valid ProdutoDto produtoDto){
        var produtoModel = new ProdutoModel();
        BeanUtils.copyProperties(produtoDto, produtoModel);
        produtoModel = distribuidoraFacade.produtoSave(produtoModel);
        EntityModel<ProdutoModel> entityModel = produtoModelAssembler.toModel(produtoModel)
                .add(linkTo(methodOn(ProdutoController.class).getAllProdutos(pageable)).withRel("todos os produtos"));
        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateProduto(@PathVariable(value = "id") Long id,
                                                @RequestBody @Valid ProdutoDto produtoDto){
        Optional<ProdutoModel> produtoModelOptional = distribuidoraFacade.produtoFindById(id);
        if(!produtoModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível encontrar o produto.");
        }
        var produtoModel = new ProdutoModel();
        BeanUtils.copyProperties(produtoDto, produtoModel);
        produtoModel.setId(produtoModelOptional.get().getId());
        EntityModel<ProdutoModel> produto = produtoModelAssembler.toModel(distribuidoraFacade.produtoSave(produtoModel));
        return ResponseEntity.ok(produto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduto(@PathVariable Long id){
        Optional<ProdutoModel> produtoModelOptional = distribuidoraFacade.produtoFindById(id);
        if(!produtoModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível encontrar o produto.");
        }
        distribuidoraFacade.produtoDelete(produtoModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Produto deletado com sucesso.");
    }

}
