package br.ufms.api.distribuidora.dtos;

import br.ufms.api.distribuidora.models.EstadoPedidoEnum;
import br.ufms.api.distribuidora.models.PessoaModel;
import br.ufms.api.distribuidora.models.ProdutoModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class PedidoDto {
    private PessoaModel cliente;
    private List<ProdutoModel> produtos;
    private EstadoPedidoEnum estadoPedido;
}
