package org.anderfolg.trainogram.service;

import org.anderfolg.trainogram.entities.ContentType;
import org.anderfolg.trainogram.entities.dto.PostDto;
import org.anderfolg.trainogram.entities.Post;
import org.anderfolg.trainogram.exceptions.*;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;



public interface PostService {
    Page<Post> listPosts(Long cursorId, int pageSize);
    Page<PostDto> listPostsByUser(Long userID, int page, int size)
            throws Status419UserException;
    Post findPostById(Long id)
            throws Status436DoesntExistException;
    void addPost( String description, JwtUser jwtUser, MultipartFile file )
            throws Status435StorageException,
            Status432InvalidFileNameException,
            Status430InvalidFileException,
            Status419UserException;
    void updatePost(Long postID, String description, JwtUser jwtUser, MultipartFile file)
            throws Status435StorageException,
            Status432InvalidFileNameException,
            Status430InvalidFileException,
            Status436DoesntExistException,
            Status419UserException;
    void deletePost(Long id, JwtUser jwtUser)
            throws Status419UserException, Status436DoesntExistException;
    Long findLatestPostId();
    Page<PostDto> listPostsByUserAndType(JwtUser jwtUser, ContentType type, int page, int size)
            throws Status419UserException;
    Page<PostDto> listPostsByType(ContentType type, int page, int size);
}
