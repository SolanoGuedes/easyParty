package com.solanoguedes.OER.controller;

import com.solanoguedes.OER.model.Imagem;
import com.solanoguedes.OER.service.ImagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.solanoguedes.OER.utils.UserUtil.getAuthenticatedUserId;

@RestController
@RequestMapping("/imagens")
public class ImagemController {

    @Autowired
    private ImagemService imagemService;

    // Endpoint para fazer upload de uma imagem (já existente)
    @PostMapping("/upload")
    public Imagem uploadImagem(@RequestParam("file") MultipartFile file, @RequestParam("legenda") String legenda, @RequestParam("privacidade") String privacidade) throws IOException {
        Long idUsuario = getAuthenticatedUserId();
        return imagemService.uploadImagem(file, legenda, idUsuario, privacidade);
    }

    // Endpoint para listar todas as imagens públicas de um usuário (já existente)
    @GetMapping("/publicas")
    public List<Imagem> listarImagensPublicas(@RequestParam(required = false) @PathVariable Long idUsuario) {
        Long usuario_id;
        if (idUsuario != null){
            usuario_id = idUsuario;
        } else {
            usuario_id = getAuthenticatedUserId();
        }
        return imagemService.listarImagensPublicas(usuario_id);
    }

    // Novo endpoint para listar as imagens privadas do usuário autenticado
    @GetMapping("/privadas")
    public List<Imagem> listarImagensPrivadas() {
        Long idUsuarioAutenticado = getAuthenticatedUserId();
        if (idUsuarioAutenticado == null) {
            throw new RuntimeException("Usuário não autenticado");
        }
        return imagemService.listarImagensPrivadas(idUsuarioAutenticado);
    }

    // Novo endpoint para buscar uma imagem específica pelo ID
    @GetMapping("/{idImagem}")
    public Imagem obterImagemPorId(@PathVariable Long idImagem) {
        return imagemService.obterImagemPorId(idImagem);
    }

    // Novo endpoint para deletar uma imagem
    @DeleteMapping("/{idImagem}")
    public void deletarImagem(@PathVariable Long idImagem) throws IOException {
        imagemService.deletarImagem(idImagem);
    }
}