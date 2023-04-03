package org.anderfolg.trainogram.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.anderfolg.trainogram.entities.Upload;
import org.anderfolg.trainogram.exceptions.Status430InvalidFileException;
import org.anderfolg.trainogram.exceptions.Status432InvalidFileNameException;
import org.anderfolg.trainogram.exceptions.Status435StorageException;
import org.anderfolg.trainogram.repo.UploadRepository;
import org.anderfolg.trainogram.service.ImageService;
import org.anderfolg.trainogram.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    @Value("${upload.path}")
    private String uploadDirectory;

    @Override
    public String getUploadDirectory() {
        return uploadDirectory;
    }

    private final StorageService storageService;

    private final UploadRepository uploadRepository;

    @Autowired
    public ImageServiceImpl( StorageService storageService, UploadRepository uploadRepository ) {
        this.storageService = storageService;
        this.uploadRepository = uploadRepository;
    }

    @Override
    public String uploadImage( MultipartFile file, String username ) throws Status435StorageException, Status432InvalidFileNameException, Status430InvalidFileException {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        log.info("storing file {}", filename);

        Upload upload = storageService.saveFile(file, username);
        uploadRepository.save(upload);
        return filename;
    }

    @Override
    public void deleteImage( String filename ) {
        storageService.deleteFile(filename);
    }
}
