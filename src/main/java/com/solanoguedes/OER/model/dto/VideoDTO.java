package com.solanoguedes.OER.model.dto;

import com.solanoguedes.OER.model.Video; // Certifique-se de importar a classe Video
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDTO {
    private Long id;
    private String urlVideo;
    private String legenda;
    private LocalDateTime dataPostagem;
    private String privacidade;
    private Integer duracao;
    private Integer numeroCurtidas;
    private Integer numeroComentarios;
    private String tipoVideo;

    // Construtor que aceita um objeto Video
    public VideoDTO(Video video) {
        this.id = video.getId();
        this.urlVideo = video.getUrlVideo();
        this.legenda = video.getLegenda();
        this.dataPostagem = video.getDataPostagem();
        this.privacidade = video.getPrivacidade();
        this.duracao = video.getDuracao();
        this.numeroCurtidas = video.getNumeroCurtidas();
        this.numeroComentarios = video.getNumeroComentarios();
        this.tipoVideo = video.getTipoVideo();
    }
}
