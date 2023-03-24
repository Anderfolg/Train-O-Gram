package org.anderfolg.trainogram.service;

import org.anderfolg.trainogram.entities.*;
import org.anderfolg.trainogram.entities.DTO.CommentDto;
import org.anderfolg.trainogram.exceptions.Status439CommentDoesntExistException;
import org.anderfolg.trainogram.exceptions.Status436PostDoesntExistException;
import org.anderfolg.trainogram.exceptions.Status419UserException;
import org.anderfolg.trainogram.security.jwt.JwtUser;

import java.util.List;

public interface CommentService {
    List<CommentDto> findCommentsByPost(Post post);
    Comment findCommentById(Long commentID) throws Status439CommentDoesntExistException;

    void addComment( String content, JwtUser jwtUser, Long postId ) throws Status436PostDoesntExistException, Status419UserException;

    void deleteComment( final Long id, JwtUser jwtUser) throws Status419UserException;
    void updateComment( String content, JwtUser jwtUser, Long comID ) throws Status439CommentDoesntExistException, Status419UserException;

}
