package com.desafio_pleno.erp_system.repository;

import com.desafio_pleno.erp_system.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Page<Produto> findByNomeContainingIgnoreCaseAndAtivo(String nome, Boolean ativo, Pageable pageable);

    Page<Produto> findByAtivo(Boolean ativo, Pageable pageable);

    Optional<Produto> findByIdAndAtivoTrue(Long id);
}
