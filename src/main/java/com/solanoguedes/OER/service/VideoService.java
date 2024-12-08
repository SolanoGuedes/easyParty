package com.solanoguedes.OER.service;

import com.solanoguedes.OER.model.Video;
import com.solanoguedes.OER.model.Usuario;
import com.solanoguedes.OER.model.dto.VideoDTO;
import com.solanoguedes.OER.repositories.ComentarioRepository;
import com.solanoguedes.OER.repositories.CurtidaRepository;
import com.solanoguedes.OER.repositories.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class VideoService {

    @Autowired
    private StorageService storageService;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CurtidaRepository curtidaRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    private final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();

    public VideoService() {
        taskScheduler.initialize(); // Inicializa o scheduler
    }

    // Método para fazer o upload de um vídeo
    public Video uploadVideo(MultipartFile file, String legenda, Long idUsuario, String privacidade, Integer duracao, String tipoVideo, boolean expiraEm24Horas) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo não pode estar vazio");
        }

        String urlVideo = storageService.uploadFile(file);

        Video video = new Video();
        video.setUrlVideo(urlVideo);
        video.setLegenda(legenda);
        video.setDataPostagem(LocalDateTime.now());
        video.setUsuario(new Usuario(idUsuario));
        video.setFormatoArquivo(file.getContentType());
        video.setTamanhoArquivo((int) file.getSize());
        video.setDuracao(duracao);
        video.setPrivacidade(privacidade);
        video.setTipoVideo(tipoVideo);

        video = videoRepository.save(video);

        if (expiraEm24Horas && "story".equals(tipoVideo)) {
            agendarMudancaDePrivacidade(video.getId(), 24);
        }

        return video;
    }

    // Método para listar vídeos de um usuário com o mesmo nível de privacidade
    private List<VideoDTO> listarVideosPorPrivacidade(Long idUsuario, String privacidade) {
        return videoRepository.findByUsuarioIdAndPrivacidade(idUsuario, privacidade)
                .stream()
                .map(VideoDTO::new) // Utiliza o construtor que aceita um Video
                .peek(videoDTO -> {
                    // Atualiza as contagens de comentários e curtidas
                    videoDTO.setNumeroComentarios(comentarioRepository.countByVideo(new Video(videoDTO.getId())));
                    videoDTO.setNumeroCurtidas(curtidaRepository.countByVideo(new Video(videoDTO.getId())));
                })
                .collect(Collectors.toList());
    }

    // Método para listar todos os vídeos públicos de um usuário
    public List<VideoDTO> listarVideosPublicos(Long idUsuario) {
        return listarVideosPorPrivacidade(idUsuario, "publico");
    }

    // Método para listar os vídeos privados de um usuário autenticado
    public List<VideoDTO> listarVideosPrivados(Long idUsuario) {
        return listarVideosPorPrivacidade(idUsuario, "privado");
    }

    // Método para buscar um vídeo pelo ID
    public VideoDTO obterVideoPorId(Long idVideo) {
        Video video = videoRepository.findById(idVideo)
                .orElseThrow(() -> new RuntimeException("Vídeo não encontrado com ID: " + idVideo));
        VideoDTO videoDTO = new VideoDTO(video);
        videoDTO.setNumeroComentarios(comentarioRepository.countByVideo(video));
        videoDTO.setNumeroCurtidas(curtidaRepository.countByVideo(video));
        return videoDTO;
    }

    // Método para deletar um vídeo
    public void deletarVideo(Long idVideo) throws IOException {
        Optional<Video> videoOptional = videoRepository.findById(idVideo);
        if (videoOptional.isPresent()) {
            Video video = videoOptional.get();
            storageService.deleteFile(video.getUrlVideo());
            videoRepository.delete(video);
        } else {
            throw new RuntimeException("Vídeo não encontrado com ID: " + idVideo);
        }
    }

    // Agenda a mudança de privacidade para daqui a 24 horas
    private void agendarMudancaDePrivacidade(Long idVideo, int horas) {
        long delayMillis = TimeUnit.HOURS.toMillis(horas);
        taskScheduler.schedule(() -> {
            Video video = videoRepository.findById(idVideo)
                    .orElseThrow(() -> new RuntimeException("Vídeo não encontrado com ID: " + idVideo));
            video.setPrivacidade("privado");
            videoRepository.save(video);
        }, new java.util.Date(System.currentTimeMillis() + delayMillis));
    }

    // Método para alterar a privacidade de um vídeo
    public VideoDTO alterarPrivacidadeVideo(Long idVideo, String novaPrivacidade) {
        Video video = videoRepository.findById(idVideo)
                .orElseThrow(() -> new RuntimeException("Vídeo não encontrado com ID: " + idVideo));

        video.setPrivacidade(novaPrivacidade);
        videoRepository.save(video);

        VideoDTO videoDTO = new VideoDTO(video);
        videoDTO.setNumeroComentarios(comentarioRepository.countByVideo(video));
        videoDTO.setNumeroCurtidas(curtidaRepository.countByVideo(video));
        return videoDTO;
    }
}
