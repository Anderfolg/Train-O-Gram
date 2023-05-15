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
import org.anderfolg.trainogram.service.CommentService;
import org.anderfolg.trainogram.service.LikeToCommentService;
import org.anderfolg.trainogram.service.NotificationService;
import org.anderfolg.trainogram.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
public class LikeToCommentServiceImpl implements LikeToCommentService {
    private final LikeV2repository likeV2repository;
    private final UserService userService;
    private final CommentService commentService;
    private final NotificationService notificationService;


    @Override
    public Page<Like> findAllLikesByComment( Long commentID, int page, int size) throws Status436DoesntExistException {
        Comment comment = commentService.findCommentById(commentID);
        log.info("getting all likes from comment by {}", comment.getUser());
        Pageable pageable = PageRequest.of(page, size);
        return likeV2repository.findAllByContentIdAndContentType(comment.getId(), ContentType.COMMENT, pageable);
    }


    @Override
    public Page<Like> findAllLikesByUser(JwtUser jwtUser, int page, int size) throws Status419UserException {
        User user = userService.findUserById(jwtUser.id());
        log.info("getting all likes to comment by user {}", user.getUsername());
        Pageable pageable = PageRequest.of(page, size);
        return likeV2repository.findAllByContentTypeAndUser(ContentType.COMMENT, user, pageable);
    }

    @Override
    public void addLikeToComment( JwtUser jwtUser, Long commentID ) throws Status436DoesntExistException, Status420AlreadyExistsException, Status419UserException {
        User user = userService.findUserById(jwtUser.id());
        Comment comment = commentService.findCommentById(commentID);
        if ( !likeV2repository.existsByUserAndContentId(user,commentID) ){

            Like like = Like.builder()
                    .user(user)
                    .contentId(comment.getId())
                    .contentType(ContentType.COMMENT)
                    .build();
            likeV2repository.save(like);
            log.info("adding like to comment with id : {} from user: {}", comment.getId(), user.getUsername());
            NotificationDto notificationDto = NotificationDto.builder()
                    .contentId(commentID)
                    .recipientId(comment.getUser().getId())
                    .message("Your comment has been liked by :"+user.getUsername())
                    .build();
            notificationService.createNotification(notificationDto, comment.getUser(), NotificationType.LIKE);
        }
        else {
            throw new Status420AlreadyExistsException("Like was already added");
        }

    }

    @Override
    @Transactional
    public void deleteLikeFromComment( JwtUser jwtUser, Long commentID ) throws Status436DoesntExistException, Status419UserException {
        User user = userService.findUserById(jwtUser.id());
        Comment comment = commentService.findCommentById(commentID);
        if ( likeV2repository.existsByUserAndContentId(user,commentID) ){
            Like like = likeV2repository.findLikeV2ByContentIdAndUser(comment.getId(), user);
            likeV2repository.deleteLikeV2ById(like.getId());
            log.info("deleting like to comment with id : {} by user: {}", comment.getId(), user.getUsername());
        }
        else {
            throw new Status436DoesntExistException("Like doesn't exist");
        }

    }

}
