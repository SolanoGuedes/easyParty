package com.solanoguedes.OER.model.dto;

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
}
