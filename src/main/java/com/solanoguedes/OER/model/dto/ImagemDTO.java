package com.solanoguedes.OER.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@NoArgsConstructor
@Data
public class ImagemDTO {
    private Long id;
    private LocalDateTime data_postagem;
    private String formato_arquivo;
    private String legenda;
    private Integer numero_comentarios;
    private Integer numero_curtidas;
    private String privacidade;
    private String status;
    private Integer tamanho_arquivo;
    private String url_imagem;
    private Long id_usuario;

    public ImagemDTO(Long id, LocalDateTime data_postagem, String formato_arquivo, String legenda, Integer numero_comentarios, Integer numero_curtidas, String privacidade, String status, Integer tamanho_arquivo, String url_imagem, Long id_usuario) {
        this.id = id;
        this.data_postagem = data_postagem;
        this.formato_arquivo = formato_arquivo;
        this.legenda = legenda;
        this.numero_comentarios = numero_comentarios;
        this.numero_curtidas = numero_curtidas;
        this.privacidade = privacidade;
        this.status = status;
        this.tamanho_arquivo = tamanho_arquivo;
        this.url_imagem = url_imagem;
        this.id_usuario = id_usuario;
    }
    public ImagemDTO(Long id, String privacidade, String url_imagem) {
        this.id = id;
        this.privacidade = privacidade;
        this.url_imagem = url_imagem;
    }
}
