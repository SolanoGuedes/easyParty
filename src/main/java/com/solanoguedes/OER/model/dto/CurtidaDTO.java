package com.solanoguedes.OER.model.dto;

import com.solanoguedes.OER.model.Curtida;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class CurtidaDTO {
    private Long id;
    private Long idUsuario;
    private Long idImagem;
    private Long idVideo;
    private LocalDateTime dataCurtida;

    public CurtidaDTO(Curtida curtida) {
        this.id = curtida.getId();
        this.idUsuario = curtida.getUsuario().getId();
        this.idImagem = curtida.getImagem() != null ? curtida.getImagem().getId() : null;
        this.idVideo = curtida.getVideo() != null ? curtida.getVideo().getId() : null;
        this.dataCurtida = curtida.getDataCurtida();
    }
}
