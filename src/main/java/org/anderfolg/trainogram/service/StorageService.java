package org.anderfolg.trainogram.service;

import org.anderfolg.trainogram.entities.Upload;
import org.anderfolg.trainogram.exceptions.Status430InvalidFileException;
import org.anderfolg.trainogram.exceptions.Status432InvalidFileNameException;
import org.anderfolg.trainogram.exceptions.Status435StorageException;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    Upload saveFile( MultipartFile file, String fileName ) throws Status435StorageException, Status430InvalidFileException, Status432InvalidFileNameException;

    void deleteFile( String fileName );
}
