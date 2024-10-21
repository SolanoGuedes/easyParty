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
import java.util.concurrent.ScheduledFuture;
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

    // Método para fazer o upload de um vídeo
    public VideoDTO uploadVideo(MultipartFile file, String legenda, Long idUsuario, String privacidade, Integer duracao, String tipoVideo, boolean expiraEm24Horas) throws IOException {
        // Valida o arquivo
        if (file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo não pode estar vazio");
        }

        // Faz o upload do arquivo usando o StorageService
        String urlVideo = storageService.uploadFile(file);

        // Cria um objeto Video com os metadados e salva no banco de dados
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

        // Salva o vídeo no repositório primeiro para obter o ID gerado
        video = videoRepository.save(video);

        // Verifica se deve agendar a mudança de privacidade
        if (expiraEm24Horas && "story".equals(tipoVideo)) {
            agendarMudancaDePrivacidade(video.getId(), 24);
        }

        return convertToDTO(video);
    }

    // Método para listar todos os vídeos públicos de um usuário
    public List<VideoDTO> listarVideosPublicos(Long idUsuario) {
        return videoRepository.findByUsuarioIdAndPrivacidade(idUsuario, "publico")
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Método para listar os vídeos privados de um usuário autenticado
    public List<VideoDTO> listarVideosPrivados(Long idUsuario) {
        return videoRepository.findByUsuarioIdAndPrivacidade(idUsuario, "privado")
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Método para buscar um vídeo pelo ID
    public VideoDTO obterVideoPorId(Long idVideo) {
        Video video = videoRepository.findById(idVideo)
                .orElseThrow(() -> new RuntimeException("Vídeo não encontrado com ID: " + idVideo));
        return convertToDTO(video);
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

    // Inicializa o Scheduler
    public VideoService() {
        taskScheduler.initialize();
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
        // Busca o vídeo no repositório ou lança exceção se não encontrar
        Video video = videoRepository.findById(idVideo)
                .orElseThrow(() -> new RuntimeException("Vídeo não encontrado com ID: " + idVideo));

        // Atualiza a privacidade e salva no banco de dados
        video.setPrivacidade(novaPrivacidade);
        videoRepository.save(video);

        // Retorna o vídeo atualizado como DTO
        return convertToDTO(video);
    }

    private VideoDTO convertToDTO(Video video) {
        // Método para converter Video em VideoDTO
        return new VideoDTO(
                video.getId(),
                video.getUrlVideo(),
                video.getLegenda(),
                video.getDataPostagem(),
                video.getPrivacidade(),
                video.getDuracao(),
                video.getNumeroCurtidas(),
                video.getNumeroComentarios(),
                video.getTipoVideo()
        );
    }
}
