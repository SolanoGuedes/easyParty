package com.solanoguedes.OER.repositories;

import com.solanoguedes.OER.model.Seguidor;
import com.solanoguedes.OER.model.dto.SeguidoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeguidorRepository extends JpaRepository<Seguidor, Long> {

    Optional<Seguidor> findByUsuario_IdAndSeguidor_Id(Long usuarioId, Long seguidorId);

    //Pega dados especificos de quem estou seguindo
    @Query("SELECT new com.solanoguedes.OER.model.dto.SeguidoDTO(u.id, u.nome, u.username, u.urlFotoPerfil) " + "FROM Seguidor s JOIN s.usuario u " + "WHERE s.seguidor.id = :seguidorId")
    List<SeguidoDTO> buscarSeguidos(@Param("seguidorId") Long seguidorId);

    //Pega dados expessificos dos seguidores
    @Query("SELECT new com.solanoguedes.OER.model.dto.SeguidoDTO(s.seguidor.id, s.seguidor.nome, s.seguidor.username, s.seguidor.urlFotoPerfil) " + "FROM Seguidor s " + "WHERE s.usuario.id = :usuarioId")
    List<SeguidoDTO> buscarSeguidores(@Param("usuarioId") Long usuarioId);

}