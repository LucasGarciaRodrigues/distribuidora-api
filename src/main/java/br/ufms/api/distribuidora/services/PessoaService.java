package br.ufms.api.distribuidora.services;

import br.ufms.api.distribuidora.dtos.PessoaDto;
import br.ufms.api.distribuidora.models.PessoaModel;
import br.ufms.api.distribuidora.repositories.PessoaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class PessoaService {
    private final PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository){
        this.pessoaRepository = pessoaRepository;
    }

    public Optional<PessoaModel> findById(Long id) {
        return pessoaRepository.findById(id);
    }

    public Page<PessoaModel> findAll(Pageable pageable) {
        return pessoaRepository.findAll(pageable);
    }

    @Transactional
    public PessoaModel save(PessoaModel pessoaModel) {
        return pessoaRepository.save(pessoaModel);
    }

    public void delete(PessoaModel pessoaModel){
        pessoaRepository.delete(pessoaModel);
    }

    public Page<PessoaModel> findByFilter(PessoaDto pessoaDto, Pageable pageable) {
        var pessoaModel = new PessoaModel();
        BeanUtils.copyProperties(pessoaDto, pessoaModel);

        var matcher = ExampleMatcher.matching()
                .withMatcher("nome", match -> match.startsWith())
                .withMatcher("tipoPessoa", match -> match.exact())
                .withIgnoreCase()
                .withIgnoreNullValues();

        var example = Example.of(pessoaModel, matcher);

        return pessoaRepository.findAll(example, pageable);
    }
}
