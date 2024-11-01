package com.example.demo.Services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    
    String uploadFile(MultipartFile file) throws IOException;

    String downloadFile(String fileName) throws IOException;

    List<String> listFiles() throws IOException;

    String deletFile(String filename) throws IOException;

    String renameFile(String oldFile, String newFile) throws IOException;

    String updateFile(MultipartFile file, String oldFile) throws IOException;
}
