package org.anderfolg.trainogram.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.anderfolg.trainogram.entities.Upload;
import org.anderfolg.trainogram.exceptions.Status430InvalidFileException;
import org.anderfolg.trainogram.exceptions.Status432InvalidFileNameException;
import org.anderfolg.trainogram.exceptions.Status435StorageException;
import org.anderfolg.trainogram.repo.UploadRepository;
import org.anderfolg.trainogram.service.StorageService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {
    private final UploadRepository uploadRepository;
    @Value("${upload.path}")
    private String uploadDirectory;


    @Value("${file.path.prefix}")
    private String filePathPrefix;

    @Autowired
    public StorageServiceImpl( UploadRepository uploadRepository ) {
        this.uploadRepository = uploadRepository;
    }

    @Override
    public Upload saveFile( MultipartFile file, String username ) throws Status430InvalidFileException, Status432InvalidFileNameException, Status435StorageException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());

        log.info("storing file{}", filename);

        try {
            if ( file.isEmpty() ){
                log.warn("failed to store empty file {}", filename);
                throw new Status430InvalidFileException("Failed to store an empty file "+ filename);
            }
            if ( filename.contains("..") ){
                log.warn("cannot store file with relative path {}", filename);
                throw new Status432InvalidFileNameException("Cannot store file with relative path outside current directory "
                        + filename);
            }
            String extension = FilenameUtils.getExtension(filename);
            String newFileName = UUID.randomUUID() + "." + extension;

            try(InputStream inputStream = file.getInputStream()) {
                Path userDir = Paths.get(uploadDirectory + username);

                if ( Files.notExists(userDir) ){
                    Files.createDirectory(userDir);
                }

                Files.copy(inputStream, userDir.resolve(newFileName),
                        StandardCopyOption.REPLACE_EXISTING);
            }

            String fileUrl = String.format("https:/%s/%s/%s", uploadDirectory, username, newFileName);
            log.info("successfully stored file {}, location {}", filename, fileUrl);

            return new Upload(newFileName, fileUrl, username, file.getContentType());
        }
        catch (IOException e ){
            log.error("failed to store file {} error: {}", filename, e );
            throw new Status435StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public void deleteFile( String fileName ){
        log.info("deleting file {}", fileName);
        try {
            Path filePath = Paths.get(uploadDirectory + fileName);
            Files.deleteIfExists(filePath);
        }
        catch ( IOException e ){
            log.error("failed to delete file {} error: {}", fileName, e );
        }
    }
}
