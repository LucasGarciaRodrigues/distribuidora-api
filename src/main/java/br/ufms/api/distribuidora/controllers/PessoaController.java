package br.ufms.api.distribuidora.controllers;

import br.ufms.api.distribuidora.dtos.PessoaDto;
import br.ufms.api.distribuidora.models.PessoaModel;
import br.ufms.api.distribuidora.models.TipoPessoaEnum;
import br.ufms.api.distribuidora.models.assembler.PessoaRepresentationModelAssembler;
import br.ufms.api.distribuidora.services.DistribuidoraFacade;
import br.ufms.api.distribuidora.services.PessoaService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/pessoa")
public class PessoaController {

    private final DistribuidoraFacade distribuidoraFacade;
    private final PessoaRepresentationModelAssembler pessoaModelAssembler;
    private final PagedResourcesAssembler pagedResourcesAssembler;
    private final Pageable pageable = PageRequest.of(0,10);

    public PessoaController(DistribuidoraFacade distribuidoraFacade, PessoaRepresentationModelAssembler pessoaModelAssembler, PagedResourcesAssembler pagedResourcesAssembler){
        this.distribuidoraFacade = distribuidoraFacade;
        this.pessoaModelAssembler = pessoaModelAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PessoaModel>> getPessoaById(@PathVariable(value = "id") Long id){
        return distribuidoraFacade.pessoaFindById(id).map(pessoaModel -> {
            EntityModel<PessoaModel> pessoa = pessoaModelAssembler.toModel(pessoaModel)
                    .add(linkTo(methodOn(PessoaController.class).getAllPessoas(pageable)).withRel("todas as pessoas"));
            return ResponseEntity.ok(pessoa);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Não foi possível encontrar a pessoa."));
    }

    @GetMapping
    public ResponseEntity<PagedModel<PessoaModel>> getAllPessoas(
            @PageableDefault(page = 0, size = 10, sort = "id",
                    direction = Sort.Direction.ASC) Pageable pageable) {
        var pagina = distribuidoraFacade.pessoaFindAll(pageable);
        PagedModel<PessoaModel> collModel = pagedResourcesAssembler.toModel(pagina, pessoaModelAssembler);
        return new ResponseEntity<>(collModel, HttpStatus.OK);
    }

    @GetMapping("/search/{tipoPessoa}")
    public ResponseEntity<PagedModel<PessoaModel>> searchPessoaByFilter(@PathVariable TipoPessoaEnum tipoPessoa,
                                                                                String nome){
        var pessoaDto = new PessoaDto();
        pessoaDto.setNome(nome);
        pessoaDto.setTipoPessoa(tipoPessoa);

        var pagina = distribuidoraFacade.pessoaFindByFilter(pessoaDto, pageable);
        PagedModel<PessoaModel> collModel = pagedResourcesAssembler.toModel(pagina, pessoaModelAssembler);

        return new ResponseEntity<>(collModel, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> savePessoa(@RequestBody @Valid PessoaDto pessoaDto){
        var pessoaModel = new PessoaModel();
        BeanUtils.copyProperties(pessoaDto, pessoaModel);
        pessoaModel = distribuidoraFacade.pessoaSave(pessoaModel);
        EntityModel<PessoaModel> entityModel = pessoaModelAssembler.toModel(pessoaModel)
                .add(linkTo(methodOn(PessoaController.class).getAllPessoas(pageable)).withRel("todas as pessoas"));
        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updatePessoa(@PathVariable(value = "id") Long id,
                                                   @RequestBody @Valid PessoaDto pessoaDto){
        Optional<PessoaModel> pessoaModelOptional = distribuidoraFacade.pessoaFindById(id);
        if(!pessoaModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível encontrar a Pessoa.");
        }
        var pessoaModel = new PessoaModel();
        BeanUtils.copyProperties(pessoaDto, pessoaModel);
        pessoaModel.setId(pessoaModelOptional.get().getId());
        EntityModel<PessoaModel> pessoa = pessoaModelAssembler.toModel(distribuidoraFacade.pessoaSave(pessoaModel));
        return ResponseEntity.ok(pessoa);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePessoa(@PathVariable Long id){
        Optional<PessoaModel> pessoaModelOptional = distribuidoraFacade.pessoaFindById(id);
        if(!pessoaModelOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível encontrar a Pessoa.");
        }
        distribuidoraFacade.pessoaDelete(pessoaModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Pessoa deletada com sucesso.");
    }

}
