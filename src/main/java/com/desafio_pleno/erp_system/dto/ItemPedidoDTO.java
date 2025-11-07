package com.desafio_pleno.erp_system.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

/**
 * Item de entrada para criação de pedido.
 * Contém apenas o necessário para montar o item no domínio.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ItemPedidoDTO {

    @NotNull(message = "Produto é obrigatório")
    private Long produtoId;

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser > 0")
    private Integer quantidade;
}
