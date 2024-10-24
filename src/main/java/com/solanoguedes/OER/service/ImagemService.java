package com.solanoguedes.OER.service;

import com.solanoguedes.OER.model.Comentario;
import com.solanoguedes.OER.model.Imagem;
import com.solanoguedes.OER.model.Usuario;
import com.solanoguedes.OER.model.dto.ImagemDTO;
import com.solanoguedes.OER.repositories.ComentarioRepository;
import com.solanoguedes.OER.repositories.CurtidaRepository;
import com.solanoguedes.OER.repositories.ImagemRepository;

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
public class ImagemService {

    @Autowired
    private StorageService storageService;

    @Autowired
    private ImagemRepository imagemRepository;

    @Autowired
    private CurtidaRepository curtidaRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private ComentarioService comentarioService;

    // Scheduler para executar a tarefa de alteração de privacidade
    private final ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();

    // Método para fazer o upload de uma imagem (já existente)
    public Imagem uploadImagem(MultipartFile file, String legenda, Long idUsuario, String privacidade, boolean expiraEm24Horas) throws IOException {
        // Valida o arquivo
        if (file.isEmpty()) {
            throw new IllegalArgumentException("O arquivo não pode estar vazio");
        }

        // Faz o upload do arquivo usando o StorageService
        String urlImagem = storageService.uploadFile(file);

        // Cria um objeto Imagem com os metadados e salva no banco de dados
        Imagem imagem = new Imagem();
        imagem.setUrlImagem(urlImagem);
        imagem.setLegenda(legenda);
        imagem.setDataPostagem(LocalDateTime.now());
        imagem.setUsuario(new Usuario(idUsuario)); // Relaciona o usuário
        imagem.setFormatoArquivo(file.getContentType());
        imagem.setTamanhoArquivo((int) file.getSize());
        imagem.setPrivacidade(privacidade);  // Define a privacidade (publica/privada)

        imagem = imagemRepository.save(imagem);

        if (expiraEm24Horas) {
            agendarMudancaDePrivacidade(imagem.getId(), 24);
        }
        // Salva a imagem no repositório e retorna a imagem salva
        return imagem;
    }

    // Método para listar todas as imagens públicas de um usuário
    public List<ImagemDTO> listarImagensPublicas(Long idUsuario) {
        return imagemRepository.findByUsuario_IdAndPrivacidade(idUsuario, "publico")
                .stream()
                .map(imagem -> new ImagemDTO(
                        imagem.getId(),
                        imagem.getDataPostagem(),
                        imagem.getFormatoArquivo(),
                        imagem.getLegenda(),
                        comentarioRepository.countByImagem(imagem),
                        curtidaRepository.countByImagem(imagem),
                        imagem.getPrivacidade(),
                        imagem.getStatus(),
                        imagem.getTamanhoArquivo(),
                        imagem.getUrlImagem(),
                        imagem.getUsuario().getId()
                ))
                .collect(Collectors.toList());
    }

    // Método para listar as imagens privadas de um usuário autenticado
    public List<ImagemDTO> listarImagensPrivadas(Long idUsuario) {
        return imagemRepository.findByUsuario_IdAndPrivacidade(idUsuario, "privado")
                .stream()
                .map(imagem -> new ImagemDTO(
                        imagem.getId(),
                        imagem.getDataPostagem(),
                        imagem.getFormatoArquivo(),
                        imagem.getLegenda(),
                        comentarioRepository.countByImagem(imagem),
                        curtidaRepository.countByImagem(imagem),
                        imagem.getPrivacidade(),
                        imagem.getStatus(),
                        imagem.getTamanhoArquivo(),
                        imagem.getUrlImagem(),
                        imagem.getUsuario().getId()
                ))
                .collect(Collectors.toList());
    }


    public ImagemDTO obterImagemPorIdDTO(Long idImagem) {
        // Busca a imagem no repositório pelo ID
        Imagem imagem = imagemRepository.findById(idImagem)
                .orElseThrow(() -> new RuntimeException("Imagem não encontrada com ID: " + idImagem));

        // Converte a entidade Imagem para ImagemDTO
        return new ImagemDTO(
                imagem.getId(),
                imagem.getDataPostagem(),
                imagem.getFormatoArquivo(),
                imagem.getLegenda(),
                comentarioRepository.countByImagem(imagem),
                curtidaRepository.countByImagem(imagem),
                imagem.getPrivacidade(),
                imagem.getStatus(),
                imagem.getTamanhoArquivo(),
                imagem.getUrlImagem(),
                imagem.getUsuario().getId()  // Obtém o ID do usuário relacionado
        );
    }

    // Novo método para deletar uma imagem
    public void deletarImagem(Long idImagem) throws IOException {
        // Busca a imagem no repositório pelo ID
        Optional<Imagem> imagemOptional = imagemRepository.findById(idImagem);

        if (imagemOptional.isPresent()) {
            Imagem imagem = imagemOptional.get();
            // Remove o arquivo do armazenamento
            storageService.deleteFile(imagem.getUrlImagem());
            // Remove a imagem do banco de dados
            imagemRepository.delete(imagem);
        } else {
            throw new RuntimeException("Imagem não encontrada com ID: " + idImagem);
        }
    }

    //upload de imagem que deixa a privacidade somente por 24 hrs, vou usar em stories achei essa a melhor forma
    public ImagemService() {
        taskScheduler.initialize();  // Inicializa o scheduler
    }

    // Agenda a mudança de privacidade para daqui a 24 horas
    private void agendarMudancaDePrivacidade(Long idImagem, int horas) {
        // Converte horas em milissegundos
        long delayMillis = TimeUnit.HOURS.toMillis(horas);

        // Agenda a tarefa de mudança de privacidade
        ScheduledFuture<?> future = taskScheduler.schedule(() -> {
            Imagem imagem = imagemRepository.findById(idImagem)
                    .orElseThrow(() -> new RuntimeException("Imagem não encontrada com ID: " + idImagem));
            imagem.setPrivacidade("privado");
            imagemRepository.save(imagem);
        }, new java.util.Date(System.currentTimeMillis() + delayMillis));
    }

    public ImagemDTO alterarPrivacidadeImagem(Long idImagem, String novaPrivacidade) {
        Imagem imagem = imagemRepository.findById(idImagem)
                .orElseThrow(() -> new RuntimeException("Imagem não encontrada"));

        imagem.setPrivacidade(novaPrivacidade);
        imagemRepository.save(imagem);

        return new ImagemDTO(imagem.getId(), imagem.getPrivacidade(), imagem.getUrlImagem());
    }


}
