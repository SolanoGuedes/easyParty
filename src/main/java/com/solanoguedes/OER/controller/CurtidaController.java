package com.solanoguedes.OER.controller;

import com.solanoguedes.OER.model.Curtida;
import com.solanoguedes.OER.service.CurtidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.solanoguedes.OER.utils.UserUtil.getAuthenticatedUserId;

@RestController
@RequestMapping("/curtidas")
public class CurtidaController {

    @Autowired
    private CurtidaService curtidaService;

    @PostMapping("/imagem/{idImagem}")
    public Curtida curtirImagem(@PathVariable Long idImagem) {
        Long idUsuario = getAuthenticatedUserId(); // Supondo que já tenha um método para pegar o usuário autenticado
        return curtidaService.curtirImagem(idImagem, idUsuario);
    }

    @PostMapping("/video/{idVideo}")
    public Curtida curtirVideo(@PathVariable Long idVideo) {
        Long idUsuario = getAuthenticatedUserId();
        return curtidaService.curtirVideo(idVideo, idUsuario);
    }

    @GetMapping("/imagem/{idImagem}")
    public List<Curtida> listarCurtidasImagem(@PathVariable Long idImagem) {
        return curtidaService.listarCurtidasImagem(idImagem);
    }

    @GetMapping("/video/{idVideo}")
    public List<Curtida> listarCurtidasVideo(@PathVariable Long idVideo) {
        return curtidaService.listarCurtidasVideo(idVideo);
    }

    @DeleteMapping("/{idCurtida}")
    public void removerCurtida(@PathVariable Long idCurtida) {
        Long idUsuario = getAuthenticatedUserId();  // Supondo que já tenha um método para pegar o usuário autenticado
        curtidaService.removerCurtida(idCurtida, idUsuario);
    }
}
