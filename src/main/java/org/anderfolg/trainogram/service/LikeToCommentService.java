package org.anderfolg.trainogram.service;

import org.anderfolg.trainogram.entities.Like;
import org.anderfolg.trainogram.exceptions.Status439CommentDoesntExistException;
import org.anderfolg.trainogram.exceptions.Status440LikeAlreadyExistsException;
import org.anderfolg.trainogram.exceptions.Status438LikeDoesntExistException;
import org.anderfolg.trainogram.exceptions.Status419UserException;
import org.anderfolg.trainogram.security.jwt.JwtUser;

import java.util.List;

public interface LikeToCommentService {
    List<Like> findAllLikesByComment( Long commentID ) throws Status439CommentDoesntExistException;

    List<Like> findAllLikesByUser( JwtUser jwtUser) throws Status419UserException;

    void addLikeToComment( JwtUser jwtUser, Long commentID) throws Status439CommentDoesntExistException, Status440LikeAlreadyExistsException, Status419UserException;

    void deleteLikeFromComment(JwtUser jwtUser, Long commentID) throws Status439CommentDoesntExistException, Status438LikeDoesntExistException, Status419UserException;
}
