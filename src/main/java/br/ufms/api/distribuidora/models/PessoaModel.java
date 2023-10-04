package br.ufms.api.distribuidora.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(
        name = "TB_PESSOA", uniqueConstraints = {@UniqueConstraint(name = "TB_PESSOA_UQ", columnNames = {"nome", "tipoPessoa"})}
)
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PessoaModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, length = 100)
    private String nome;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPessoaEnum tipoPessoa;

}
