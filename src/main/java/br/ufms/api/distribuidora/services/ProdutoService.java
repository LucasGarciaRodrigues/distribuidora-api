package br.ufms.api.distribuidora.services;

import br.ufms.api.distribuidora.dtos.ProdutoDto;
import br.ufms.api.distribuidora.models.PessoaModel;
import br.ufms.api.distribuidora.models.ProdutoModel;
import br.ufms.api.distribuidora.repositories.ProdutoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository){
        this.produtoRepository = produtoRepository;
    }

    public Optional<ProdutoModel> findById(Long id) {
        return produtoRepository.findById(id);
    }

    public Page<ProdutoModel> findAll(Pageable pageable) {
        return produtoRepository.findAll(pageable);
    }

    @Transactional
    public ProdutoModel save(ProdutoModel produtoModel){
        return produtoRepository.save(produtoModel);
    }

    public void delete(ProdutoModel produtoModel){
        produtoRepository.delete(produtoModel);
    }

    public Page<ProdutoModel> findByFilter(ProdutoDto produtoDto, Pageable pageable) {
        var produtoModel = new ProdutoModel();
        BeanUtils.copyProperties(produtoDto, produtoModel);

        var matcher = ExampleMatcher.matching()
                .withMatcher("nome", match -> match.startsWith())
                .withMatcher("preco", match -> match.exact())
                .withIgnoreCase()
                .withIgnoreNullValues();

        var example = Example.of(produtoModel, matcher);

        return produtoRepository.findAll(example, pageable);
    }
}
