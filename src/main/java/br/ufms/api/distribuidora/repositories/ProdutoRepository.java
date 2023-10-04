package br.ufms.api.distribuidora.repositories;

import br.ufms.api.distribuidora.models.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository <ProdutoModel, Long> {

}
