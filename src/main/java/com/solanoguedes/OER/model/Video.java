package com.solanoguedes.OER.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "videos")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O ID do usuário é obrigatório.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @NotBlank(message = "A URL do vídeo é obrigatória.")
    @Column(nullable = false)
    private String urlVideo;

    @Column(length = 255)
    private String legenda;

    @Column(nullable = false)
    private LocalDateTime dataPostagem = LocalDateTime.now();

    @Column(length = 10)
    private String formatoArquivo;

    private Integer tamanhoArquivo;

    // Duração do vídeo em segundos
    @NotNull(message = "A duração do vídeo é obrigatória.")
    private Integer duracao;

    @Column(length = 10, nullable = false)
    private String status = "ativo";

    @Column(length = 20, nullable = false)
    private String privacidade = "publico";

    @Column(nullable = false)
    private Integer numeroCurtidas = 0;

    @Column(nullable = false)
    private Integer numeroComentarios = 0;

    @Column(length = 20, nullable = false)
    private String tipoVideo;  // "story" ou "página_inicial"

    @Transient // Isso não vai para o banco de dados
    private List<Comentario> comentarios; // Para armazenar os 3 primeiros comentários

    // Construtor que aceita um ID do vídeo
    public Video(Long idVideo) {
        this.id = idVideo;
    }
}
