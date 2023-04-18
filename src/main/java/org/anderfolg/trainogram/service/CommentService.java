package org.anderfolg.trainogram.service;

import org.anderfolg.trainogram.entities.*;
import org.anderfolg.trainogram.entities.dto.CommentDto;
import org.anderfolg.trainogram.exceptions.Status436DoesntExistException;
import org.anderfolg.trainogram.exceptions.Status419UserException;
import org.anderfolg.trainogram.security.jwt.JwtUser;

import java.util.List;

public interface CommentService {
    List<CommentDto> findCommentsByPost(Post post);
    Comment findCommentById(Long commentID)
            throws Status436DoesntExistException;

    void addComment( String content, JwtUser jwtUser, Long postId )
            throws Status436DoesntExistException, Status419UserException;

    void deleteComment( final Long id, JwtUser jwtUser)
            throws Status419UserException;
    void updateComment( String content, JwtUser jwtUser, Long comID )
            throws Status436DoesntExistException, Status419UserException;

}
