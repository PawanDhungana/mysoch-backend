package com.mysoch.repository;

import com.mysoch.model.Follow;
import com.mysoch.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    // Check if a user is already following another user
    boolean existsByFollowerAndFollowing(User follower, User following);

    // Used to unfollow
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);

    // Get all users followed by a user
    List<Follow> findAllByFollower(User follower);

    // Get all users following a user (optional)
    List<Follow> findAllByFollowing(User following);
}
