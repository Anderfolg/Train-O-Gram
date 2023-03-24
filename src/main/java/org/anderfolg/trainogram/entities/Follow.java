package org.anderfolg.trainogram.entities;

import lombok.*;
import org.anderfolg.trainogram.entities.DTO.FollowDto;

import javax.persistence.*;

@Entity
@Table(name = "follows")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "follower")
    private User follower;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "following")
    private User following;

    public Follow( FollowDto followDto) {
        this.follower = followDto.getFollower();
        this.following = followDto.getFollowing();
    }
}