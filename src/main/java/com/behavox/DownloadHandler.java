package com.behavox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;

@Component
public class DownloadHandler {
    @Autowired
    FilesStorageService storageService;

    public ResponseEntity<Object> handle(String uuid) {
        try {
            Resource file = storageService.load(uuid+".log");
            if(!file.exists()){
                return new ResponseEntity<>("Log file does not exists. Retry uploading the groovy file", HttpStatus.NOT_FOUND);
            }else{
                InputStreamResource resource = new InputStreamResource(new FileInputStream(file.getFile()));
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));
                headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
                headers.add("Pragma", "no-cache");
                headers.add("Expires", "0");

                return ResponseEntity.ok().headers(headers).contentLength(file.contentLength()).contentType(
                        MediaType.parseMediaType("application/txt")).body(resource);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to download log file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
