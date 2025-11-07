package com.desafio_pleno.erp_system.controller;

import com.desafio_pleno.erp_system.dto.ClienteDTO;
import com.desafio_pleno.erp_system.exception.BusinessException;
import com.desafio_pleno.erp_system.exception.NotFoundException;
import com.desafio_pleno.erp_system.mapper.ClienteMapper;
import com.desafio_pleno.erp_system.model.Cliente;
import com.desafio_pleno.erp_system.repository.ClienteRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.desafio_pleno.erp_system.mapper.ClienteMapper.*;

@RestController
@RequestMapping("/api/clientes")
@Validated
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteRepository clienteRepository;

    @PostMapping
    public ClienteDTO criar(@Valid @RequestBody ClienteDTO dto) {
        if (clienteRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new BusinessException("E-mail já cadastrado");
        }
        var entity = toNewEntity(dto);
        entity = clienteRepository.save(entity);
        return toDto(entity);
    }

    @GetMapping
    public Page<ClienteDTO> listar(@RequestParam(required = false) String nome, Pageable pageable) {
        var page = (nome == null || nome.isBlank())
                ? clienteRepository.findAll(pageable)
                : clienteRepository.findByNomeContainingIgnoreCase(nome, pageable);
        return page.map(ClienteMapper::toDto);
    }

    @GetMapping("/{id}")
    public ClienteDTO detalhar(@PathVariable Long id) {
        Cliente c = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));
        return toDto(c);
    }

    @PutMapping("/{id}")
    public ClienteDTO atualizar(@PathVariable Long id, @Valid @RequestBody ClienteDTO dto) {
        var entity = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));

        if (!entity.getEmail().equalsIgnoreCase(dto.getEmail())
                && clienteRepository.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new BusinessException("E-mail já cadastrado");
        }

        updateEntity(entity, dto);
        return toDto(clienteRepository.save(entity));
    }

    private ClienteDTO toDto(Cliente c) {
        return ClienteDTO.builder()
                .id(c.getId())
                .nome(c.getNome())
                .email(c.getEmail())
                .build();
    }
}
