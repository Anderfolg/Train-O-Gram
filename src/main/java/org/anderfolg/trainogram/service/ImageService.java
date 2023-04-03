package org.anderfolg.trainogram.service;

import org.anderfolg.trainogram.exceptions.Status430InvalidFileException;
import org.anderfolg.trainogram.exceptions.Status432InvalidFileNameException;
import org.anderfolg.trainogram.exceptions.Status435StorageException;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String uploadImage( MultipartFile file, String username )
            throws Status435StorageException, Status432InvalidFileNameException, Status430InvalidFileException;

    String getUploadDirectory();

    void deleteImage( String filename );
}
