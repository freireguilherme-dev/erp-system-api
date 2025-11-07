package com.desafio_pleno.erp_system.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

/**
 * Payload de criação de pedido.
 * Validações protegem a regra de negócio já na borda.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CriarPedidoDTO {

    @NotNull(message = "Cliente é obrigatório")
    private Long clienteId;

    @NotEmpty(message = "Pedido deve conter ao menos 1 item")
    @Valid
    private List<ItemPedidoDTO> itens;
}
