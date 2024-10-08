// SeguidoDTO.java
package com.solanoguedes.OER.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SeguidoDTO {
    private Long id;
    private String nome;
    private String username;
    private String urlFotoPerfil;

    public SeguidoDTO(Long id, String nome, String username, String urlFotoPerfil) {
        this.id = id;
        this.nome = nome;
        this.username = username;
        this.urlFotoPerfil = urlFotoPerfil;
    }

}
