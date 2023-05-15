package org.anderfolg.trainogram.repo;

import org.anderfolg.trainogram.entities.SponsorPost;
import org.anderfolg.trainogram.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SponsorPostRepository extends JpaRepository<SponsorPost, Long> {
    Page<SponsorPost> findAllBySponsor( User sponsor, Pageable pageable );
}
