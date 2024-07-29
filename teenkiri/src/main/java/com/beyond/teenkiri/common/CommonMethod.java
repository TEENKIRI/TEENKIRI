package com.beyond.teenkiri.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public class CommonMethod {

    public static Boolean fileSizeCheck(MultipartFile file){
        String filename = file.getOriginalFilename();
        if (filename != null) {
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
        return null;
    }

    public static long getMaxFileSizeForExtension(String extension) {
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
}
