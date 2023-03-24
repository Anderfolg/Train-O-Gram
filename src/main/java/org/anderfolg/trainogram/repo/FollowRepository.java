package org.anderfolg.trainogram.repo;

import org.anderfolg.trainogram.entities.Follow;
import org.anderfolg.trainogram.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    @Query("from Follow where following=:user")
    List<Follow> getFollowersByUser( @Param("user") User user);

    @Query("from Follow where follower=:user")
    List<Follow> getFollowingsByUser(@Param("user") User user);
}
