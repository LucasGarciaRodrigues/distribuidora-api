package br.ufms.api.distribuidora.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ProdutoDto {
    @NotBlank
    @Size(max = 100)
    private String nome;
    @NotNull
    private Double preco;
}
