package com.solanoguedes.OER.repositories;

import com.solanoguedes.OER.model.Seguidor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeguidorRepository extends JpaRepository<Seguidor, Long> {
    List<Seguidor> findByUsuario_Id(Long usuarioId);
    List<Seguidor> findBySeguidor_Id(Long seguidorId);
    Optional<Seguidor> findByUsuario_IdAndSeguidor_Id(Long usuarioId, Long seguidorId);
    @Query("select us.nome, se.usuario_id from seguidores se join usuarios us on se.usuario_id = us.id;")
    List<String> findSeguidosBySeguidorId(@Param("seguidorId") Long seguidorId);
}