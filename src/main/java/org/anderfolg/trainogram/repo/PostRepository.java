package org.anderfolg.trainogram.repo;

import org.anderfolg.trainogram.entities.ContentType;
import org.anderfolg.trainogram.entities.Post;
import org.anderfolg.trainogram.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByUser( User user, Pageable pageable );
    List<Post> findAllByUserAndType( User user, ContentType type );
    List<Post> findAllByType(ContentType type);
}
