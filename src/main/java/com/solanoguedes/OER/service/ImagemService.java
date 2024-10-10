package com.solanoguedes.OER.service;

import com.solanoguedes.OER.model.Imagem;
import com.solanoguedes.OER.model.Usuario;
import com.solanoguedes.OER.repositories.ImagemRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ImagemService {

    @Autowired
    private StorageService storageService;

    @Autowired
    private ImagemRepository imagemRepository;

    // Método para fazer o upload de uma imagem (já existente)
    public Imagem uploadImagem(MultipartFile file, String legenda, Long idUsuario, String privacidade) throws IOException {
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

        // Salva a imagem no repositório e retorna a imagem salva
        return imagemRepository.save(imagem);
    }

    // Método para listar todas as imagens públicas de um usuário (já existente)
    public List<Imagem> listarImagensPublicas(Long idUsuario) {
        return imagemRepository.findByUsuarioIdAndPrivacidade(idUsuario, "publica");
    }

    // Método para listar as imagens privadas de um usuário autenticado
    public List<Imagem> listarImagensPrivadas(Long idUsuario) {
        return imagemRepository.findByUsuarioIdAndPrivacidade(idUsuario, "privada");
    }

    // Novo método para buscar uma imagem pelo ID
    public Imagem obterImagemPorId(Long idImagem) {
        // Busca a imagem no repositório pelo ID
        Optional<Imagem> imagem = imagemRepository.findById(idImagem);

        // Se a imagem não for encontrada, lança uma exceção
        if (imagem.isPresent()) {
            return imagem.get();
        } else {
            throw new RuntimeException("Imagem não encontrada com ID: " + idImagem);
        }
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
}
