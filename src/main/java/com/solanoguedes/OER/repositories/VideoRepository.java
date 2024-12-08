package com.solanoguedes.OER.repositories;

import com.solanoguedes.OER.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findByUsuarioIdAndPrivacidade(Long usuarioId, String privacidade);
}
