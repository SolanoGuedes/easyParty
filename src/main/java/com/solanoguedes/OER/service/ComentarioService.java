package com.solanoguedes.OER.service;

import com.solanoguedes.OER.model.Comentario;
import com.solanoguedes.OER.model.Imagem;
import com.solanoguedes.OER.model.Usuario;
import com.solanoguedes.OER.model.Video;
import com.solanoguedes.OER.repositories.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private ImagemService imagemService;

    @Autowired
    private VideoService videoService;

    public Comentario comentarImagem(Long idImagem, Long idUsuario, String texto) {
        Imagem imagem = imagemService.obterImagemPorId(idImagem);
        Usuario usuario = new Usuario(idUsuario);
        Comentario comentario = new Comentario();
        comentario.setImagem(imagem);
        comentario.setUsuario(usuario);
        comentario.setTexto(texto);

        return comentarioRepository.save(comentario);
    }

    public Comentario comentarVideo(Long idVideo, Long idUsuario, String texto) {
        Video video = videoService.obterVideoPorId(idVideo);
        Usuario usuario = new Usuario(idUsuario);
        Comentario comentario = new Comentario();
        comentario.setVideo(video);
        comentario.setUsuario(usuario);
        comentario.setTexto(texto);

        return comentarioRepository.save(comentario);
    }

    public List<Comentario> listarComentariosImagem(Long idImagem) {
        return comentarioRepository.findByImagemId(idImagem);
    }

    public List<Comentario> listarComentariosVideo(Long idVideo) {
        return comentarioRepository.findByVideoId(idVideo);
    }

    // Novo método para listar os 3 primeiros comentários de uma imagem
    public List<Comentario> listarTresPrimeirosComentariosImagem(Long idImagem) {
        Imagem imagem = new Imagem(idImagem); // Supondo que você tenha um construtor em Imagem
        Pageable pageable = PageRequest.of(0, 3); // Primeira página, 3 itens por página
        return comentarioRepository.findByImagem(imagem, pageable).getContent();
    }

    // Novo método para listar os 3 primeiros comentários de um vídeo
    public List<Comentario> listarTresPrimeirosComentariosVideo(Long idVideo) {
        Video video = new Video(idVideo); // Supondo que você tenha um construtor em Video
        Pageable pageable = PageRequest.of(0, 3); // Primeira página, 3 itens por página
        return comentarioRepository.findByVideo(video, pageable).getContent();
    }

    public void removerComentario(Long idComentario, Long idUsuario) {
        Comentario comentario = comentarioRepository.findById(idComentario)
                .orElseThrow(() -> new RuntimeException("Comentário não encontrado com ID: " + idComentario));

        if (!comentario.getUsuario().getId().equals(idUsuario)) {
            throw new RuntimeException("Você não tem permissão para remover este comentário.");
        }

        comentarioRepository.delete(comentario);
    }
}
