package com.behavox;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Component
public class UploadHandler {

    @Autowired
    FilesStorageService storageService;

    public ResponseEntity<Object> handle(MultipartFile file, Map<String, String> statusCache) throws IOException {
        UUID uuid = UUID.randomUUID();
        try {
            storageService.save(file);


            statusCache.put(uuid.toString(), "STARTED");

            Path logPath = Paths.get("uploads", uuid+".log");
            Files.createFile(logPath);
            Binding binding = new Binding();
            binding.setProperty("out", new PrintStream("uploads/"+uuid+".log"));
            GroovyShell shell = new GroovyShell(binding);
            shell.evaluate(new File("uploads/"+ file.getOriginalFilename()));

            statusCache.put(uuid.toString(), "FINISHED");
            return new ResponseEntity<>("Submitted job with id: "+uuid, HttpStatus.OK);
        } catch (IOException e) {
            statusCache.put(uuid.toString(), "FAILED");
            e.printStackTrace();
            return new ResponseEntity<>("Failed to process the uploaded file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
