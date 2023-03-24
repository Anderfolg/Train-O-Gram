package org.anderfolg.trainogram.service;

import org.anderfolg.trainogram.entities.Like;
import org.anderfolg.trainogram.exceptions.Status440LikeAlreadyExistsException;
import org.anderfolg.trainogram.exceptions.Status438LikeDoesntExistException;
import org.anderfolg.trainogram.exceptions.Status436PostDoesntExistException;
import org.anderfolg.trainogram.exceptions.Status419UserException;
import org.anderfolg.trainogram.security.jwt.JwtUser;

import java.util.List;

public interface LikeToPostService {
    List<Like> findAllLikesByPost( Long id) throws Status436PostDoesntExistException;

    List<Like> findAllLikesByUser( JwtUser jwtUser) throws Status419UserException;
    void addLikeToPost(JwtUser jwtUser, Long postID) throws Status440LikeAlreadyExistsException, Status436PostDoesntExistException, Status419UserException;

    void deleteLikeFromPost( JwtUser jwtUser, Long postID) throws Status438LikeDoesntExistException, Status436PostDoesntExistException, Status419UserException;

}
