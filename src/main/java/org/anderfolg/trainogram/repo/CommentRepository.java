package org.anderfolg.trainogram.repo;

import org.anderfolg.trainogram.entities.Comment;
import org.anderfolg.trainogram.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost( Post post);
}
