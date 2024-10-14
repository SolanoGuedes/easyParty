package com.solanoguedes.OER.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "curtidas")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Curtida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCurtida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_imagem")
    private Imagem imagem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_video")
    private Video video;

    private LocalDateTime dataCurtida = LocalDateTime.now();
}
