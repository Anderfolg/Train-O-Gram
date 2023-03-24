package org.anderfolg.trainogram.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anderfolg.trainogram.entities.ContentType;
import org.anderfolg.trainogram.entities.DTO.PostDto;
import org.anderfolg.trainogram.entities.Post;
import org.anderfolg.trainogram.entities.User;
import org.anderfolg.trainogram.exceptions.*;
import org.anderfolg.trainogram.repo.PostRepository;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.anderfolg.trainogram.service.ImageService;
import org.anderfolg.trainogram.service.PostService;
import org.anderfolg.trainogram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ImageService imageService;
    private final UserService userService;

    @Override
    public List<Post> listPosts() {
        return postRepository.findAllByType(ContentType.POST);
    }

    @Override
    public List<PostDto> listPostsByUser( Long userID ) throws Status419UserException {
        User user = userService.findUserById(userID);
        List<Post> postList = postRepository.findAllByUser(user);
        List<PostDto> posts = new ArrayList<>();
        for (Post post:postList){
            PostDto postDto = getDtoFromPost(post);
            posts.add(postDto);
        }
        return posts;
    }

    @Override
    public Post findPostById( Long id ) throws Status436PostDoesntExistException {
        Optional<Post> optionalPost = postRepository.findById(id);
        if ( optionalPost.isEmpty() )
            throw new Status436PostDoesntExistException("Post id is invalid " + id);
        return optionalPost.get();
    }


    @Override
    public void addPost( String description, JwtUser jwtUser, MultipartFile file ) throws Status435StorageException, Status432InvalidFileNameException, Status430InvalidFileException, Status419UserException {
        User user = userService.findUserById(jwtUser.id());
        Post post = getPostFromRequest(description, user, file);
        postRepository.save(post);
        log.info("user {} adding new post with image : {}", user.getUsername(), file.getOriginalFilename());
    }

    @Transactional
    @Override
    public void updatePost(Long postID, String description, JwtUser jwtUser, MultipartFile file ) throws Status435StorageException, Status432InvalidFileNameException, Status430InvalidFileException, Status436PostDoesntExistException, Status419UserException {
        if ( postRepository.existsById(postID) && postRepository.findById(postID).isPresent()){
            User user = userService.findUserById(jwtUser.id());
            Post post = postRepository.findById(postID).get();
            post.setImageName(System.currentTimeMillis() + "_" + file.getOriginalFilename());
            post.setImageUrl(imageService.uploadImage(file, user.getUsername()));
            post.setDescription(description);
            postRepository.save(post);
        }
        else throw new Status436PostDoesntExistException("Post was not found");
    }

    @Override
    @Transactional
    public void deletePost( Long id, JwtUser jwtUser ) throws Status419UserException, Status436PostDoesntExistException {
        User user = userService.findUserById(jwtUser.id());
        if ( postRepository.findById(id).isPresent() ){
            imageService.deleteImage(postRepository.findById(id).get().getImageName());
            postRepository.deleteById(id);
            log.info("deleting post from user: {}", user.getUsername());
        }
        else throw new Status436PostDoesntExistException("Post was not found");
    }

    @Override
    public Long findLatestPostId() {
        List<Post> posts = postRepository.findAll();
        return posts.get(posts.size()-1).getId();
    }

    @Override
    public List<PostDto> listPostsByUserAndType( JwtUser jwtUser, ContentType type ) throws Status419UserException {
        User user = userService.findUserById(jwtUser.id());
        List<Post> postList = postRepository.findAllByUserAndType(user, type);
        List<PostDto> posts = new ArrayList<>();
        for (Post post:postList) {
            PostDto postDto = getDtoFromPost(post);
            posts.add(postDto);
        }
        return posts;
    }

    @Override
    public List<PostDto> listPostsByType( ContentType type ) {
        List<Post> postList = postRepository.findAllByType(type);
        List<PostDto> posts = new ArrayList<>();
        for (Post post:postList) {
            PostDto postDto = getDtoFromPost(post);

            posts.add(postDto);
        }
        return posts;
    }

    public PostDto getDtoFromPost(Post post) {
        PostDto postDto = new PostDto();
        postDto.setImageUrl(post.getImageUrl());
        postDto.setDescription(post.getDescription());
        return postDto;
    }

    /*public Post getPostFromDto(PostDto postDto, User user, MultipartFile image) throws Status435StorageException, Status432InvalidFileNameException, Status430InvalidFileException {
        Post post = new Post();
        post.setImageName(System.currentTimeMillis() + "_" + image.getOriginalFilename());
        post.setImageUrl(imageService.uploadImage(image, user.getUsername()));
        post.setDescription(postDto.getDescription());
        post.setUser(user);
        return post;
    }*/
    public Post getPostFromRequest(String description, User user, MultipartFile image) throws Status435StorageException, Status432InvalidFileNameException, Status430InvalidFileException {
        Post post = new Post();
        post.setImageName(imageService.uploadImage(image, user.getUsername()));
        post.setImageUrl(imageService.getUploadDirectory()+user.getUsername());
        post.setDescription(description);
        post.setType(ContentType.POST);
        post.setUser(user);
        return post;
    }
}
