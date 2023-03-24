package org.anderfolg.trainogram.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anderfolg.trainogram.entities.DTO.FollowDto;
import org.anderfolg.trainogram.entities.DTO.NotificationDto;
import org.anderfolg.trainogram.entities.Follow;
import org.anderfolg.trainogram.entities.NotificationType;
import org.anderfolg.trainogram.entities.User;
import org.anderfolg.trainogram.exceptions.Status419UserException;
import org.anderfolg.trainogram.repo.FollowRepository;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.anderfolg.trainogram.service.FollowService;
import org.anderfolg.trainogram.service.NotificationService;
import org.anderfolg.trainogram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final UserService userService;
    private final NotificationService notificationService;


    @Override
    public List<FollowDto> findFollowersByUser( User user ) {
        List<Follow> followersList = followRepository.getFollowersByUser(user);

        List<FollowDto> followers = new ArrayList<>();
        for (Follow follower:followersList) {
            FollowDto followDto = getDtoFromFollow(follower);
            followers.add(followDto);
        }
        log.info("getting all followers from user {}", user.getUsername());
        return followers;
    }

    @Override
    public List<FollowDto> findFollowingsByUser( User user ) {
        List<Follow> followingsList = followRepository.getFollowingsByUser(user);

        List<FollowDto> followings = new ArrayList<>();
        for (Follow following:followingsList) {
            FollowDto followDto = getDtoFromFollow(following);
            followings.add(followDto);
        }
        log.info("getting all following from user {}", user.getUsername());
        return followings;
    }

    @Override
    public void addFollowing( JwtUser jwtUser, Long userId) throws Status419UserException {
        User follower = userService.findUserById(jwtUser.id());
        User following = userService.findUserById(userId);

        Follow follow = Follow.builder()
                .follower(follower)
                .following(following)
                .build();
        followRepository.save(follow);

        NotificationDto notificationDto = NotificationDto.builder()
                .contentId(userId)
                .recipientId(userId)
                .message("You have been followed by :"+follower.getUsername())
                .build();

        notificationService.createNotification(notificationDto, following, NotificationType.FOLLOW);

        log.info("adding following from user {} to {}", follower.getUsername(), following.getUsername());
    }

    @Override
    @Transactional
    public void deleteFollower( Long id, JwtUser jwtUser) throws Status419UserException {
        User user = userService.findUserById(jwtUser.id());
        followRepository.deleteById(id);
        log.info("deleting follower: {}", user.getUsername());
    }

    @Override
    @Transactional
    public void deleteFollowing( Long id,JwtUser jwtUser) throws Status419UserException {
        User user = userService.findUserById(jwtUser.id());
        followRepository.deleteById(id);
        log.info("deleting following: {}", user.getUsername());
    }
    public static FollowDto getDtoFromFollow(Follow follow) {
        return new FollowDto(follow);
    }

    public static Follow getFollowFromDto(FollowDto followDto) {
        return new Follow(followDto);
    }
}
