package com.solanoguedes.OER.service;

import com.solanoguedes.OER.model.Comentario;
import com.solanoguedes.OER.model.Imagem;
import com.solanoguedes.OER.model.Usuario;
import com.solanoguedes.OER.model.Video;
import com.solanoguedes.OER.model.dto.ComentarioDTO;
import com.solanoguedes.OER.repositories.ComentarioRepository;
import com.solanoguedes.OER.repositories.ImagemRepository;
import com.solanoguedes.OER.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private ImagemRepository imagemRepository;

    @Autowired
    private VideoRepository videoRepository;

    public Comentario comentarImagem(Long idImagem, Long idUsuario, String texto) {
        Imagem imagem = imagemRepository.findById(idImagem).orElseThrow(() -> new RuntimeException("Imagem não encontrada com ID: " + idImagem));
        Comentario comentario = new Comentario();
        comentario.setImagem(imagem);
        comentario.setUsuario(new Usuario(idUsuario));
        comentario.setTexto(texto);

        return comentarioRepository.save(comentario);
    }

    public Comentario comentarVideo(Long idVideo, Long idUsuario, String texto) {
        Video video = videoRepository.findById(idVideo).orElseThrow(() -> new RuntimeException("Vídeo não encontrado com ID: " + idVideo));
        Usuario usuario = new Usuario(idUsuario);
        Comentario comentario = new Comentario();
        comentario.setVideo(video);
        comentario.setUsuario(usuario);
        comentario.setTexto(texto);

        return comentarioRepository.save(comentario);
    }

    public List<ComentarioDTO> listarComentariosImagem(Long idImagem) {
        return comentarioRepository.findByImagemId(idImagem).stream()
                .map(ComentarioDTO::new)
                .collect(Collectors.toList());
    }

    public List<ComentarioDTO>  listarComentariosVideo(Long idVideo) {
        return comentarioRepository.findByVideoId(idVideo).stream()
                .map(ComentarioDTO::new)
                .collect(Collectors.toList());
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
