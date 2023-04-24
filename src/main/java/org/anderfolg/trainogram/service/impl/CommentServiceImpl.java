package org.anderfolg.trainogram.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anderfolg.trainogram.entities.Comment;
import org.anderfolg.trainogram.entities.dto.CommentDto;
import org.anderfolg.trainogram.entities.dto.NotificationDto;
import org.anderfolg.trainogram.entities.NotificationType;
import org.anderfolg.trainogram.entities.Post;
import org.anderfolg.trainogram.entities.User;
import org.anderfolg.trainogram.exceptions.Status419UserException;
import org.anderfolg.trainogram.exceptions.Status436DoesntExistException;
import org.anderfolg.trainogram.repo.CommentRepository;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.anderfolg.trainogram.service.CommentService;
import org.anderfolg.trainogram.service.NotificationService;
import org.anderfolg.trainogram.service.PostService;
import org.anderfolg.trainogram.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;
    private final NotificationService notificationService;


    @Override
    public List<CommentDto> findCommentsByPost(Post post) {
        List<Comment> commentList = commentRepository.findAllByPost(post);
        List<CommentDto> comments = commentList.stream().map(this::getDtoFromComment).toList();
        log.info("getting all comments from post with ID: {}", post.getId());
        return comments;
    }

    @Override
    public Comment findCommentById( Long commentID ) throws Status436DoesntExistException {
        return commentRepository.findById(commentID)
                .orElseThrow(() -> new Status436DoesntExistException("Comment id is invalid " + commentID));
    }

    @Override
    public void addComment(String content, JwtUser jwtUser, Long postId) throws Status436DoesntExistException, Status419UserException {

        User user = userService.findUserById(jwtUser.id());

        Post post = postService.findPostById(postId);

        Comment comment = Comment.builder()
                .content(content)
                .user(user)
                .post(post)
                .build();

        commentRepository.save(comment);

        NotificationDto notificationDto = NotificationDto.builder()
                .contentId(comment.getId())
                .recipientId(post.getUser().getId())
                .message("Your post has been commented by: " + user.getUsername())
                .build();

        notificationService.createNotification(notificationDto, post.getUser(), NotificationType.COMMENT);

        // Log the commented
        log.info("Saved comment by user {}", user.getUsername());
    }


    @Override
    @Transactional
    public void deleteComment( Long id, JwtUser jwtUser ) throws Status419UserException {
        User user = userService.findUserById(jwtUser.id());
        commentRepository.deleteById(id);
        log.info("deleting user {}'s comment", user.getUsername());
    }

    @Override
    public void updateComment(String content, JwtUser jwtUser, Long comID) throws Status436DoesntExistException, Status419UserException {
        Comment comment = commentRepository.findById(comID)
                .orElseThrow(() -> new Status436DoesntExistException("Could not find comment"));
        User user = userService.findUserById(jwtUser.id());
        comment.setContent(content);
        commentRepository.save(comment);
        log.info("updating comment by user {}", user.getUsername());
    }

    //  TODO (Bogdan O.) 24/4/23: use ModelMapper instead
    public CommentDto getDtoFromComment( Comment comment ) {
        return new CommentDto(comment);
    }

}
