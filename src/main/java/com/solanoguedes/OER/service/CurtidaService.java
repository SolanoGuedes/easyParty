package com.solanoguedes.OER.service;

import com.solanoguedes.OER.model.Curtida;
import com.solanoguedes.OER.model.Imagem;
import com.solanoguedes.OER.model.Usuario;
import com.solanoguedes.OER.model.Video;
import com.solanoguedes.OER.model.dto.CurtidaDTO;
import com.solanoguedes.OER.model.dto.ImagemDTO;
import com.solanoguedes.OER.repositories.CurtidaRepository;
import com.solanoguedes.OER.repositories.ImagemRepository;
import com.solanoguedes.OER.repositories.UsuarioRepository;
import com.solanoguedes.OER.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurtidaService {

    @Autowired
    private ImagemRepository imagemRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CurtidaRepository curtidaRepository;

    @Autowired
    private ImagemService imagemService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private UsuarioRepository usuarioRepository;


    public Curtida curtirImagem(Long idImagem, Long idUsuario) {
        // Busca a imagem no repositório pelo ID
        Imagem imagem = imagemRepository.findById(idImagem)
                .orElseThrow(() -> new RuntimeException("Imagem não encontrada com ID: " + idImagem));

        // Verifica se o usuário já curtiu essa imagem para evitar duplicatas
        boolean jaCurtiu = curtidaRepository.existsByImagemAndUsuarioId(imagem, idUsuario);
        if (jaCurtiu) {
            throw new RuntimeException("O usuário já curtiu esta imagem.");
        }

        Curtida curtida = new Curtida();
        curtida.setImagem(imagem);
        curtida.setUsuario(new Usuario(idUsuario));
        curtida.setDataCurtida(LocalDateTime.now());

        return curtidaRepository.save(curtida);
    }

    public Curtida curtirVideo(Long idVideo, Long idUsuario) {
        // Busca o vídeo no repositório pelo ID
        Video video = videoRepository.findById(idVideo)
                .orElseThrow(() -> new RuntimeException("Vídeo não encontrado com ID: " + idVideo));

        // Verifica se o usuário já curtiu este vídeo
        boolean jaCurtiu = curtidaRepository.existsByVideoAndUsuarioId(video, idUsuario);
        if (jaCurtiu) {
            throw new RuntimeException("O usuário já curtiu este vídeo.");
        }

        Curtida curtida = new Curtida();
        curtida.setVideo(video);
        curtida.setUsuario(new Usuario(idUsuario));


        return curtidaRepository.save(curtida);
    }


    public List<CurtidaDTO> listarCurtidasImagem(Long idImagem) {
        return curtidaRepository.findByImagemId(idImagem).stream()
                .map(CurtidaDTO::new)
                .collect(Collectors.toList());
    }

    public List<CurtidaDTO> listarCurtidasVideo(Long idVideo) {
        return curtidaRepository.findByVideoId(idVideo).stream()
                .map(CurtidaDTO::new)
                .collect(Collectors.toList());
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
