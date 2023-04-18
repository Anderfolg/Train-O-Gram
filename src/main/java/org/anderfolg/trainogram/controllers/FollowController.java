package org.anderfolg.trainogram.controllers;

import org.anderfolg.trainogram.entities.ApiResponse;
import org.anderfolg.trainogram.entities.dto.FollowDto;
import org.anderfolg.trainogram.entities.User;
import org.anderfolg.trainogram.exceptions.Status419UserException;
import org.anderfolg.trainogram.security.jwt.JwtUser;
import org.anderfolg.trainogram.service.FollowService;
import org.anderfolg.trainogram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/follows")
//  TODO (Bogdan O.) 7/4/23: use pagination for "getAll" method types
public class FollowController {
    private final FollowService followService;
    private final UserService userService;

    @Autowired
    public FollowController( FollowService followService, UserService userService ) {
        this.followService = followService;
        this.userService = userService;
    }

    @GetMapping("/followers/{userID}")
    public ResponseEntity<List<FollowDto>> getFollowersByUser( @PathVariable("userID") Long userId) throws Status419UserException {
        User user = userService.findUserById(userId);
        List<FollowDto> followers = followService.findFollowersByUser(user);
        return new ResponseEntity<>(followers, HttpStatus.OK);
    }

    @PostMapping("/{userID}")
    public ResponseEntity<ApiResponse> addFollowing( @PathVariable("userID") Long id,
                                                     JwtUser jwtUser) throws Status419UserException {
        followService.addFollowing(jwtUser,id);
        return new ResponseEntity<>(new ApiResponse(true, "Following has been added"), HttpStatus.CREATED);
    }

    @DeleteMapping("followings/{followID}")
    public ResponseEntity<ApiResponse> deleteFollowing(@PathVariable("followID") final Long followID,
                                                       JwtUser jwtUser) throws Status419UserException {
        followService.deleteFollowing(followID, jwtUser);
        return new ResponseEntity<>(new ApiResponse(true, "Following has been deleted"), HttpStatus.OK);
    }

    @DeleteMapping("followers/{followID}")
    public ResponseEntity<ApiResponse> deleteFollower(@PathVariable("followID") final Long followID,
                                                      JwtUser jwtUser) throws Status419UserException {
        followService.deleteFollower(followID, jwtUser);
        return new ResponseEntity<>(new ApiResponse(true, "Follower has been deleted"), HttpStatus.OK);
    }
}
