package com.solanoguedes.OER.repositories;

import com.solanoguedes.OER.model.Seguidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeguidorRepository extends JpaRepository<Seguidor, Long> {
    // Método para encontrar todos os seguidores de um usuário
    List<Seguidor> findByUsuario_Id(Long usuarioId);

    // Método para encontrar todos os usuários que estão seguindo um usuário específico
    List<Seguidor> findBySeguidor_Id(Long seguidorId);

    Optional<Seguidor> findByUsuario_IdAndSeguidor_Id(Long usuarioId, Long seguidorId);
}