package com.solanoguedes.OER.controller;

import com.solanoguedes.OER.model.Seguidor;
import com.solanoguedes.OER.model.Usuario;
import com.solanoguedes.OER.service.SeguidorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seguidores")
public class SeguidorController {

    @Autowired
    private SeguidorService seguidorService;

    // Método para obter o ID do usuário autenticado
    private Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                return ((Usuario) principal).getId(); // Certifique-se de que o principal seja o objeto correto
            }
        }
        return null; // Retorna null se não autenticado
    }



    // Rota para seguir um usuário
    @PostMapping("/seguir")
    public ResponseEntity<?> seguirUsuario(@RequestParam Long usuarioId) {
        Long seguidorId = getAuthenticatedUserId(); // Pega o ID do usuário autenticado
        try {
            Seguidor seguidor = seguidorService.seguirUsuario(usuarioId, seguidorId);
            return ResponseEntity.ok(seguidor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao seguir usuário: " + e.getMessage());
        }
    }

    // Rota para listar seguidores de um usuário
    @GetMapping("/seguidores/{usuarioId}")
    public ResponseEntity<List<Seguidor>> listarSeguidores(@PathVariable Long usuarioId) {
        List<Seguidor> seguidores = seguidorService.listarSeguidores(usuarioId);
        return ResponseEntity.ok(seguidores);
    }

    // Rota para listar usuários que um usuário está seguindo
    @GetMapping("/seguindo")
    public ResponseEntity<List<Seguidor>> listarSeguidos() {
        Long seguidorId = getAuthenticatedUserId(); // Pega o ID do usuário autenticado
        List<Seguidor> seguidos = seguidorService.listarSeguidos(seguidorId);
        return ResponseEntity.ok(seguidos);
    }

    // Rota para desfazer o seguimento
    @DeleteMapping("/unfollow")
    public ResponseEntity<?> unfollowUsuario(@RequestParam Long usuarioId) {
        Long seguidorId = getAuthenticatedUserId(); // Pega o ID do usuário autenticado
        try {
            seguidorService.unfollowUsuario(usuarioId, seguidorId);
            return ResponseEntity.ok("Usuário deixado de seguir com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao deixar de seguir usuário: " + e.getMessage());
        }
    }
}
