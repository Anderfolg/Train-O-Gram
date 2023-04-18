package org.anderfolg.trainogram.controllers;

import org.anderfolg.trainogram.entities.ApiResponse;
import org.anderfolg.trainogram.entities.Like;
import org.anderfolg.trainogram.exceptions.*;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.anderfolg.trainogram.service.LikeToCommentService;
import org.anderfolg.trainogram.service.LikeToPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
//  TODO (Bogdan O.) 7/4/23: use pagination for "getAll" method types
public class LikeV2Controller {
    private final LikeToPostService likeToPostService;
    private final LikeToCommentService likeToCommentService;

    @Autowired
    public LikeV2Controller( LikeToPostService likeToPostService, LikeToCommentService likeToCommentService ) {
        this.likeToPostService = likeToPostService;
        this.likeToCommentService = likeToCommentService;
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<List<Like>> getLikesByPost( @PathVariable("postId") Long postId) throws Status436DoesntExistException {
        List<Like> likes = likeToPostService.findAllLikesByPost(postId);
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Like>> getAllPostLikesByUser( @PathVariable("userId") JwtUser jwtUser) throws Status419UserException {
        List<Like> likes = likeToPostService.findAllLikesByUser(jwtUser);
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }

    @PostMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse> addLikeToPost(@PathVariable("postId") Long postID,
                                                     JwtUser jwtUser) throws Status436DoesntExistException, Status419UserException {
        likeToPostService.addLikeToPost(jwtUser, postID);
        return new ResponseEntity<>(new ApiResponse(true, "Like has been added"), HttpStatus.CREATED);
    }

    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse> deleteLikeFromPost(@PathVariable("postId") final Long postID,
                                                          JwtUser jwtUser) throws  Status436DoesntExistException, Status419UserException {
        likeToPostService.deleteLikeFromPost(jwtUser, postID);
        return new ResponseEntity<>(new ApiResponse(true, "Like has been deleted"), HttpStatus.OK);
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<List<Like>> getLikesByComment( @PathVariable("commentId") Long commentID) throws Status436DoesntExistException {
        List<Like> likes = likeToCommentService.findAllLikesByComment(commentID);
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }

    @GetMapping("/users/comments/{userId}")
    public ResponseEntity<List<Like>> getAllCommentLikesByUser( @PathVariable("userId") JwtUser jwtUser) throws Status419UserException {
        List<Like> likes = likeToCommentService.findAllLikesByUser(jwtUser);
        return new ResponseEntity<>(likes, HttpStatus.OK);
    }

    @PostMapping("/comments/{commentId}")
    public ResponseEntity<ApiResponse> addLikeToComment(@PathVariable("commentId") Long commentID,
                                                        JwtUser jwtUser) throws Status436DoesntExistException, Status419UserException {
        likeToCommentService.addLikeToComment(jwtUser, commentID);
        return new ResponseEntity<>(new ApiResponse(true, "Like has been added"), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete-from-comment/{commentID}")
    public ResponseEntity<ApiResponse> deleteLikeFromComment(@PathVariable("commentID") final Long commentID,
                                                             JwtUser jwtUser) throws Status436DoesntExistException, Status419UserException {
        likeToCommentService.deleteLikeFromComment(jwtUser,commentID);
        return new ResponseEntity<>(new ApiResponse(true, "Like has been deleted"), HttpStatus.OK);
    }
}
