package com.desafio_pleno.erp_system.repository;

import com.desafio_pleno.erp_system.model.Pedido;
import com.desafio_pleno.erp_system.model.StatusPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Evita N+1 ao detalhar pedido com itens/produtos/cliente
    @Override
    @EntityGraph(attributePaths = {"itens", "itens.produto", "cliente"})
    Optional<Pedido> findById(Long id);

    Page<Pedido> findByCliente_NomeContainingIgnoreCase(String nome, Pageable pageable);

    Page<Pedido> findByStatus(StatusPedido status, Pageable pageable);
}
