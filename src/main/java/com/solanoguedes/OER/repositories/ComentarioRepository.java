package com.solanoguedes.OER.repositories;

import com.solanoguedes.OER.model.Comentario;
import com.solanoguedes.OER.model.Imagem;
import com.solanoguedes.OER.model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByImagemId(Long idImagem);
    List<Comentario> findByVideoId(Long idVideo);
    int countByVideo(Video video);
    int countByImagem(Imagem imagem);

    // Método para buscar os 3 primeiros comentários de uma imagem com paginação
    Page<Comentario> findByImagem(Imagem imagem, Pageable pageable);

    // Método para buscar os 3 primeiros comentários de um vídeo com paginação
    Page<Comentario> findByVideo(Video video, Pageable pageable);
}
