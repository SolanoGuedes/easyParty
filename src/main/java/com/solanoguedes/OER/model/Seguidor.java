package com.solanoguedes.OER.model;

import jakarta.persistence.*;

@Entity
@Table(name = "seguidores")
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

    // Construtores
    public Seguidor() {}

    public Seguidor(Usuario usuario, Usuario seguidor) {
        this.usuario = usuario;
        this.seguidor = seguidor;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getSeguidor() {
        return seguidor;
    }

    public void setSeguidor(Usuario seguidor) {
        this.seguidor = seguidor;
    }
}
