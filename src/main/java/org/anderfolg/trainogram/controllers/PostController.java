package org.anderfolg.trainogram.controllers;


import org.anderfolg.trainogram.entities.ApiResponse;
import org.anderfolg.trainogram.entities.DTO.PostDto;
import org.anderfolg.trainogram.entities.Post;
import org.anderfolg.trainogram.exceptions.*;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.anderfolg.trainogram.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController( PostService postService ) {
        this.postService = postService;
    }

    @GetMapping("/")
    public ResponseEntity<List<Post>> getPosts() {
        List<Post> body = postService.listPosts();
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<PostDto>> getPostsByUserId( @PathVariable("id") Long userID) throws Status419UserException {

        List<PostDto> posts = postService.listPostsByUser(userID);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PostMapping(value="/add")
    public ResponseEntity<ApiResponse> addPost( @RequestParam @Valid String description,
                                                JwtUser jwtUser,
                                                @RequestPart("image") MultipartFile image) throws Status435StorageException, Status432InvalidFileNameException, Status430InvalidFileException, Status419UserException {
        postService.addPost(description, jwtUser, image);
        return new ResponseEntity<>(new ApiResponse(true, "Post has been added"), HttpStatus.CREATED);
    }

    @PutMapping(value="/update/{postID}")
    public ResponseEntity<ApiResponse> updatePost(@RequestParam @Valid String description,
                                                  @PathVariable("postID") Long postId,
                                                  JwtUser jwtUser,
                                                  @RequestPart("image") MultipartFile image) throws Status436PostDoesntExistException, Status435StorageException, Status432InvalidFileNameException, Status430InvalidFileException, Status419UserException {
        postService.updatePost(postId, description, jwtUser, image);
        return new ResponseEntity<>(new ApiResponse(true, "Post has been updated"), HttpStatus.OK);
    }

    @DeleteMapping("/{postID}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable("postID") final Long postID,
                                                  JwtUser jwtUser) throws Status419UserException, Status436PostDoesntExistException {
        postService.deletePost(postID, jwtUser);
        return new ResponseEntity<>(new ApiResponse(true, "Post has been deleted"), HttpStatus.OK);
    }
}
