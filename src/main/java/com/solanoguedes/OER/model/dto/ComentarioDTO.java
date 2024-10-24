package com.solanoguedes.OER.model.dto;

import com.solanoguedes.OER.model.Comentario;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;



@NoArgsConstructor
@Data
public class ComentarioDTO {

    private Long id;
    private Long idUsuario;
    private Long idImagem;
    private Long idVideo;
    private String texto;
    private LocalDateTime dataComentario = LocalDateTime.now();

    public ComentarioDTO(Comentario comentario){
        this.id = comentario.getId();
        this.idUsuario = comentario.getUsuario().getId();
        this.idImagem = comentario.getImagem() != null ? comentario.getImagem().getId() : null;
        this.idVideo = comentario.getVideo() != null ? comentario.getVideo().getId() : null;
        this.texto = comentario.getTexto();
        this.dataComentario = comentario.getDataComentario();
    }
}
