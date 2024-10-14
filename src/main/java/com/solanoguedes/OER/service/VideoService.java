package com.solanoguedes.OER.service;

import com.solanoguedes.OER.model.Comentario;
import com.solanoguedes.OER.model.Video;
import com.solanoguedes.OER.model.Usuario;
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

    @Autowired
    private ComentarioService comentarioService;

    // Scheduler para executar a tarefa de alteração de privacidade
    private final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();

    // Método para fazer o upload de um vídeo
    public Video uploadVideo(MultipartFile file, String legenda, Long idUsuario, String privacidade, Integer duracao, String tipoVideo, boolean expiraEm24Horas) throws IOException {
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
        video.setUsuario(new Usuario(idUsuario)); // Relaciona o usuário
        video.setFormatoArquivo(file.getContentType());
        video.setTamanhoArquivo((int) file.getSize());
        video.setDuracao(duracao);
        video.setPrivacidade(privacidade);  // Define a privacidade (publico/privado)
        video.setTipoVideo(tipoVideo);  // Define se é "story" ou "página_inicial"

        // Salva o vídeo no repositório primeiro para obter o ID gerado
        video = videoRepository.save(video);

        // Verifica se deve agendar a mudança de privacidade
        if (expiraEm24Horas && "story".equals(tipoVideo)) {
            agendarMudancaDePrivacidade(video.getIdVideo(), 24);
        }

        // Retorna o vídeo salvo
        return video;
    }

    // Método para listar todos os vídeos públicos de um usuário
    public List<Video> listarVideosPublicos(Long idUsuario) {
        return videoRepository.findByUsuarioIdAndPrivacidade(idUsuario, "publico");
    }

    // Método para listar os vídeos privados de um usuário autenticado
    public List<Video> listarVideosPrivados(Long idUsuario) {
        return videoRepository.findByUsuarioIdAndPrivacidade(idUsuario, "privado");
    }

    // Método para buscar um vídeo pelo ID
    public Video obterVideoPorId(Long idVideo) {
        Optional<Video> video = videoRepository.findById(idVideo);
        if (video.isPresent()) {
            return video.get();
        } else {
            throw new RuntimeException("Vídeo não encontrado com ID: " + idVideo);
        }
    }

    // Método para deletar um vídeo
    public void deletarVideo(Long idVideo) throws IOException {
        Optional<Video> videoOptional = videoRepository.findById(idVideo);

        if (videoOptional.isPresent()) {
            Video video = videoOptional.get();
            storageService.deleteFile(video.getUrlVideo());  // Remove o vídeo do armazenamento
            videoRepository.delete(video);  // Remove o vídeo do banco de dados
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

        // Agenda a tarefa de mudança de privacidade
        ScheduledFuture<?> future = taskScheduler.schedule(() -> {
            Video video = videoRepository.findById(idVideo)
                    .orElseThrow(() -> new RuntimeException("Vídeo não encontrado com ID: " + idVideo));
            video.setPrivacidade("privado");
            videoRepository.save(video);
        }, new java.util.Date(System.currentTimeMillis() + delayMillis));
    }

    // Método para mudar a privacidade de um vídeo
    public Video alterarPrivacidadeVideo(Long idVideo, String novaPrivacidade) {
        // Busca o vídeo pelo ID
        Video video = videoRepository.findById(idVideo)
                .orElseThrow(() -> new RuntimeException("Vídeo não encontrado com ID: " + idVideo));

        // Atualiza a privacidade do vídeo
        video.setPrivacidade(novaPrivacidade);
        return videoRepository.save(video);  // Salva a alteração no banco de dados
    }

    public Video obterVideoComDetalhes(Long idVideo) {
        Video video = videoRepository.findById(idVideo)
                .orElseThrow(() -> new RuntimeException("Vídeo não encontrado com ID: " + idVideo));

        // Contar o número de curtidas
        int numeroCurtidas = curtidaRepository.countByVideo(video);
        video.setNumeroCurtidas(numeroCurtidas);

        // Contar o número de comentários
        int numeroComentarios = comentarioRepository.countByVideo(video);
        video.setNumeroComentarios(numeroComentarios);

        // Listar os 3 primeiros comentários
        List<Comentario> comentarios = comentarioService.listarTresPrimeirosComentariosVideo(video.getIdVideo());
        video.setComentarios(comentarios); // Adicione um método para armazenar os comentários no modelo

        return video;
    }
}
