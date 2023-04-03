package org.anderfolg.trainogram.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anderfolg.trainogram.entities.*;
import org.anderfolg.trainogram.entities.DTO.NotificationDto;
import org.anderfolg.trainogram.exceptions.Status419UserException;
import org.anderfolg.trainogram.exceptions.Status438LikeDoesntExistException;
import org.anderfolg.trainogram.exceptions.Status439CommentDoesntExistException;
import org.anderfolg.trainogram.exceptions.Status440LikeAlreadyExistsException;
import org.anderfolg.trainogram.repo.LikeV2repository;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.anderfolg.trainogram.service.CommentService;
import org.anderfolg.trainogram.service.LikeToCommentService;
import org.anderfolg.trainogram.service.NotificationService;
import org.anderfolg.trainogram.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class LikeToCommentServiceImpl implements LikeToCommentService {
    private final LikeV2repository likeV2repository;
    private final UserService userService;
    private final CommentService commentService;
    private final NotificationService notificationService;


    @Override
    public List<Like> findAllLikesByComment( Long commentID ) throws Status439CommentDoesntExistException {
        Comment comment = commentService.findCommentById(commentID);
        log.info("getting all likes from comment by {}", comment.getUser());
        return likeV2repository.findAllByContentIdAndContentType(comment.getId(), ContentType.COMMENT);
    }

    @Override
    public List<Like> findAllLikesByUser( JwtUser jwtUser ) throws Status419UserException {
        User user = userService.findUserById(jwtUser.id());
        log.info("getting all likes to comment by user {}", user.getUsername());
        return likeV2repository.findAllByContentTypeAndUser(ContentType.COMMENT, user);
    }
    @Override
    public void addLikeToComment( JwtUser jwtUser, Long commentID ) throws Status439CommentDoesntExistException, Status440LikeAlreadyExistsException, Status419UserException {
        User user = userService.findUserById(jwtUser.id());
        Comment comment = commentService.findCommentById(commentID);
        if ( !likeV2repository.existsByUserAndContentId(user,commentID) ){
            Like like = new Like(user, comment.getId(), ContentType.COMMENT);
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
            throw new Status440LikeAlreadyExistsException("Like was already added");
        }

    }

    @Override
    @Transactional
    public void deleteLikeFromComment( JwtUser jwtUser, Long commentID ) throws Status439CommentDoesntExistException, Status438LikeDoesntExistException, Status419UserException {
        User user = userService.findUserById(jwtUser.id());
        Comment comment = commentService.findCommentById(commentID);
        if ( likeV2repository.existsByUserAndContentId(user,commentID) ){
            Like like = likeV2repository.findLikeV2ByContentIdAndUser(comment.getId(), user);
            likeV2repository.deleteLikeV2ById(like.getId());
            log.info("deleting like to comment with id : {} by user: {}", comment.getId(), user.getUsername());
        }
        else {
            throw new Status438LikeDoesntExistException("Like doesn't exist");
        }

    }

}
