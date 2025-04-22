package com.xvpi.smarttransportbackend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUploadUtil {

    @Value("${upload.path}")
    private String uploadRootPath;  // e.g., D:/emergency_photos/

    @Value("${upload.url.prefix}")
    private String urlPrefix;       // e.g., /static/emergency_photos/

    public String saveFile(MultipartFile file, String subDir) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("文件为空");
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFileName = UUID.randomUUID().toString() + fileExtension;

        String fullDirPath = uploadRootPath + File.separator + subDir;
        File dir = new File(fullDirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File dest = new File(dir, newFileName);
        file.transferTo(dest);

        return urlPrefix + subDir + "/" + newFileName;
    }
}
