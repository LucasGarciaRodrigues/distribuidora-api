package br.ufms.api.distribuidora.models;

public enum EstadoPedidoEnum {
    AGUARDANDO_PAGAMENTO,
    PAGAMENTO_CONFIRMADO,
    SAIU_PARA_ENTREGA,
    ENTREGUE,
    CANCELADO
}
