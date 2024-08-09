package com.beyond.teenkiri.common.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
public class CommonMethod {

    public Boolean fileSizeCheck(MultipartFile file){
        String filename = file.getOriginalFilename();
        String extension = filename.substring(filename.lastIndexOf('.') + 1);
        long maxSize = getMaxFileSizeForExtension(extension);

        if(maxSize == -1) {
            throw new IllegalArgumentException("허용되지 않은 확장자 입니다.");
        }else if(file.getSize() > maxSize) {
            return false;
        }else{
            return true;
        }
    }

    public Boolean fileSizeCheckFromByte(String fileName, byte[] file){
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        long maxSize = getMaxFileSizeForExtension(extension);

        if(maxSize == -1) {
            throw new IllegalArgumentException("허용되지 않은 확장자 입니다.");
        }else if(file.length > maxSize) { //byte는 array의 길이로 파일크기 측정
            return false;
        }else{
            return true;
        }
    }

    public long getMaxFileSizeForExtension(String extension) {
        switch (extension.toLowerCase()) {
            case "jpg":
            case "jpeg":
            case "png":
                return 300 * 1024 * 1024; // 300MB
            case "mp4":
                return 5L * 1024 * 1024 * 1024; // 5GB
            default:
                return -1; // 허용되지 않은 확장자
        }
    }

    public Path fileSave(MultipartFile file, Long id){
        try{
            if(file.isEmpty()){ // 비어있는 파일
                return null;
            }
            Boolean fileBoolean = fileSizeCheck(file);
            if (Boolean.FALSE.equals(fileBoolean)) {
                throw new IllegalArgumentException("파일의 크기가 너무 큽니다.");
            }

            byte[] fileBytes = file.getBytes();
            Path filePath = Paths.get("C:/Users/rro06/OneDrive/Desktop/tmp/", id + "_" + file.getOriginalFilename());
            Files.write(filePath,fileBytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패");
        }
    }
}
