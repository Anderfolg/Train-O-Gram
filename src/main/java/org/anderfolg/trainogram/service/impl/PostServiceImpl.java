package org.anderfolg.trainogram.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anderfolg.trainogram.entities.ContentType;
import org.anderfolg.trainogram.entities.dto.PostDto;
import org.anderfolg.trainogram.entities.Post;
import org.anderfolg.trainogram.entities.User;
import org.anderfolg.trainogram.exceptions.*;
import org.anderfolg.trainogram.repo.PostRepository;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.anderfolg.trainogram.service.ImageService;
import org.anderfolg.trainogram.service.PostService;
import org.anderfolg.trainogram.service.UserService;
import org.springframework.data.domain.*;
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
    public Page<PostDto> listPostsByUser(Long userID, int page, int size) throws Status419UserException {
        User user = userService.findUserById(userID);
        Pageable paging = PageRequest.of(page, size, Sort.by("createDate").descending());
        Page<Post> postPage = postRepository.findAllByUser(user, paging);
        List<PostDto> posts = new ArrayList<>();
        for (Post post:postPage.getContent()){
            PostDto postDto = getDtoFromPost(post);
            posts.add(postDto);
        }
        return new PageImpl<>(posts, paging, postPage.getTotalElements());
    }

    @Override
    public Post findPostById( Long id ) throws Status436DoesntExistException {
        Optional<Post> optionalPost = postRepository.findById(id);
        if ( optionalPost.isEmpty() )
            throw new Status436DoesntExistException("Post id is invalid " + id);
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
    public void updatePost(Long postID, String description, JwtUser jwtUser, MultipartFile file ) throws Status435StorageException, Status432InvalidFileNameException, Status430InvalidFileException, Status436DoesntExistException, Status419UserException {
        Optional<Post> optionalPost = postRepository.findById(postID);
        if (optionalPost.isPresent()){
            User user = userService.findUserById(jwtUser.id());
            Post post = optionalPost.get();
            post.setImageName(System.currentTimeMillis() + "_" + file.getOriginalFilename());
            post.setImageUrl(imageService.uploadImage(file, user.getUsername()));
            post.setDescription(description);
            postRepository.save(post);
        }
        else throw new Status436DoesntExistException("Post was not found");
    }

    @Override
    @Transactional
    public void deletePost(Long id, JwtUser jwtUser) throws Status419UserException, Status436DoesntExistException {
        User user = userService.findUserById(jwtUser.id());
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            imageService.deleteImage(post.get().getImageName());
            postRepository.deleteById(id);
            log.info("deleting post from user: {}", user.getUsername());
        } else {
            throw new Status436DoesntExistException("Post was not found");
        }
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
        postDto.setUser(post.getUser());
        postDto.setImageName(post.getImageName());
        return postDto;
    }
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
