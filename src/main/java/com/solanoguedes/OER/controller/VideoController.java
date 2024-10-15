package com.solanoguedes.OER.controller;

import com.solanoguedes.OER.model.Video;
import com.solanoguedes.OER.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Video uploadVideo(@RequestParam("file") MultipartFile file, @RequestParam("legenda") String legenda, @RequestParam("privacidade") String privacidade, @RequestParam("duracao") Integer duracao, @RequestParam("tipoVideo") String tipoVideo, @RequestParam(value = "expiraEm24Horas", defaultValue = "false") boolean expiraEm24Horas) throws IOException {
        Long idUsuario = getAuthenticatedUserId();
        return videoService.uploadVideo(file, legenda, idUsuario, privacidade, duracao, tipoVideo, expiraEm24Horas);
    }

    // Endpoint para listar todos os vídeos públicos de um usuário
    @GetMapping("/publicos")
    public List<Video> listarVideosPublicos(@RequestParam(required = false) @PathVariable Long idUsuario) {
        Long usuario_id = (idUsuario != null) ? idUsuario : getAuthenticatedUserId();
        return videoService.listarVideosPublicos(usuario_id);
    }

    // Endpoint para listar os vídeos privados do usuário autenticado
    @GetMapping("/privados")
    public List<Video> listarVideosPrivados() {
        Long idUsuarioAutenticado = getAuthenticatedUserId();
        return videoService.listarVideosPrivados(idUsuarioAutenticado);
    }

    // Endpoint para buscar um vídeo pelo ID
    @GetMapping("/{idVideo}")
    public Video obterVideoPorId(@PathVariable Long idVideo) {
        return videoService.obterVideoPorId(idVideo);
    }

    // Endpoint para alterar a privacidade de um vídeo
    @PutMapping("/{idVideo}/privacidade")
    public Video alterarPrivacidadeVideo(@PathVariable Long idVideo, @RequestParam("privacidade") String novaPrivacidade) {
        return videoService.alterarPrivacidadeVideo(idVideo, novaPrivacidade);
    }

    // Endpoint para deletar um vídeo
    @DeleteMapping("/{idVideo}")
    public void deletarVideo(@PathVariable Long idVideo) throws IOException {
        videoService.deletarVideo(idVideo);
    }


}
