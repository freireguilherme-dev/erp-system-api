package com.desafio_pleno.erp_system.dto;

import com.desafio_pleno.erp_system.model.ItemPedido;
import com.desafio_pleno.erp_system.model.Pedido;
import com.desafio_pleno.erp_system.model.StatusPedido;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * DTO de saída com visão completa do pedido.
 * Evite vazar entidades: mapeie campos explicitamente.
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PedidoDetalheDTO {

    private Long id;
    private Long clienteId;
    private String clienteNome;
    private Instant dataCriacao;
    private StatusPedido status;
    private BigDecimal total;
    private List<ItemDTO> itens;

    /** Construtor de conveniência a partir da entidade (para o desafio). */
    public PedidoDetalheDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.clienteId = pedido.getCliente().getId();
        this.clienteNome = pedido.getCliente().getNome();
        this.dataCriacao = pedido.getDataCriacao();
        this.status = pedido.getStatus();
        this.total = pedido.getTotal();
        this.itens = pedido.getItens().stream()
                .map(ItemDTO::from)
                .toList();
    }

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class ItemDTO {
        private Long produtoId;
        private String produtoNome;
        private Integer quantidade;
        private BigDecimal subtotal;

        public static ItemDTO from(ItemPedido item) {
            return ItemDTO.builder()
                    .produtoId(item.getProduto().getId())
                    .produtoNome(item.getProduto().getNome())
                    .quantidade(item.getQuantidade())
                    .subtotal(item.getSubtotal())
                    .build();
        }
    }
}
