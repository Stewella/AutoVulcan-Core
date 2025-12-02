package com.prosigmaka.catra.diglett.service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;


@Service
public class Base64File {

    public String getFile(String namaFile, String folderStrPath) {
        Path path = Paths.get(folderStrPath + "/" + namaFile);

        try {
            byte[] file = Files.readAllBytes(path);
            String fileB64 = Base64.getEncoder().encodeToString(file);
            return fileB64;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
