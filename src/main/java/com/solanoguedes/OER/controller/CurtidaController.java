package com.solanoguedes.OER.controller;

import com.solanoguedes.OER.model.Curtida;
import com.solanoguedes.OER.model.dto.CurtidaDTO;
import com.solanoguedes.OER.service.CurtidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.solanoguedes.OER.utils.UserUtil.getAuthenticatedUserId;

@RestController
@RequestMapping("/curtidas")
public class CurtidaController {

    @Autowired
    private CurtidaService curtidaService;

    @PostMapping("/imagem/{idImagem}")
    public ResponseEntity<?> curtirImagem(@PathVariable Long idImagem) {
        try {
            Long idUsuario = getAuthenticatedUserId();
            curtidaService.curtirImagem(idImagem, idUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body("Imagem curtida com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao curtir a imagem: " + e.getMessage());
        }
    }

    @PostMapping("/video/{idVideo}")
    public ResponseEntity<?> curtirVideo(@PathVariable Long idVideo) {
        try {
            Long idUsuario = getAuthenticatedUserId();
            curtidaService.curtirVideo(idVideo, idUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body("Video curtido com sucesso");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao curtir o vídeo: " + e.getMessage());
        }
    }

    @GetMapping("/imagem/{idImagem}")
    public ResponseEntity<?> listarCurtidasImagem(@PathVariable Long idImagem) {
        try {
            List<CurtidaDTO> curtidas = curtidaService.listarCurtidasImagem(idImagem);
            return ResponseEntity.ok(curtidas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar curtidas da imagem: " + e.getMessage());
        }
    }

    @GetMapping("/video/{idVideo}")
    public ResponseEntity<?> listarCurtidasVideo(@PathVariable Long idVideo) {
        try {
            List<CurtidaDTO> curtidas = curtidaService.listarCurtidasVideo(idVideo);
            return ResponseEntity.ok(curtidas);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar curtidas do vídeo: " + e.getMessage());
        }
    }


    @DeleteMapping("/{idCurtida}")
    public ResponseEntity<?> removerCurtida(@PathVariable Long idCurtida) {
        try {
            Long idUsuario = getAuthenticatedUserId();
            curtidaService.removerCurtida(idCurtida, idUsuario);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao remover curtida: " + e.getMessage());
        }
    }
}
