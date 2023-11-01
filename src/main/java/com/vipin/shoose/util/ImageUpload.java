package com.vipin.shoose.util;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class ImageUpload {
    public String saveImage(MultipartFile multipartFile) throws IOException {
        String rootPath=System.getProperty("user.dir");
        String uploadDir= rootPath + "/src/main/resources/static/img/product-images";
        File dir = new File(uploadDir);
        if(!dir.exists()){
            dir.mkdir();
        }
        String fileName= UUID.randomUUID().toString()+"-"+multipartFile.getOriginalFilename();
        String filePath = uploadDir+"/"+fileName;
        Path path = Paths.get(filePath);
        Files.copy(multipartFile.getInputStream(),path,StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }
}
