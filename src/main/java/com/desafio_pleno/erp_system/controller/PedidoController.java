package com.desafio_pleno.erp_system.controller;

import com.desafio_pleno.erp_system.dto.CriarPedidoDTO;
import com.desafio_pleno.erp_system.dto.PedidoDetalheDTO;
import com.desafio_pleno.erp_system.mapper.PedidoMapper;
import com.desafio_pleno.erp_system.model.Pedido;
import com.desafio_pleno.erp_system.model.StatusPedido;
import com.desafio_pleno.erp_system.repository.PedidoRepository;
import com.desafio_pleno.erp_system.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
@Validated
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoRepository pedidoRepository;

    @PostMapping
    public PedidoDetalheDTO criar(@Valid @RequestBody CriarPedidoDTO dto) {
        return pedidoService.criarPedido(dto);
    }

    @GetMapping("/{id}")
    public PedidoDetalheDTO detalhar(@PathVariable Long id) throws ChangeSetPersister.NotFoundException {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        return PedidoMapper.toDetalheDTO(pedido);
    }

    // Lista paginada com filtros simples (clienteNome/status)
    @GetMapping
    public Page<PedidoDetalheDTO> listar(@RequestParam(required = false) String clienteNome,
                                         @RequestParam(required = false) StatusPedido status,
                                         Pageable pageable) {

        Page<Pedido> page;
        if (clienteNome != null && !clienteNome.isBlank()) {
            page = pedidoRepository.findByCliente_NomeContainingIgnoreCase(clienteNome, pageable);
        } else if (status != null) {
            page = pedidoRepository.findByStatus(status, pageable);
        } else {
            page = pedidoRepository.findAll(pageable);
        }

        // Mapeia cada Pedido para DTO de detalhe (carregado com EntityGraph no findById; aqui é listagem resumida)
        return page.map(p -> PedidoDetalheDTO.builder()
                .id(p.getId())
                .clienteId(p.getCliente().getId())
                .clienteNome(p.getCliente().getNome())
                .dataCriacao(p.getDataCriacao())
                .status(p.getStatus())
                .total(p.getTotal())
                .build());
    }
}
