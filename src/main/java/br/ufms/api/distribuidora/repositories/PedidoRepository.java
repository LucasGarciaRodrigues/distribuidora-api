package br.ufms.api.distribuidora.repositories;

import br.ufms.api.distribuidora.models.PedidoModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<PedidoModel, Long> {

}
