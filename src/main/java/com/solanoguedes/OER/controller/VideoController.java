package com.solanoguedes.OER.controller;

import com.solanoguedes.OER.model.Video;
import com.solanoguedes.OER.model.dto.VideoDTO;
import com.solanoguedes.OER.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.solanoguedes.OER.utils.UserUtil.getAuthenticatedUserId;

@RestController
@RequestMapping("/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    // Endpoint para fazer upload de um vídeo
    @PostMapping("/upload")
    public ResponseEntity<Video> uploadVideo(@RequestParam("file") MultipartFile file, @RequestParam("legenda") String legenda, @RequestParam("privacidade") String privacidade, @RequestParam("duracao") Integer duracao, @RequestParam("tipoVideo") String tipoVideo, @RequestParam(value = "expiraEm24Horas", defaultValue = "false") boolean expiraEm24Horas) {
        try {
            Long idUsuario = getAuthenticatedUserId();
            Video video = videoService.uploadVideo(file, legenda, idUsuario, privacidade, duracao, tipoVideo, expiraEm24Horas);
            return ResponseEntity.status(HttpStatus.CREATED).body(video);  // Retorna 201 Created
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);  // Retorna 400 Bad Request
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);  // Retorna 500 Internal Server Error
        }
    }

    // Endpoint para listar todos os vídeos públicos de um usuário
    @GetMapping("/publicos")
    public ResponseEntity<List<VideoDTO>> listarVideosPublicos(@RequestParam(required = false) Long idUsuario) {
        try {
            Long usuarioId = (idUsuario != null) ? idUsuario : getAuthenticatedUserId();
            List<VideoDTO> videosPublicos = videoService.listarVideosPublicos(usuarioId);
            return ResponseEntity.ok(videosPublicos);  // Retorna 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Retorna 404 Not Found
        }
    }

    // Endpoint para listar os vídeos privados do usuário autenticado
    @GetMapping("/privados")
    public ResponseEntity<List<VideoDTO>> listarVideosPrivados() {
        try {
            Long idUsuario = getAuthenticatedUserId();
            List<VideoDTO> videosPrivados = videoService.listarVideosPrivados(idUsuario);
            return ResponseEntity.ok(videosPrivados);  // Retorna 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Retorna 404 Not Found
        }
    }

    // Endpoint para alterar a privacidade de um vídeo
    @PutMapping("/{idVideo}/privacidade")
    public ResponseEntity<VideoDTO> alterarPrivacidadeVideo(
            @PathVariable Long idVideo,
            @RequestParam("privacidade") String novaPrivacidade) {
        try {
            VideoDTO videoDTO = videoService.alterarPrivacidadeVideo(idVideo, novaPrivacidade);
            return ResponseEntity.ok(videoDTO);  // Retorna 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Retorna 404 Not Found
        }
    }

    // Endpoint para buscar um vídeo pelo ID
    @GetMapping("/{idVideo}")
    public ResponseEntity<VideoDTO> obterVideoPorId(@PathVariable Long idVideo) {
        try {
            VideoDTO videoDTO = videoService.obterVideoPorId(idVideo);
            return ResponseEntity.ok(videoDTO);  // Retorna 200 OK se encontrado
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Retorna 404 Not Found
        }
    }

    // Endpoint para deletar um vídeo
    @DeleteMapping("/{idVideo}")
    public ResponseEntity<Void> deletarVideo(@PathVariable Long idVideo) {
        try {
            videoService.deletarVideo(idVideo);
            return ResponseEntity.noContent().build();  // Retorna 204 No Content
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Retorna 404 Not Found
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // Retorna 500 Internal Server Error
        }
    }
}
