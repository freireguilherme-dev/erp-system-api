package com.desafio_pleno.erp_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "produtos", indexes = {
        @Index(name = "idx_produto_nome", columnList = "nome")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nome para busca e exibição. */
    @NotBlank(message = "Nome do produto é obrigatório")
    @Column(nullable = false)
    private String nome;

    /** Preço unitário. Nunca negativo. */
    @PositiveOrZero(message = "Preço não pode ser negativo")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal preco;

    /** Quantidade disponível. Nunca negativa. */
    @PositiveOrZero(message = "Estoque não pode ser negativo")
    @Column(nullable = false)
    private Integer estoque;

    /** Controle simples de disponibilidade. */
    @Column(nullable = false)
    private Boolean ativo = true;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Produto that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
