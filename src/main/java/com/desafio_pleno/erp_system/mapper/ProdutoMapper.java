package com.desafio_pleno.erp_system.mapper;

import com.desafio_pleno.erp_system.dto.ProdutoDTO;
import com.desafio_pleno.erp_system.model.Produto;

/**
 * Conversões Produto ↔ ProdutoDTO.
 * Evita vazamento de entidade para a API e centraliza a transformação.
 */
public final class ProdutoMapper {

    private ProdutoMapper() { }

    /** Entity -> DTO */
    public static ProdutoDTO toDto(Produto entity) {
        if (entity == null) return null;
        return ProdutoDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .preco(entity.getPreco())
                .estoque(entity.getEstoque())
                .ativo(entity.getAtivo())
                .build();
    }

    /** DTO -> Entity (para criação). Ignora ID do DTO. */
    public static Produto toNewEntity(ProdutoDTO dto) {
        if (dto == null) return null;
        return Produto.builder()
                .nome(dto.getNome())
                .preco(dto.getPreco())
                .estoque(dto.getEstoque())
                .ativo(dto.getAtivo())
                .build();
    }

    /** Atualiza uma entidade existente a partir do DTO (para PUT/PATCH). */
    public static void updateEntity(Produto target, ProdutoDTO dto) {
        if (target == null || dto == null) return;
        target.setNome(dto.getNome());
        target.setPreco(dto.getPreco());
        target.setEstoque(dto.getEstoque());
        target.setAtivo(dto.getAtivo());
    }
}
