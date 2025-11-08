package com.desafio_pleno.erp_system.controller;

import com.desafio_pleno.erp_system.dto.CriarPedidoDTO;
import com.desafio_pleno.erp_system.dto.PedidoDetalheDTO;
import com.desafio_pleno.erp_system.model.StatusPedido;
import com.desafio_pleno.erp_system.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
@Validated
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService; // ✅ só service

    @PostMapping
    public PedidoDetalheDTO criar(@Valid @RequestBody CriarPedidoDTO dto) {
        return pedidoService.criarPedido(dto);
    }

    @GetMapping("/{id}")
    public PedidoDetalheDTO detalhar(@PathVariable Long id) {
        return pedidoService.buscarPorId(id);
    }

    @GetMapping
    public <PedidoResumoDTO> Page<PedidoResumoDTO> listar(@RequestParam(required = false) String clienteNome,
                                                          @RequestParam(required = false) StatusPedido status,
                                                          Pageable pageable) {
        return pedidoService.listar(clienteNome, status, pageable);
    }
}
