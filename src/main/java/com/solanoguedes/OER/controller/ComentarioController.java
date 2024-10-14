package com.solanoguedes.OER.controller;

import com.solanoguedes.OER.model.Comentario;
import com.solanoguedes.OER.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Comentario comentarImagem(@PathVariable Long idImagem, @RequestParam String texto) {
        Long idUsuario = getAuthenticatedUserId();
        return comentarioService.comentarImagem(idImagem, idUsuario, texto);
    }

    @PostMapping("/video/{idVideo}")
    public Comentario comentarVideo(@PathVariable Long idVideo, @RequestParam String texto) {
        Long idUsuario = getAuthenticatedUserId();
        return comentarioService.comentarVideo(idVideo, idUsuario, texto);
    }

    @GetMapping("/imagem/{idImagem}")
    public List<Comentario> listarComentariosImagem(@PathVariable Long idImagem) {
        return comentarioService.listarComentariosImagem(idImagem);
    }

    @GetMapping("/video/{idVideo}")
    public List<Comentario> listarComentariosVideo(@PathVariable Long idVideo) {
        return comentarioService.listarComentariosVideo(idVideo);
    }

    @DeleteMapping("/{idComentario}")
    public void removerComentario(@PathVariable Long idComentario) {
        Long idUsuario = getAuthenticatedUserId();  // Supondo que já tenha um método para pegar o usuário autenticado
        comentarioService.removerComentario(idComentario, idUsuario);
    }

    @GetMapping("/imagem/{idImagem}/top3")
    public ResponseEntity<List<Comentario>> listarTresPrimeirosComentariosImagem(@PathVariable Long idImagem) {
        List<Comentario> comentarios = comentarioService.listarTresPrimeirosComentariosImagem(idImagem);
        return ResponseEntity.ok(comentarios);
    }

    @GetMapping("/video/{idVideo}/top3")
    public ResponseEntity<List<Comentario>> listarTresPrimeirosComentariosVideo(@PathVariable Long idVideo) {
        List<Comentario> comentarios = comentarioService.listarTresPrimeirosComentariosVideo(idVideo);
        return ResponseEntity.ok(comentarios);
    }
}
