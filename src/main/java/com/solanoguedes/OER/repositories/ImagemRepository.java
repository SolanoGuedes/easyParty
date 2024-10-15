package com.solanoguedes.OER.repositories;

import com.solanoguedes.OER.model.Imagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ImagemRepository extends JpaRepository<Imagem, Long> {
    List<Imagem> findByUsuario_IdAndPrivacidade(Long usuarioId, String privacidade);
}
