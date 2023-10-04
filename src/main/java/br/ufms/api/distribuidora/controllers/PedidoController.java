package br.ufms.api.distribuidora.controllers;


import br.ufms.api.distribuidora.dtos.PedidoDto;
import br.ufms.api.distribuidora.dtos.PessoaDto;
import br.ufms.api.distribuidora.dtos.ProdutoDto;
import br.ufms.api.distribuidora.models.*;
import br.ufms.api.distribuidora.models.assembler.PedidoRepresentationModelAssembler;
import br.ufms.api.distribuidora.services.DistribuidoraFacade;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/pedido")
public class PedidoController {
    private final DistribuidoraFacade distribuidoraFacade;
    private final PedidoRepresentationModelAssembler pedidoModelAssembler;
    private final PagedResourcesAssembler pagedResourcesAssembler;
    private final Pageable pageable = PageRequest.of(0,10);

    public PedidoController(DistribuidoraFacade distribuidoraFacade, PedidoRepresentationModelAssembler pedidoModelAssembler, PagedResourcesAssembler pagedResourcesAssembler) {
        this.distribuidoraFacade = distribuidoraFacade;
        this.pedidoModelAssembler = pedidoModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PedidoModel>> getPedidoById(@PathVariable(value = "id") Long id){
        return distribuidoraFacade.pedidoFindById(id).map(pedidoModel -> {
            EntityModel<PedidoModel> pedido = pedidoModelAssembler.toModel(pedidoModel)
                    .add(linkTo(methodOn(PedidoController.class).getAllPedidos(pageable)).withRel("todos os pedidos"));
            return ResponseEntity.ok(pedido);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi possível encontrar o pedido."));
    }

    @GetMapping
    public ResponseEntity<PagedModel<PedidoModel>> getAllPedidos(
            @PageableDefault(page = 0, size = 10, sort = "id",
                    direction = Sort.Direction.ASC) Pageable pageable) {
        var pagina = distribuidoraFacade.pedidoFindAll(pageable);
        PagedModel<PedidoModel> collModel = pagedResourcesAssembler.toModel(pagina, pedidoModelAssembler);
        return new ResponseEntity<>(collModel, HttpStatus.OK);
    }

    @GetMapping("/search/{estadoPedido}")
    public ResponseEntity<PagedModel<PedidoModel>> searchPedidoByFilter(@PathVariable EstadoPedidoEnum estadoPedido){
        var pedidoDto = new PedidoDto();
        pedidoDto.setEstadoPedido(estadoPedido);

        var pagina = distribuidoraFacade.pedidoFindByFilter(pedidoDto, pageable);
        PagedModel<PedidoModel> collModel = pagedResourcesAssembler.toModel(pagina, pedidoModelAssembler);

        return new ResponseEntity<>(collModel, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> savePedido(@RequestBody @Valid PedidoDto pedidoDto) throws Exception{
        var pedidoModel = new PedidoModel();
        BeanUtils.copyProperties(pedidoDto, pedidoModel);
        pedidoModel.setProdutos(pedidoModel.getProdutos());
        pedidoModel.setValorTotal(0.00);
        for(ProdutoModel produtoModel : pedidoDto.getProdutos()){
            pedidoModel.setValorTotal(pedidoModel.getValorTotal() +  produtoModel.getPreco());
        }
        pedidoModel = distribuidoraFacade.pedidoSave(pedidoModel);
        EntityModel<PedidoModel> entityModel = pedidoModelAssembler.toModel(pedidoModel)
                .add(linkTo(methodOn(PedidoController.class).getAllPedidos(pageable)).withRel("todos os pedidos"));
        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePedido(@PathVariable(value = "id") Long id,
                                               @RequestBody @Valid PedidoDto pedidoDto) throws Exception{
        Optional<PedidoModel> pedidoModelOptional = distribuidoraFacade.pedidoFindById(id);
        if(!pedidoModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível encontrar o Pedido.");
        }
        var pedidoModel = new PedidoModel();
        BeanUtils.copyProperties(pedidoDto, pedidoModel);
        pedidoModel.setProdutos(pedidoModel.getProdutos());
        pedidoModel.setValorTotal(0.00);
        for(ProdutoModel produtoModel : pedidoDto.getProdutos()){
            pedidoModel.setValorTotal(pedidoModel.getValorTotal() +  produtoModel.getPreco());
        }
        pedidoModel.setId(pedidoModelOptional.get().getId());
        EntityModel<PedidoModel> pedido = pedidoModelAssembler.toModel(distribuidoraFacade.pedidoUpdate(pedidoModel));
        return ResponseEntity.ok(pedido);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePedido(@PathVariable Long id){
        Optional<PedidoModel> pedidoModelOptional = distribuidoraFacade.pedidoFindById(id);
        if(!pedidoModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível encontrar o Pedido.");
        }
        distribuidoraFacade.pedidoDelete(pedidoModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Pedido deletado com sucesso.");
    }
}
