package com.solanoguedes.OER.service;

import com.solanoguedes.OER.model.Seguidor;
import com.solanoguedes.OER.model.Usuario;
import com.solanoguedes.OER.model.dto.SeguidoDTO;
import com.solanoguedes.OER.repositories.SeguidorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeguidorService {

    @Autowired
    private SeguidorRepository seguidorRepository;

    // Método para seguir um usuário
    public Seguidor seguirUsuario(Long usuarioId, Long seguidorId) throws Exception {
        // Verifica se o seguidor já está seguindo o usuário
        Optional<Seguidor> existingFollow = seguidorRepository.findByUsuario_IdAndSeguidor_Id(usuarioId, seguidorId);
        if (existingFollow.isPresent()) {
            throw new Exception("Usuário já está seguindo este usuário.");
        }

        Seguidor seguidor = new Seguidor();
        seguidor.setUsuario(new Usuario(usuarioId));
        seguidor.setSeguidor(new Usuario(seguidorId));
        return seguidorRepository.save(seguidor);
    }

    // Método para listar seguidores de um usuário
    public List<SeguidoDTO> listarSeguidores(Long usuarioId) {
        return seguidorRepository.buscarSeguidores(usuarioId);
    }

    // Método para listar usuários que um usuário está seguindo
    public List<SeguidoDTO> listarSeguidos(Long seguidorId) {
        return seguidorRepository.buscarSeguidos(seguidorId);
    }

    // Método para remover um seguidor
    public void unfollowUsuario(Long usuarioId, Long seguidorId) throws Exception {
        Optional<Seguidor> seguidor = seguidorRepository.findByUsuario_IdAndSeguidor_Id(usuarioId, seguidorId);
        if (seguidor.isPresent()) {
            seguidorRepository.delete(seguidor.get());
        } else {
            throw new Exception("Relação de seguir não encontrada.");
        }
    }

}
