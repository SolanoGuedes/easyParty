package com.solanoguedes.OER.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seguidores")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Seguidor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario; // O usuário que está sendo seguido

    @ManyToOne
    @JoinColumn(name = "seguidor_id", nullable = false)
    private Usuario seguidor; // O usuário que está seguindo


    public Seguidor(Usuario usuario, Usuario seguidor) {
        this.usuario = usuario;
        this.seguidor = seguidor;
    }

}
