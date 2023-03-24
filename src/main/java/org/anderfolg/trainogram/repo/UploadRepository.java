package org.anderfolg.trainogram.repo;

import org.anderfolg.trainogram.entities.Upload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UploadRepository extends JpaRepository<Upload,Long> {
}
