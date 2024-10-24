package com.solanoguedes.OER.controller;

import com.solanoguedes.OER.model.Comentario;
import com.solanoguedes.OER.model.dto.ComentarioDTO;
import com.solanoguedes.OER.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.solanoguedes.OER.utils.UserUtil.getAuthenticatedUserId;

@RestController
@RequestMapping("/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @PostMapping("/imagem/{idImagem}")
    public ResponseEntity<?> comentarImagem(@PathVariable Long idImagem, @RequestParam String texto) {
        try {
            Long idUsuario = getAuthenticatedUserId();
            Comentario comentario = comentarioService.comentarImagem(idImagem, idUsuario, texto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Comentario feito com sucesso: " +"'"+comentario.getTexto()+"'");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao comentar a imagem: " + e.getMessage());
        }
    }

    @PostMapping("/video/{idVideo}")
    public ResponseEntity<?> comentarVideo(@PathVariable Long idVideo, @RequestParam String texto) {
        try {
            Long idUsuario = getAuthenticatedUserId();
            Comentario comentario = comentarioService.comentarVideo(idVideo, idUsuario, texto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Comentario feito com sucesso: " +"'"+comentario.getTexto()+"'");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao comentar o vídeo: " + e.getMessage());
        }
    }

    @GetMapping("/imagem/{idImagem}")
    public ResponseEntity<?> listarComentariosImagem(@PathVariable Long idImagem) {
        try {
            List<ComentarioDTO> comentarios = comentarioService.listarComentariosImagem(idImagem);
            return ResponseEntity.ok(comentarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar comentários da imagem: " + e.getMessage());
        }
    }

    @GetMapping("/video/{idVideo}")
    public ResponseEntity<?> listarComentariosVideo(@PathVariable Long idVideo) {
        try {
            List<ComentarioDTO> comentarios = comentarioService.listarComentariosVideo(idVideo);
            return ResponseEntity.ok(comentarios);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao listar comentários do vídeo: " + e.getMessage());
        }
    }

    @DeleteMapping("/{idComentario}")
    public ResponseEntity<?> removerComentario(@PathVariable Long idComentario) {
        try {
            Long idUsuario = getAuthenticatedUserId();
            comentarioService.removerComentario(idComentario, idUsuario);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao remover o comentário: " + e.getMessage());
        }
    }

}
