package org.anderfolg.trainogram.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anderfolg.trainogram.entities.*;
import org.anderfolg.trainogram.entities.dto.NotificationDto;
import org.anderfolg.trainogram.exceptions.Status419UserException;
import org.anderfolg.trainogram.exceptions.Status420AlreadyExistsException;
import org.anderfolg.trainogram.exceptions.Status436DoesntExistException;
import org.anderfolg.trainogram.repo.LikeV2repository;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.anderfolg.trainogram.service.LikeToPostService;
import org.anderfolg.trainogram.service.NotificationService;
import org.anderfolg.trainogram.service.PostService;
import org.anderfolg.trainogram.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
public class LikeToPostServiceImpl implements LikeToPostService {
    private final LikeV2repository likeV2repository;
    private final UserService userService;
    private final PostService postService;
    private final NotificationService notificationService;


    @Override
    public Page<Like> findAllLikesByPost( Long postID, int page, int size) throws Status436DoesntExistException {
        Post post = postService.findPostById(postID);
        Pageable pageable = PageRequest.of(page, size);
        log.info("getting all likes from post with id : {}", post.getId());
        return likeV2repository.findAllByContentIdAndContentType(postID , ContentType.POST, pageable);
    }


    @Override
    public Page<Like> findAllLikesByUser(JwtUser jwtUser, int page, int size) throws Status419UserException {
        User user = userService.findUserById(jwtUser.id());
        Pageable pageable = PageRequest.of(page, size);
        log.info("getting all likes to posts from user : {}", user.getUsername());
        return likeV2repository.findAllByContentTypeAndUser(ContentType.POST, user, pageable);
    }



    @Override
    public void addLikeToPost( JwtUser jwtUser, Long postID ) throws Status436DoesntExistException, Status419UserException {
        User user = userService.findUserById(jwtUser.id());
        Post post = postService.findPostById(postID);
        if ( !likeV2repository.existsByUserAndContentId(user,postID) ){


            Like like = new Like(user, post.getId(), ContentType.POST);
            likeV2repository.save(like);

            NotificationDto notificationDto = NotificationDto.builder()
                    .contentId(postID)
                    .recipientId(post.getUser().getId())
                    .message("Your post has been liked by: "+user.getUsername())
                    .build();
            notificationService.createNotification(notificationDto, post.getUser(), NotificationType.LIKE);
            log.info("adding like to post with id : {}", post.getId());
        }
        else {
            throw new Status420AlreadyExistsException("Like was already added");
        }

    }

    @Override
    @Transactional
    public void deleteLikeFromPost( JwtUser jwtUser, Long postID ) throws Status436DoesntExistException, Status419UserException {
        User user = userService.findUserById(jwtUser.id());
        Post post = postService.findPostById(postID);
        if ( likeV2repository.existsByUserAndContentId(user, postID) ){
            Like like = likeV2repository.findLikeV2ByContentIdAndUser(post.getId(), user);
            likeV2repository.deleteLikeV2ById(like.getId());
            log.info("deleting like from post with id : {}", post.getId());
        }
        else {
            throw new Status436DoesntExistException("Like doesn't exist");
        }
    }
}
