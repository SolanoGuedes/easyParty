package com.solanoguedes.OER.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "imagens")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Imagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idImagem;

    @NotNull(message = "O ID do usuário é obrigatório.")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @NotBlank(message = "A URL da imagem é obrigatória.")
    @Column(nullable = false)
    private String urlImagem;

    @Column(length = 255)
    private String legenda;

    @Column(nullable = false)
    private LocalDateTime dataPostagem = LocalDateTime.now();

    @Column(length = 10)
    private String formatoArquivo;

    private Integer tamanhoArquivo;

    private Integer larguraImagem;

    private Integer alturaImagem;

    // Armazena os metadados EXIF em formato JSON
    @Column(columnDefinition = "jsonb")
    private String exifMetadados;

    @Column(length = 10, nullable = false)
    private String status = "ativo";

    @Column(length = 20, nullable = false)
    private String privacidade = "publico";

    @Column(nullable = false)
    private Integer numeroCurtidas = 0;

    @Column(nullable = false)
    private Integer numeroComentarios = 0;

    // Lista de IDs de usuários marcados na imagem, armazenado em formato JSON
    @Column(columnDefinition = "jsonb")
    private String tagsUsuario;

    @Column(length = 255)
    private String tagsLocalizacao;

    @Column(length = 255)
    private String edicoesAplicadas;

    // Construtor que aceita um ID da imagem
    public Imagem(Long idImagem) {
        this.idImagem = idImagem;
    }
}
