package org.anderfolg.trainogram.controllers;


import org.anderfolg.trainogram.entities.ApiResponse;
import org.anderfolg.trainogram.entities.dto.PostDto;
import org.anderfolg.trainogram.entities.Post;
import org.anderfolg.trainogram.exceptions.*;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.anderfolg.trainogram.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController( PostService postService ) {
        this.postService = postService;
    }

    @GetMapping("/")
    public ResponseEntity<Page<Post>> getPosts(@RequestParam(required = false) Long cursorId,
                                               @RequestParam(defaultValue = "10") int pageSize) {
        Page<Post> posts = postService.listPosts(cursorId, pageSize);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Page<PostDto>> getPostsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws Status419UserException {
        Page<PostDto> postDtoPage = postService.listPostsByUser(userId, page, size);
        return new ResponseEntity<>(postDtoPage, HttpStatus.OK);
    }

    @PostMapping(value="/")
    public ResponseEntity<ApiResponse> addPost( @RequestParam @Valid String description,
                                                JwtUser jwtUser,
                                                @RequestPart("image") MultipartFile image) throws Status435StorageException, Status432InvalidFileNameException, Status430InvalidFileException, Status419UserException {
        postService.addPost(description, jwtUser, image);
        return new ResponseEntity<>(new ApiResponse(true, "Post has been added"), HttpStatus.CREATED);
    }

    @PutMapping(value="/{postID}")
    public ResponseEntity<ApiResponse> updatePost(@RequestParam @Valid String description,
                                                  @PathVariable("postID") Long postId,
                                                  JwtUser jwtUser,
                                                  @RequestPart("image") MultipartFile image) throws Status436DoesntExistException, Status435StorageException, Status432InvalidFileNameException, Status430InvalidFileException, Status419UserException {
        postService.updatePost(postId, description, jwtUser, image);
        return new ResponseEntity<>(new ApiResponse(true, "Post has been updated"), HttpStatus.OK);
    }

    @DeleteMapping("/{postID}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable("postID") final Long postID,
                                                  JwtUser jwtUser) throws Status419UserException, Status436DoesntExistException {
        postService.deletePost(postID, jwtUser);
        return new ResponseEntity<>(new ApiResponse(true, "Post has been deleted"), HttpStatus.OK);
    }
}
