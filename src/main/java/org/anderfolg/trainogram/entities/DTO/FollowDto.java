package org.anderfolg.trainogram.entities.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.anderfolg.trainogram.entities.Follow;
import org.anderfolg.trainogram.entities.User;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class FollowDto {
    private User follower;
    private User following;

    public FollowDto( Follow follow) {
        this.setFollower(follow.getFollower());
        this.setFollowing(follow.getFollowing());
    }
}
