package com.desafio_pleno.erp_system.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "pedidos", indexes = {
        @Index(name = "idx_pedido_cliente_data", columnList = "cliente_id, data_criacao")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Agregado pertence a um cliente. */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    /** Momento da criação (UTC). */
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private Instant dataCriacao;

    /** Soma dos subtotais dos itens. */
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal total;

    /** Estado atual do pedido. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPedido status;

    /**
     * Raiz do agregado → itens.
     * Cascade e orphanRemoval permitem salvar/remover via "pedido" de forma simples.
     */
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();

    public void adicionarItem(ItemPedido item) {
        itens.add(item);
        item.setPedido(this);
    }

    public void removerItem(ItemPedido item) {
        itens.remove(item);
        item.setPedido(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pedido that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
