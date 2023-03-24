package org.anderfolg.trainogram.controllers;


import org.anderfolg.trainogram.entities.ApiResponse;
import org.anderfolg.trainogram.entities.DTO.CommentDto;
import org.anderfolg.trainogram.entities.Post;
import org.anderfolg.trainogram.exceptions.Status439CommentDoesntExistException;
import org.anderfolg.trainogram.exceptions.Status436PostDoesntExistException;
import org.anderfolg.trainogram.exceptions.Status419UserException;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.anderfolg.trainogram.service.CommentService;
import org.anderfolg.trainogram.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;
    private final PostService postService;

    @Autowired
    public CommentController( CommentService commentService, PostService postService ) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @GetMapping("/{postID}")
    public ResponseEntity<List<CommentDto>> getCommentsByPost( @PathVariable("postID") Long id ) throws Status436PostDoesntExistException {
        Post post = postService.findPostById(id);
        List<CommentDto> comments = commentService.findCommentsByPost(post);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping("/add/{postID}")
    public ResponseEntity<ApiResponse> addComment( @RequestParam String content,
                                                   @PathVariable ("postID") Long id,
                                                   JwtUser jwtUser ) throws Status436PostDoesntExistException, Status419UserException {
        /*jwtTokenProvider.getAuthentication(token);
        User user = userService.findByUsername(jwtTokenProvider.getUsername(token));*/
        /*Post post  = postService.findPostById(commentaryDto.getPostId());*/
        commentService.addComment(content, jwtUser, id);
        // TODO: 27/2/23 simplify methods to return only responsible body with content which FE requires
        return new ResponseEntity<>(new ApiResponse(true, "Comment has been added"), HttpStatus.CREATED);
    }

    @PutMapping("/update/{comID}")
    public ResponseEntity<ApiResponse> updateComment( @RequestParam String content,
                                                      JwtUser jwtUser,
                                                      @PathVariable("comID") Long comID) throws Status439CommentDoesntExistException, Status419UserException {
        commentService.updateComment(content, jwtUser, comID);
        return new ResponseEntity<>(new ApiResponse(true, "Comment has been updated"), HttpStatus.OK);
    }

    @DeleteMapping("/{comID}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable("comID") final Long comId,
                                                     JwtUser jwtUser) throws Status419UserException {
        /*jwtTokenProvider.getAuthentication(token);
        User user = userService.findByUsername(jwtTokenProvider.getUsername(token));*/
        commentService.deleteComment(comId, jwtUser);
        // TODO: 27/2/23 simplify responses and do not return body on delete mapping
        return new ResponseEntity<>(new ApiResponse(true, "Comment has been deleted"), HttpStatus.OK);
    }
}
