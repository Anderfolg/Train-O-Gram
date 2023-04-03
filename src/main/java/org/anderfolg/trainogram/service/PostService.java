package org.anderfolg.trainogram.service;

import org.anderfolg.trainogram.entities.ContentType;
import org.anderfolg.trainogram.entities.DTO.PostDto;
import org.anderfolg.trainogram.entities.Post;
import org.anderfolg.trainogram.exceptions.*;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface PostService {
    List<Post> listPosts();
    List<PostDto> listPostsByUser(Long userID)
            throws Status419UserException;
    Post findPostById(Long id)
            throws Status436PostDoesntExistException;
    void addPost( String description, JwtUser jwtUser, MultipartFile file )
            throws Status435StorageException,
            Status432InvalidFileNameException,
            Status430InvalidFileException,
            Status419UserException;
    void updatePost(Long postID, String description, JwtUser jwtUser, MultipartFile file)
            throws Status435StorageException,
            Status432InvalidFileNameException,
            Status430InvalidFileException,
            Status436PostDoesntExistException,
            Status419UserException;
    void deletePost(Long id, JwtUser jwtUser)
            throws Status419UserException, Status436PostDoesntExistException;
    Long findLatestPostId();
    List<PostDto> listPostsByUserAndType( JwtUser jwtUser, ContentType type )
            throws Status419UserException;
    List<PostDto> listPostsByType(ContentType type);
}
