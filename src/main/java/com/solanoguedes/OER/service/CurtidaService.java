package com.solanoguedes.OER.service;

import com.solanoguedes.OER.model.Curtida;
import com.solanoguedes.OER.model.Imagem;
import com.solanoguedes.OER.model.Usuario;
import com.solanoguedes.OER.model.Video;
import com.solanoguedes.OER.repositories.CurtidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurtidaService {

    @Autowired
    private CurtidaRepository curtidaRepository;

    @Autowired
    private ImagemService imagemService;

    @Autowired
    private VideoService videoService;

    public Curtida curtirImagem(Long idImagem, Long idUsuario) {
        Imagem imagem = imagemService.obterImagemPorId(idImagem);
        Usuario usuario = new Usuario(idUsuario);
        Curtida curtida = new Curtida();
        curtida.setImagem(imagem);
        curtida.setUsuario(usuario);

        return curtidaRepository.save(curtida);
    }

    public Curtida curtirVideo(Long idVideo, Long idUsuario) {
        Video video = videoService.obterVideoPorId(idVideo);
        Usuario usuario = new Usuario(idUsuario);
        Curtida curtida = new Curtida();
        curtida.setVideo(video);
        curtida.setUsuario(usuario);

        return curtidaRepository.save(curtida);
    }

    public List<Curtida> listarCurtidasImagem(Long idImagem) {
        return curtidaRepository.findByImagemId(idImagem);
    }

    public List<Curtida> listarCurtidasVideo(Long idVideo) {
        return curtidaRepository.findByVideoId(idVideo);
    }

    public void removerCurtida(Long idCurtida, Long idUsuario) {
        Curtida curtida = curtidaRepository.findById(idCurtida)
                .orElseThrow(() -> new RuntimeException("Curtida não encontrada com ID: " + idCurtida));

        if (!curtida.getUsuario().getId().equals(idUsuario)) {
            throw new RuntimeException("Você não tem permissão para remover esta curtida.");
        }

        curtidaRepository.delete(curtida);
    }
}
