package com.solanoguedes.OER.controller;

import com.solanoguedes.OER.model.Imagem;
import com.solanoguedes.OER.model.dto.ImagemDTO;
import com.solanoguedes.OER.service.ImagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    // Endpoint para fazer upload de uma imagem
    @PostMapping("/upload")
    public Imagem uploadImagem(@RequestParam("file") MultipartFile file, @RequestParam("legenda") String legenda, @RequestParam("privacidade") String privacidade,  @RequestParam(value = "expiraEm24Horas", defaultValue = "false") boolean expiraEm24Horas) throws IOException {
        Long idUsuario = getAuthenticatedUserId();
        return imagemService.uploadImagem(file, legenda, idUsuario, privacidade, expiraEm24Horas);
    }

    // Endpoint para listar todas as imagens públicas de um usuário (já existente)
    @GetMapping("/publicas")
    public List<ImagemDTO> listarImagensPublicas(@RequestParam(required = false) Long idUsuario) {
        Long usuarioId = (idUsuario != null) ? idUsuario : getAuthenticatedUserId();
        return imagemService.listarImagensPublicas(usuarioId);
    }

    // Novo endpoint para listar as imagens privadas do usuário autenticado
    @GetMapping("/privadas")
    public List<ImagemDTO> listarImagensPrivadas() {
        Long idUsuario = getAuthenticatedUserId();
        return imagemService.listarImagensPrivadas(idUsuario);
    }

    // Novo endpoint para buscar uma imagem específica pelo ID
    @GetMapping("/{idImagem}")
    public ImagemDTO obterImagemPorId(@PathVariable Long idImagem) {
        return imagemService.obterImagemPorIdDTO(idImagem);  // Altere para chamar o método que retorna o DTO
    }

    // Endpoint para alterar a privacidade de uma imagem
    @PutMapping("/{idImagem}/privacidade")
    public ResponseEntity<ImagemDTO> alterarPrivacidadeImagem(
            @PathVariable Long idImagem,
            @RequestParam("privacidade") String novaPrivacidade) {
        ImagemDTO imagemDTO = imagemService.alterarPrivacidadeImagem(idImagem, novaPrivacidade);
        return ResponseEntity.ok(imagemDTO);
    }


    // Novo endpoint para deletar uma imagem
    @DeleteMapping("deletar/{idImagem}")
    public void deletarImagem(@PathVariable Long idImagem) throws IOException {
        imagemService.deletarImagem(idImagem);
    }

}
