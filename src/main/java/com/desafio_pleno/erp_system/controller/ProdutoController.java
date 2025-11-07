package com.desafio_pleno.erp_system.controller;

import com.desafio_pleno.erp_system.dto.ProdutoDTO;
import com.desafio_pleno.erp_system.exception.NotFoundException;
import com.desafio_pleno.erp_system.mapper.ProdutoMapper;
import com.desafio_pleno.erp_system.model.Produto;
import com.desafio_pleno.erp_system.repository.ProdutoRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.desafio_pleno.erp_system.mapper.ProdutoMapper.*;

@RestController
@RequestMapping("/api/produtos")
@Validated
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoRepository produtoRepository;

    @PostMapping
    public ProdutoDTO criar(@Valid @RequestBody ProdutoDTO dto) {
        var entity = toNewEntity(dto);
        entity = produtoRepository.save(entity);
        return toDto(entity);
    }

    @GetMapping
    public Page<ProdutoDTO> listar(@RequestParam(required = false) String nome,
                                   @RequestParam(required = false, defaultValue = "true") Boolean ativo,
                                   Pageable pageable) {
        var page = (nome == null || nome.isBlank())
                ? produtoRepository.findByAtivo(ativo, pageable)
                : produtoRepository.findByNomeContainingIgnoreCaseAndAtivo(nome, ativo, pageable);
        return page.map(ProdutoMapper::toDto);
    }

    @GetMapping("/{id}")
    public ProdutoDTO detalhar(@PathVariable Long id) {
        Produto p = produtoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));
        return toDto(p);
    }

    @PutMapping("/{id}")
    public ProdutoDTO atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoDTO dto) {
        var entity = produtoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));

        updateEntity(entity, dto);
        return toDto(produtoRepository.save(entity));
    }

    // PATCH rápido para ajustar estoque (subir/baixar)
    @PatchMapping("/{id}/estoque")
    public ProdutoDTO ajustarEstoque(@PathVariable Long id,
                                     @RequestParam Integer novoEstoque) {
        if (novoEstoque == null || novoEstoque < 0) {
            throw new IllegalArgumentException("novoEstoque deve ser >= 0");
        }
        Produto p = produtoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado"));
        p.setEstoque(novoEstoque);
        return toDto(produtoRepository.save(p));
    }

    // --- mapeamentos auxiliares ---

    private ProdutoDTO toDto(Produto p) {
        return ProdutoDTO.builder()
                .id(p.getId())
                .nome(p.getNome())
                .preco(p.getPreco())
                .estoque(p.getEstoque())
                .ativo(p.getAtivo())
                .build();
    }

    private Produto toEntity(ProdutoDTO dto) {
        return Produto.builder()
                .id(dto.getId())
                .nome(dto.getNome())
                .preco(dto.getPreco())
                .estoque(dto.getEstoque())
                .ativo(dto.getAtivo())
                .build();
    }
}