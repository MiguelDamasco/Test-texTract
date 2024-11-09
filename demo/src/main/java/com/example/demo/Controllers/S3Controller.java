package com.example.demo.Controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Services.S3Service;

@RestController
@RequestMapping(path = "/file")
public class S3Controller {
    
    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("file: " + file);
        return this.s3Service.uploadFile(file);
    }

    @GetMapping("/download/{fileName}")
    public String downloadFile(@PathVariable String fileName) throws IOException {
        System.out.println("Nombre archivo: " + fileName);
        return s3Service.downloadFile(fileName);
    }

    @GetMapping("/all")
    public List<String> getAll() throws IOException {
        return this.s3Service.listFiles();
    } 

    @PutMapping("/rename/{oldFile}/{newFile}")
    public String renameFile(@PathVariable String oldFile, @PathVariable String newFile) throws IOException {
        return this.s3Service.renameFile(oldFile, newFile);
    }

    @PutMapping("/update/{oldFile}")
    public String updateFile(@RequestParam("file") MultipartFile file, @PathVariable String oldFile) throws IOException {
        return this.s3Service.updateFile(file, oldFile);
    }


    @DeleteMapping("/delete/{filename}")
    public String deleteFile(@PathVariable String filename) throws IOException {
        System.out.println("Filename:: " + filename);
        return this.s3Service.deletFile(filename);
    }



}
