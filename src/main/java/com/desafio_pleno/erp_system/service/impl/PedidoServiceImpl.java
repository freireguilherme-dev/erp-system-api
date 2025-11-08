package com.desafio_pleno.erp_system.service.impl;

import com.desafio_pleno.erp_system.dto.CriarPedidoDTO;
import com.desafio_pleno.erp_system.dto.ItemPedidoDTO;
import com.desafio_pleno.erp_system.dto.PedidoDetalheDTO;
import com.desafio_pleno.erp_system.exception.BusinessException;
import com.desafio_pleno.erp_system.exception.NotFoundException;
import com.desafio_pleno.erp_system.mapper.PedidoMapper;
import com.desafio_pleno.erp_system.model.*;
import com.desafio_pleno.erp_system.repository.*;
import com.desafio_pleno.erp_system.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private static final String CLIENTE_NAO_ENCONTRADO = "Cliente não encontrado";
    private static final String PRODUTO_NAO_ENCONTRADO = "Produto não encontrado: ";
    private static final String ESTOQUE_INSUFICIENTE   = "Estoque insuficiente para o produto: ";

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    /**
     * Fluxo transacional:
     * 1) valida payload
     * 2) carrega cliente
     * 3) agrega quantidades por produto (evita repetição)
     * 4) carrega produtos em lote
     * 5) valida estoque/preço
     * 6) monta pedido + itens, debita estoque, calcula total
     * 7) persiste (pedido, itens, produtos)
     */
    @Override
    @Transactional
    public PedidoDetalheDTO criarPedido(final CriarPedidoDTO dto) {
        validarEntrada(dto);

        final Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new NotFoundException(CLIENTE_NAO_ENCONTRADO));

        // agrega quantidades por produtoId (protege contra itens repetidos no payload)
        final Map<Long, Integer> qtdPorProduto = agregaQuantidades(dto.getItens());

        // carrega todos os produtos em 1 consulta
        final List<Long> ids = new ArrayList<>(qtdPorProduto.keySet());
        final Map<Long, Produto> produtos = produtoRepository.findAllById(ids).stream()
                .collect(Collectors.toMap(Produto::getId, Function.identity()));

        // valida existência de todos os produtos
        for (Long idProduto : ids) {
            if (!produtos.containsKey(idProduto)) {
                throw new NotFoundException(PRODUTO_NAO_ENCONTRADO + idProduto);
            }
        }

        // instancia o pedido
        final Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setDataCriacao(Instant.now());
        pedido.setStatus(StatusPedido.CRIADO);

        final List<ItemPedido> itens = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        // monta itens, valida estoque (agregado) e debita
        for (Map.Entry<Long, Integer> entry : qtdPorProduto.entrySet()) {
            final Long produtoId = entry.getKey();
            final int quantidadeSolicitada = entry.getValue();

            final Produto produto = produtos.get(produtoId);

            if (quantidadeSolicitada <= 0) {
                throw new BusinessException("Quantidade inválida (<= 0) para o produto: " + produto.getNome());
            }
            if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException("Preço inválido para o produto: " + produto.getNome());
            }
            if (produto.getEstoque() < quantidadeSolicitada) {
                throw new BusinessException(ESTOQUE_INSUFICIENTE + produto.getNome());
            }

            // calcula subtotal e debita estoque
            final BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(quantidadeSolicitada));
            produto.setEstoque(produto.getEstoque() - quantidadeSolicitada);

            final ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(quantidadeSolicitada);
            item.setSubtotal(subtotal);
            itens.add(item);

            total = total.add(subtotal);
        }

        pedido.setItens(itens);
        pedido.setTotal(total);

        // persistência
        pedidoRepository.save(pedido);                // gera ID do pedido
        itemPedidoRepository.saveAll(itens);          // persiste itens (sem depender de cascade)
        produtoRepository.saveAll(new HashSet<>(      // salva cada produto APENAS UMA vez
                itens.stream().map(ItemPedido::getProduto).toList()
        ));

        // mapeia resposta (se tiver PedidoMapper, pode usar aqui)
        return new PedidoDetalheDTO(pedido);
    }

    @Override
    @Transactional(readOnly = true) // leitura explícita
    public PedidoDetalheDTO buscarPorId(final Long id) {
        final Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado"));
        return new PedidoDetalheDTO(pedido);
    }

    // ---------------- helpers ----------------

    private void validarEntrada(final CriarPedidoDTO dto) {
        if (dto == null) throw new BusinessException("Payload nulo para criação de pedido");
        if (dto.getClienteId() == null) throw new BusinessException("Cliente obrigatório");
        if (dto.getItens() == null || dto.getItens().isEmpty())
            throw new BusinessException("Pedido deve conter ao menos 1 item");
    }

    /** Soma quantidades por produtoId (protege contra itens duplicados). */
    private Map<Long, Integer> agregaQuantidades(List<ItemPedidoDTO> itens) {
        return itens.stream().collect(Collectors.toMap(
                ItemPedidoDTO::getProdutoId,
                ItemPedidoDTO::getQuantidade,
                Integer::sum          // se repetir produtoId, soma quantidades
        ));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PedidoDetalheDTO> listar(String clienteNome, StatusPedido status, Pageable pageable) {
        Page<Pedido> page;

        if (clienteNome != null && !clienteNome.isBlank()) {
            page = pedidoRepository.findByCliente_NomeContainingIgnoreCase(clienteNome, pageable);
        } else if (status != null) {
            page = pedidoRepository.findByStatus(status, pageable);
        } else {
            page = pedidoRepository.findAll(pageable);
        }

        return page.map(PedidoMapper::toResumoDTO);
    }
}
