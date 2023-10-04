package br.ufms.api.distribuidora.services;

import br.ufms.api.distribuidora.dtos.PedidoDto;
import br.ufms.api.distribuidora.models.PedidoModel;
import br.ufms.api.distribuidora.models.PessoaModel;
import br.ufms.api.distribuidora.models.TipoPessoaEnum;
import br.ufms.api.distribuidora.repositories.PedidoRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class PedidoService {
    private final PedidoRepository pedidoRepository;

    private final PessoaService pessoaService;

    public PedidoService(PedidoRepository pedidoRepository, PessoaService pessoaService){
        this.pedidoRepository = pedidoRepository;
        this.pessoaService = pessoaService;
    }

    public Optional<PedidoModel> findById(Long id) {
        return pedidoRepository.findById(id);
    }

    public Page<PedidoModel> findAll(Pageable pageable) {
        return pedidoRepository.findAll(pageable);
    }

    @Transactional
    public PedidoModel save(PedidoModel pedidoModel) throws Exception{

        if(pedidoModel.getValorTotal() < 200.00){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O valor total do pedido deve ser no mínimo de 200 reais.");
        }
        if(pedidoModel.getCliente().getTipoPessoa() == TipoPessoaEnum.FISICA && pedidoModel.getValorTotal() > 1000.00){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O valor total do pedido excede o limite para pessoas físicas.");
        }
        if(pedidoModel.getCliente().getTipoPessoa() == TipoPessoaEnum.JURIDICA){
            var valorDoDesconto = pedidoModel.getValorTotal() * 0.15;
            pedidoModel.setValorTotal(pedidoModel.getValorTotal() - valorDoDesconto);
        }
        return pedidoRepository.save(pedidoModel);
    }

    @Transactional
    public PedidoModel update(PedidoModel pedidoModel) throws Exception{
        if(pedidoModel.getValorTotal() < 200.00){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O valor total do pedido deve ser no mínimo de 200 reais.");
        }
        if(pedidoModel.getCliente().getTipoPessoa() == TipoPessoaEnum.FISICA && pedidoModel.getValorTotal() > 1000.00){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O valor total do pedido excede o limite para pessoas físicas.");
        }
        return pedidoRepository.save(pedidoModel);
    }

    public void delete(PedidoModel pedidoModel){
        pedidoRepository.delete(pedidoModel);
    }

    public Page<PedidoModel> findByFilter(PedidoDto pedidoDto, Pageable pageable) {
        var pedidoModel = new PedidoModel();
        pedidoModel.setEstadoPedido(pedidoDto.getEstadoPedido());

        var matcher = ExampleMatcher.matching()
                .withMatcher("estadoPedido", match -> match.exact())
                .withIgnoreCase()
                .withIgnoreNullValues();

        var example = Example.of(pedidoModel, matcher);

        return pedidoRepository.findAll(example, pageable);
    }
}
