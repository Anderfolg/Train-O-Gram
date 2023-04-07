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
//  TODO (Bogdan O.) 7/4/23: remove CRUD namings
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
    public ResponseEntity<String> addComment( @RequestParam String content,
                                              @PathVariable ("postID") Long id,
                                              JwtUser jwtUser ) throws Status436PostDoesntExistException, Status419UserException {
        commentService.addComment(content, jwtUser, id);
        return ResponseEntity.ok("Comment has been added");
    }

    @PutMapping("/update/{comID}")
    public ResponseEntity<ApiResponse> updateComment( @RequestParam String content,
                                                      JwtUser jwtUser,
                                                      @PathVariable("comID") Long comID) throws Status439CommentDoesntExistException, Status419UserException {
        commentService.updateComment(content, jwtUser, comID);
        return new ResponseEntity<>(new ApiResponse(true, "Comment has been updated"), HttpStatus.OK);
    }

    @DeleteMapping("/{comID}")
    public ResponseEntity<String> deleteComment(@PathVariable("comID") final Long comId,
                                                     JwtUser jwtUser) throws Status419UserException {
        commentService.deleteComment(comId, jwtUser);
        return ResponseEntity.ok("Comment has been deleted");
    }
}
