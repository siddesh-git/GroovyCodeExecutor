package com.behavox;

import java.io.*;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class GroovyServiceController {
    private static Map<String, String> statusCache = new HashMap<>();

    @Autowired
    UploadHandler uploadHandler;

    @Autowired
    DownloadHandler downloadHandler;

    @RequestMapping(value = "/index")
    public String index() {
        return "index";
    }
    @RequestMapping(value = "/upload", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadGroovyFile(@RequestParam("file") MultipartFile file) throws IOException {
        return uploadHandler.handle(file, statusCache);
    }



    @RequestMapping(value = "/downloadLogs/{uuid}")
    public ResponseEntity<Object> downloadLogs(@PathVariable String uuid) {
        if(statusCache.get(uuid)!=null)
            return downloadHandler.handle(uuid);
        else
            return new ResponseEntity<>("Not found", HttpStatus.OK);
    }

    @RequestMapping(value = "/status/{uuid}")
    public ResponseEntity<Object> status(@PathVariable String uuid) {
        if(statusCache.get(uuid)!=null)
            return new ResponseEntity<>(statusCache.get(uuid), HttpStatus.OK);
        else
            return new ResponseEntity<>("Not found", HttpStatus.OK);
    }
}
