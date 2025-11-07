package com.desafio_pleno.erp_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "clientes", indexes = {
        @Index(name = "idx_cliente_email", columnList = "email", unique = true)
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nome de exibição do cliente. */
    @NotBlank(message = "Nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    /** E-mail único para contato/identificação. */
    @NotBlank(message = "E-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    @Column(nullable = false, unique = true)
    private String email;

    /** Igualdade baseada no ID para entidades persistidas. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
