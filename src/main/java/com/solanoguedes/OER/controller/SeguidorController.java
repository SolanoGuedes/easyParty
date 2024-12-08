package com.solanoguedes.OER.controller;

import com.solanoguedes.OER.model.Seguidor;
import com.solanoguedes.OER.model.dto.SeguidoDTO;
import com.solanoguedes.OER.service.SeguidorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.solanoguedes.OER.utils.UserUtil.getAuthenticatedUserId;

@RestController
@RequestMapping("/seguidores")
public class SeguidorController {

    @Autowired
    private SeguidorService seguidorService;



    // Rota para seguir um usuário
    @PostMapping("/seguir")
    public ResponseEntity<?> seguirUsuario(@RequestParam Long usuarioId) throws Exception {
        Long seguidorId = getAuthenticatedUserId(); // Pega o ID do usuário autenticado
            Seguidor seguidor = seguidorService.seguirUsuario(usuarioId, seguidorId);
            return ResponseEntity.ok("O usuário seguiu com sucesso" + seguidor);
    }

    // Rota para listar seguidores de um usuário
    @GetMapping("/seguidores")
    public ResponseEntity<List<SeguidoDTO>> listarSeguidores(@RequestParam(required = false) Long usuario_id) {
        Long usuarioId;
        if (usuario_id != null) {
            usuarioId = usuario_id;
        } else {
            usuarioId = getAuthenticatedUserId();
        }
        List<SeguidoDTO> seguidores = seguidorService.listarSeguidores(usuarioId);
        return ResponseEntity.ok(seguidores);
    }


    // Rota para listar usuários que um usuário está seguindo
    @GetMapping("/seguindo")
    public ResponseEntity<List<SeguidoDTO>> listarSeguidos(@RequestParam(required = false) Long seguidor_id) {
        Long seguidorId;
        // Se seguidor_id for fornecido, use ele. Caso contrário, use o ID do usuário autenticado.
        if (seguidor_id != null) {
            seguidorId = seguidor_id;
        } else {
            seguidorId = getAuthenticatedUserId();
        }

        List<SeguidoDTO> seguidos = seguidorService.listarSeguidos(seguidorId);
        return ResponseEntity.ok(seguidos);
    }


    // Rota para desfazer o seguimento
    @DeleteMapping("/unfollow")
    public ResponseEntity<?> unfollowUsuario(@RequestParam Long usuarioId) throws Exception {
        Long seguidorId = getAuthenticatedUserId();
            seguidorService.unfollowUsuario(usuarioId, seguidorId);
            return ResponseEntity.ok("Usuário deixado de seguir com sucesso.");
    }
}
