package com.desafio_pleno.erp_system.service;

import com.desafio_pleno.erp_system.dto.CriarPedidoDTO;
import com.desafio_pleno.erp_system.dto.PedidoDetalheDTO;

/**
 * Camada de regras de negócio do módulo de Pedidos.
 * Mantém o contrato de alto nível para criação e consulta.
 */
public interface PedidoService {

    /**
     * Cria um pedido aplicando regras de estoque, cálculo de totais e persistência atômica.
     * @param dto dados necessários para criar o pedido (cliente + itens)
     * @return detalhes do pedido persistido
     */
    PedidoDetalheDTO criarPedido(CriarPedidoDTO dto);

    /**
     * Busca um pedido pelo id (read-only).
     * @param id identificador do pedido
     * @return detalhes do pedido
     */
    PedidoDetalheDTO buscarPorId(Long id);
}
