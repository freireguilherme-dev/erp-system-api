package com.desafio_pleno.erp_system.repository;

import com.desafio_pleno.erp_system.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByEmailIgnoreCase(String email);

    Page<Cliente> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
