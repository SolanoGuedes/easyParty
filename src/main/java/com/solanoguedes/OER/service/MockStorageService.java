package com.solanoguedes.OER.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class MockStorageService implements StorageService {

    private static final String STORAGE_PATH = "uploads/";

    public MockStorageService() {
        // Cria o diretório de armazenamento local, se não existir
        new File(STORAGE_PATH).mkdirs();
    }

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        // Gera um nome de arquivo único
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File localFile = new File(STORAGE_PATH + fileName);

        // Salva o conteúdo do arquivo localmente
        try (FileOutputStream fos = new FileOutputStream(localFile)) {
            fos.write(file.getBytes());
        }

        // Retorna a URL simulada do arquivo
        return STORAGE_PATH + fileName;
    }

    @Override
    public void deleteFile(String fileUrl) throws IOException {
        File file = new File(fileUrl);
        if (file.exists()) {
            if (!file.delete()) {
                throw new IOException("Não foi possível deletar o arquivo: " + fileUrl);
            }
        } else {
            throw new IOException("Arquivo não encontrado: " + fileUrl);
        }
    }
}
