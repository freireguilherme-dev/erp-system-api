package com.desafio_pleno.erp_system.mapper;

import com.desafio_pleno.erp_system.dto.ClienteDTO;
import com.desafio_pleno.erp_system.model.Cliente;

/**
 * Conversões Cliente ↔ ClienteDTO.
 * Mantém controllers e services enxutos e sem lógica de mapeamento.
 */
public final class ClienteMapper {

    private ClienteMapper() { }

    /** Entity -> DTO */
    public static ClienteDTO toDto(Cliente entity) {
        if (entity == null) return null;
        return ClienteDTO.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .email(entity.getEmail())
                .build();
    }

    /** DTO -> Entity (para criação). Ignora ID do DTO. */
    public static Cliente toNewEntity(ClienteDTO dto) {
        if (dto == null) return null;
        return Cliente.builder()
                .nome(dto.getNome())
                .email(dto.getEmail())
                .build();
    }

    /** Atualiza uma entidade existente a partir do DTO (para PUT). */
    public static void updateEntity(Cliente target, ClienteDTO dto) {
        if (target == null || dto == null) return;
        target.setNome(dto.getNome());
        target.setEmail(dto.getEmail());
    }
}
