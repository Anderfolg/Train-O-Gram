package org.anderfolg.trainogram.repo;

import org.anderfolg.trainogram.entities.SponsorPost;
import org.anderfolg.trainogram.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SponsorPostRepository extends JpaRepository<SponsorPost, Long> {
    List<SponsorPost> findAllBySponsor(User sponsor);
}
