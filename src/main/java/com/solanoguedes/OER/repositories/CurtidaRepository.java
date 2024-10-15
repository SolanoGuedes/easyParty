package com.solanoguedes.OER.repositories;

import com.solanoguedes.OER.model.Curtida;
import com.solanoguedes.OER.model.Imagem;
import com.solanoguedes.OER.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CurtidaRepository extends JpaRepository<Curtida, Long> {
    List<Curtida> findByImagemId(Long idImagem);
    List<Curtida> findByVideoId(Long idVideo);
    int countByVideo(Video video);
    int countByImagem(Imagem imagem);
    boolean existsByImagemAndUsuarioId(Imagem imagem, Long usuarioId);
}
