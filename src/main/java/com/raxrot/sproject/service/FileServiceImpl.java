package com.raxrot.sproject.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {


    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        // Get original file name (like "cat.png")
        String originalFilename = file.getOriginalFilename();

        // Generate a unique name like "a38dd-filename.png"
        String randomId = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.'));
        String fileName = randomId + extension;

        // Build full path
        String filePath = path + File.separator + fileName;

        // Create directories if not exist
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        // Save file
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }
}
