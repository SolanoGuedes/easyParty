package com.solanoguedes.OER.controller;

import com.solanoguedes.OER.model.Imagem;
import com.solanoguedes.OER.model.dto.ImagemDTO;
import com.solanoguedes.OER.service.ImagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Imagem> uploadImagem(
            @RequestParam("file") MultipartFile file,
            @RequestParam("legenda") String legenda,
            @RequestParam("privacidade") String privacidade,
            @RequestParam(value = "expiraEm24Horas", defaultValue = "false") boolean expiraEm24Horas) throws IOException {

        Long idUsuario = getAuthenticatedUserId();
        Imagem imagem = imagemService.uploadImagem(file, legenda, idUsuario, privacidade, expiraEm24Horas);
        return ResponseEntity.status(HttpStatus.CREATED).body(imagem);  // Retorna 201 Created
    }

    // Endpoint para listar todas as imagens públicas de um usuário
    @GetMapping("/publicas")
    public ResponseEntity<List<ImagemDTO>> listarImagensPublicas(@RequestParam(required = false) Long idUsuario) {
        Long usuarioId = (idUsuario != null) ? idUsuario : getAuthenticatedUserId();
        List<ImagemDTO> imagensPublicas = imagemService.listarImagensPublicas(usuarioId);
        return ResponseEntity.ok(imagensPublicas);  // Retorna 200 OK
    }

    // Endpoint para listar as imagens privadas do usuário autenticado
    @GetMapping("/privadas")
    public ResponseEntity<List<ImagemDTO>> listarImagensPrivadas() {
        Long idUsuario = getAuthenticatedUserId();
        List<ImagemDTO> imagensPrivadas = imagemService.listarImagensPrivadas(idUsuario);
        return ResponseEntity.ok(imagensPrivadas);  // Retorna 200 OK
    }

    // Endpoint para buscar uma imagem específica pelo ID
    @GetMapping("/{idImagem}")
    public ResponseEntity<ImagemDTO> obterImagemPorId(@PathVariable Long idImagem) {
        try {
            ImagemDTO imagemDTO = imagemService.obterImagemPorIdDTO(idImagem);
            return ResponseEntity.ok(imagemDTO);  // Retorna 200 OK se encontrada
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Retorna 404 Not Found
        }
    }

    // Endpoint para alterar a privacidade de uma imagem
    @PutMapping("/{idImagem}/privacidade")
    public ResponseEntity<ImagemDTO> alterarPrivacidadeImagem(
            @PathVariable Long idImagem,
            @RequestParam("privacidade") String novaPrivacidade) {

        try {
            ImagemDTO imagemDTO = imagemService.alterarPrivacidadeImagem(idImagem, novaPrivacidade);
            return ResponseEntity.ok(imagemDTO);  // Retorna 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // Retorna 404 Not Found
        }
    }

    // Endpoint para deletar uma imagem
    @DeleteMapping("/deletar/{idImagem}")
    public ResponseEntity<Void> deletarImagem(@PathVariable Long idImagem) {
        try {
            imagemService.deletarImagem(idImagem);
            return ResponseEntity.noContent().build();  // Retorna 204 No Content
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // Retorna 500 Internal Server Error
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Retorna 404 Not Found
        }
    }
}
