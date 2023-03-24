package org.anderfolg.trainogram.repo;

import org.anderfolg.trainogram.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface LikeV2repository extends JpaRepository<Like, Long> {
    List<Like> findAllByContentIdAndContentType( Long contentId, ContentType contentType);

    List<Like> findAllByContentTypeAndUser( ContentType contentType, User user);
    Like findLikeV2ByContentIdAndUser( Long contentId, User user);

    List<Like> findAllByUser( User user);

    @Modifying
    @Transactional
    @Query("delete from Like l where l.id = ?1 ")
    void deleteLikeV2ById(Long id);

    boolean existsByUser( User user);
    boolean existsByUserAndContentId(User user, Long contentId);
}
