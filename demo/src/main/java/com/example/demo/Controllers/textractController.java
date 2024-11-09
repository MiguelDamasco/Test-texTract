package com.example.demo.Controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.DTO.TextDTO;
import com.example.demo.DTO.userDTO;
import com.example.demo.Services.S3Service;
import com.example.demo.Services.textService;

@RestController
@RequestMapping("/text")
public class textractController {
    
    @Autowired
    private textService myTextService;

    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public userDTO getText(@RequestParam("file") MultipartFile file) throws IOException {
        String response = this.s3Service.uploadFile(file);
        if(response.indexOf("Archivo subido correctamente") != -1) {
            String fileName = file.getOriginalFilename();
            System.out.println("Nombre del archivo: " + fileName);
            return this.myTextService.getText(fileName);
        }
        return null;
    }

    @PostMapping("/pure")
    public TextDTO getTextPure(@RequestParam("file") MultipartFile file) throws IOException {
        String response = this.s3Service.uploadFile(file);
        if(response.indexOf("Archivo subido correctamente") != -1) {
            String fileName = file.getOriginalFilename();
            return this.myTextService.getTextPure(fileName);
        }
        return null; 
    }
}
