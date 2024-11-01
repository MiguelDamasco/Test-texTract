package com.example.demo.Services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;



@Service
public class S3ServiceImpl implements S3Service{

    @Value("${download.localpath}")
    private String localpath;

    private final S3Client s3Client;

    private String bucketName = "bucket-prueba-aws-forggstar";

    @Autowired
    public S3ServiceImpl(S3Client pS3Client) {
        this.s3Client = pS3Client;
    }

    public String uploadFile(MultipartFile file) throws IOException {
      
        try {
            String fileName = file.getOriginalFilename();
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));
            return "Archivo subido correctamente";
        }
        catch(IOException e) {
            throw new IOException(e.getMessage());

        }
    }

    private boolean doesObjectExist(String objcectKey) {

        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                                                    .bucket(bucketName)
                                                    .key(objcectKey)
                                                    .build();
            
            s3Client.headObject(headObjectRequest);
        }
        catch (S3Exception e) {
            if(e.statusCode() == 404)
            {
                return false;
            }
        }
        return true;
    }

    /* 
    public String downloadFile(String fileName) throws IOException {

        
        if(!doesObjectExist(fileName)) {
            return "El archivo NO existe!";
            }
            System.out.println("El archivo existe: " + doesObjectExist(fileName));
            System.out.println("VEEEEER: " + localpath + "/" + fileName);

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                                            .bucket(bucketName)
                                            .key(fileName)
                                            .build();
        
        ResponseInputStream<GetObjectResponse> result = s3Client.getObject(getObjectRequest);


        try (FileOutputStream fos = new FileOutputStream(localpath + "/" + fileName)) {
            byte[] read_buffer = new byte[1024];
            int read_len = 0;

            while((read_len = result.read(read_buffer)) > 0) {
                fos.write(read_buffer, 0, read_len);
            }
        }
        catch (IOException e) {
            throw new IOException(e.getMessage());
        }
        return "Archivo descargado con exito!!";
    } */

    public String downloadFile(String fileName) throws IOException {
        // Verificar existencia del objeto
        if (!doesObjectExist(fileName)) {
            return "El archivo NO existe!";
        }
        
        System.out.println("El archivo existe: " + doesObjectExist(fileName));
        System.out.println("Ruta de descarga: " + localpath + "/" + fileName);
        
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();
        
        ResponseInputStream<GetObjectResponse> result = null;
        try {
            result = s3Client.getObject(getObjectRequest);
        } catch (S3Exception e) {
            e.printStackTrace(); // Captura errores específicos de S3
            return "Error al obtener el archivo: " + e.getMessage();
        }
        
        try (FileOutputStream fos = new FileOutputStream(localpath + "/" + fileName)) {
            byte[] read_buffer = new byte[1024];
            int read_len;
    
            while ((read_len = result.read(read_buffer)) > 0) {
                fos.write(read_buffer, 0, read_len);
            }
        } catch (IOException e) {
            e.printStackTrace(); // Para depuración
            throw new IOException(e.getMessage());
        }
        
        return "Archivo descargado con éxito!!";
    }

    public List<String> listFiles() throws IOException {
        try {
            ListObjectsRequest request = ListObjectsRequest.builder()
                                            .bucket(bucketName)
                                            .build();

            List<S3Object> objects = s3Client.listObjects(request).contents();
            List<String> result = new ArrayList<>();

            for (S3Object object : objects) {
                result.add(object.key());
            }
            return result;
        }
        catch (S3Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    public String deletFile(String filename) throws IOException {

        if(!doesObjectExist(filename)) {
            return "No existe el archivo a eliminar!!";
        }

        try {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                                                            .bucket(bucketName)
                                                            .key(filename)
                                                            .build();

            s3Client.deleteObject(request);
            return "Objeto " + filename + " eliminado con exito";
        }
        catch (S3Exception e) {
            throw new IOException(e.getMessage());
        }
    }

    public String renameFile(String oldFile, String newFile) throws IOException {
        if(!doesObjectExist(oldFile)) {
            return "No existe el archivo viejo!!!";
        }

        try {

            CopyObjectRequest request = CopyObjectRequest.builder()
                                            .destinationBucket(bucketName)
                                            .copySource(bucketName + "/" + oldFile)
                                            .destinationKey(newFile)
                                            .build();
            
            s3Client.copyObject(request);
            deletFile(oldFile);
            return "Archivo renombrado con exito!";
        }
        catch (S3Exception e) {
            throw new IOException(e.getMessage());
        }
    }
    
    public String updateFile(MultipartFile file, String oldFile) throws IOException {
        if(!doesObjectExist(oldFile)) {
            return "El archivo no existe!";
        }

        try {
            String newFileName = file.getOriginalFilename();
            deletFile(oldFile);

            PutObjectRequest request = PutObjectRequest.builder()
                                                        .bucket(bucketName)
                                                        .key(newFileName)
                                                        .build();

            s3Client.putObject(request, RequestBody.fromBytes(file.getBytes()));
            return "Archivo actualizado con exito!!";
                                                        
        }
        catch (S3Exception e)
        {
            throw new IOException(e.getMessage());
        }
    }

}
