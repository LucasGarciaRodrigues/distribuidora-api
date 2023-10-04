package br.ufms.api.distribuidora.dtos;

import br.ufms.api.distribuidora.models.TipoPessoaEnum;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class PessoaDto {
    @NotBlank
    @Size(max = 100)
    private String nome;
    private TipoPessoaEnum tipoPessoa;
}
