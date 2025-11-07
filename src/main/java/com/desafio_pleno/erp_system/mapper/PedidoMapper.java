package com.desafio_pleno.erp_system.mapper;

import com.desafio_pleno.erp_system.dto.*;
import com.desafio_pleno.erp_system.model.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper responsável pela conversão entre entidades e DTOs do domínio Pedido.
 * Mantém isolamento entre camadas (domínio ↔ API).
 *
 * Clean Code aplicado:
 * - Métodos estáticos (sem estado)
 * - Nomes autoexplicativos
 * - Conversões explícitas e seguras
 */
public final class PedidoMapper {

    // Construtor privado: classe utilitária
    private PedidoMapper() {}

    // ----------------------------------------------------------
    // ENTRADA → DOMÍNIO (DTO -> Entity)
    // ----------------------------------------------------------

    /**
     * Constrói um Pedido base (sem itens) a partir do DTO de criação.
     * O vínculo com cliente e itens é feito no serviço.
     */
    public static Pedido toEntityBase(CriarPedidoDTO dto, Cliente cliente) {
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.CRIADO);
        pedido.setTotal(BigDecimal.ZERO);
        return pedido;
    }

    /**
     * Converte uma lista de ItemPedidoDTO em entidades ItemPedido,
     * associando cada uma ao pedido e produto correspondentes.
     */
    public static List<ItemPedido> toItemEntities(List<ItemPedidoDTO> itensDto, Pedido pedido, List<Produto> produtos) {
        return itensDto.stream()
                .map(itemDto -> {
                    Produto produto = produtos.stream()
                            .filter(p -> p.getId().equals(itemDto.getProdutoId()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + itemDto.getProdutoId()));

                    ItemPedido item = new ItemPedido();
                    item.setPedido(pedido);
                    item.setProduto(produto);
                    item.setQuantidade(itemDto.getQuantidade());
                    item.setSubtotal(produto.getPreco().multiply(BigDecimal.valueOf(itemDto.getQuantidade())));
                    return item;
                })
                .collect(Collectors.toList());
    }

    // ----------------------------------------------------------
    // SAÍDA → API (Entity -> DTO)
    // ----------------------------------------------------------

    /**
     * Monta o DTO completo de detalhe do pedido, com todos os itens.
     */
    public static PedidoDetalheDTO toDetalheDTO(Pedido pedido) {
        return PedidoDetalheDTO.builder()
                .id(pedido.getId())
                .clienteId(pedido.getCliente().getId())
                .clienteNome(pedido.getCliente().getNome())
                .dataCriacao(pedido.getDataCriacao())
                .status(pedido.getStatus())
                .total(pedido.getTotal())
                .itens(pedido.getItens().stream()
                        .map(PedidoMapper::toItemDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * Cria um DTO de item de pedido (interno do detalhe).
     */
    private static PedidoDetalheDTO.ItemDTO toItemDTO(ItemPedido item) {
        return PedidoDetalheDTO.ItemDTO.builder()
                .produtoId(item.getProduto().getId())
                .produtoNome(item.getProduto().getNome())
                .quantidade(item.getQuantidade())
                .subtotal(item.getSubtotal())
                .build();
    }

    /**
     * Monta um DTO resumido (sem itens) — usado para listagens paginadas.
     */
    public static PedidoDetalheDTO toResumoDTO(Pedido pedido) {
        return PedidoDetalheDTO.builder()
                .id(pedido.getId())
                .clienteId(pedido.getCliente().getId())
                .clienteNome(pedido.getCliente().getNome())
                .dataCriacao(pedido.getDataCriacao())
                .status(pedido.getStatus())
                .total(pedido.getTotal())
                .build();
    }
}
