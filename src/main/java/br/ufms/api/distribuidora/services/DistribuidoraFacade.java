package br.ufms.api.distribuidora.services;

import br.ufms.api.distribuidora.dtos.PedidoDto;
import br.ufms.api.distribuidora.dtos.PessoaDto;
import br.ufms.api.distribuidora.dtos.ProdutoDto;
import br.ufms.api.distribuidora.models.PedidoModel;
import br.ufms.api.distribuidora.models.PessoaModel;
import br.ufms.api.distribuidora.models.ProdutoModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class DistribuidoraFacade {
    private final PessoaService pessoaService;
    private final ProdutoService produtoService;
    private final PedidoService pedidoService;

    public DistribuidoraFacade(PessoaService pessoaService, ProdutoService produtoService, PedidoService pedidoService){
        this.pessoaService = pessoaService;
        this.produtoService = produtoService;
        this.pedidoService = pedidoService;
    }

    // Pessoa
    public Optional<PessoaModel> pessoaFindById(Long id){
        return pessoaService.findById(id);
    }

    public Page<PessoaModel> pessoaFindAll(Pageable pageable){
        return pessoaService.findAll(pageable);
    }

    public Page<PessoaModel> pessoaFindByFilter(PessoaDto pessoaDto, Pageable pageable) {
        return pessoaService.findByFilter(pessoaDto, pageable);
    }
    public PessoaModel pessoaSave(PessoaModel pessoaModel){
        return pessoaService.save(pessoaModel);
    }

    public void pessoaDelete(PessoaModel pessoaModel){
        pessoaService.delete(pessoaModel);
    }

    // Produto
    public Optional<ProdutoModel> produtoFindById(Long id){
        return produtoService.findById(id);
    }
    public Page<ProdutoModel> produtoFindAll(Pageable pageable){
        return produtoService.findAll(pageable);
    }
    public Page<ProdutoModel> produtoFindByFilter(ProdutoDto produtoDto, Pageable pageable) {
        return produtoService.findByFilter(produtoDto, pageable);
    }
    public ProdutoModel produtoSave(ProdutoModel produtoModel){
        return produtoService.save(produtoModel);
    }
    public void produtoDelete(ProdutoModel produtoModel){
        produtoService.delete(produtoModel);
    }

    //Pedido
    public Optional<PedidoModel> pedidoFindById(Long id) {
        return pedidoService.findById(id);
    }
    public Page<PedidoModel> pedidoFindAll(Pageable pageable) {
        return pedidoService.findAll(pageable);
    }
    public Page<PedidoModel> pedidoFindByFilter(PedidoDto pedidoDto, Pageable pageable) {
        return pedidoService.findByFilter(pedidoDto, pageable);
    }
    public PedidoModel pedidoSave(PedidoModel pedidoModel) throws Exception{
        return pedidoService.save(pedidoModel);
    }
    public PedidoModel pedidoUpdate(PedidoModel pedidoModel) throws Exception {
        return pedidoService.update(pedidoModel);
    }
    public void pedidoDelete(PedidoModel pedidoModel) {
        pedidoService.delete(pedidoModel);
    }
}
