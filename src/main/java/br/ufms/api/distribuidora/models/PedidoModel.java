package br.ufms.api.distribuidora.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.Fetch;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "TB_PEDIDO", uniqueConstraints = {@UniqueConstraint(name = "TB_PEDIDO_UQ", columnNames = {"cliente", "valorTotal", "estadoPedido"})}
)
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PedidoModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente", foreignKey = @ForeignKey(name = "FK_CLIENTE"), nullable = false)
    @JsonIgnore
    private PessoaModel cliente;
    @ManyToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ProdutoModel> produtos;
    @Column(nullable = false)
    private Double valorTotal;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedidoEnum estadoPedido;

    public void setProdutos(List<ProdutoModel> produtos){this.produtos = new ArrayList<>(produtos);}




}
