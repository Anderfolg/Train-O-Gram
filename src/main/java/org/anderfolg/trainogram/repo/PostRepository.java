package org.anderfolg.trainogram.repo;

import org.anderfolg.trainogram.entities.ContentType;
import org.anderfolg.trainogram.entities.Post;
import org.anderfolg.trainogram.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByUser( User user, Pageable pageable );
    Page<Post> findAllByUserAndType( User user, ContentType type ,Pageable pageable);
    Page<Post> findAllByType(ContentType type, Pageable pageable);
}
