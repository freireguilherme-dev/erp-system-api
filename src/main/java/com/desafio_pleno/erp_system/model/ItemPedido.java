package com.desafio_pleno.erp_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "itens_pedido")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Dono do item: Pedido (lado muitos). */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    /** Produto referenciado no item. */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    /** Quantidade solicitada. */
    @Positive(message = "Quantidade deve ser > 0")
    @Column(nullable = false)
    private Integer quantidade;

    /** Valor total deste item (preço * quantidade). */
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal subtotal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemPedido that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
