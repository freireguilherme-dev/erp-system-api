package com.desafio_pleno.erp_system.controller;

import com.desafio_pleno.erp_system.dto.CriarPedidoDTO;
import com.desafio_pleno.erp_system.dto.PedidoDetalheDTO;
import com.desafio_pleno.erp_system.model.StatusPedido;
import com.desafio_pleno.erp_system.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Teste isolado do PedidoController, sem carregar o contexto inteiro.
 * Usa MockMvc para simular chamadas HTTP reais.
 */
@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Deve criar pedido com sucesso (HTTP 201)")
    void criarPedidoComSucesso() throws Exception {
        // Arrange (entrada simulada)
        var dto = new CriarPedidoDTO();
        dto.setClienteId(1L);
        dto.setItens(Collections.emptyList());

        var detalheMock = new PedidoDetalheDTO();
        detalheMock.setId(1L);
        detalheMock.setTotal(BigDecimal.valueOf(150.0));
        detalheMock.setStatus(StatusPedido.CRIADO);
        detalheMock.setDataCriacao(Instant.now());

        Mockito.when(pedidoService.criarPedido(Mockito.any(CriarPedidoDTO.class)))
                .thenReturn(detalheMock);

        // Act + Assert
        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.total").value(150.0))
                .andExpect(jsonPath("$.status").value("CRIADO"));
    }

    @Test
    @DisplayName("Deve retornar pedido por ID (HTTP 200)")
    void buscarPedidoPorId() throws Exception {
        // Arrange
        var detalhe = new PedidoDetalheDTO();
        detalhe.setId(10L);
        detalhe.setTotal(BigDecimal.valueOf(200));
        detalhe.setStatus(StatusPedido.CRIADO);
        detalhe.setDataCriacao(Instant.now());

        Mockito.when(pedidoService.buscarPorId(10L)).thenReturn(detalhe);

        // Act + Assert
        mockMvc.perform(get("/api/pedidos/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.total").value(200))
                .andExpect(jsonPath("$.status").value("CRIADO"));
    }
}
