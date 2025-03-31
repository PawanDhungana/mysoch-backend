package com.mysoch.service;

import com.mysoch.model.Follow;
import com.mysoch.model.User;
import com.mysoch.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {

    private final FollowRepository followRepository;

    @Override
    public void followUser(User follower, User following) {
        if (!followRepository.existsByFollowerAndFollowing(follower, following)) {
            followRepository.save(Follow.builder()
                    .follower(follower)
                    .following(following)
                    .build());
        }
    }

    @Override
    public void unfollowUser(User follower, User following) {
        followRepository.findByFollowerAndFollowing(follower, following)
                .ifPresent(followRepository::delete);
    }

    @Override
    public boolean isFollowing(User follower, User following) {
        return followRepository.existsByFollowerAndFollowing(follower, following);
    }

    @Override
    public List<User> getFollowings(User follower) {
        return followRepository.findAllByFollower(follower).stream()
                .map(Follow::getFollowing)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getFollowers(User user) {
        return followRepository.findAllByFollowing(user).stream()
                .map(Follow::getFollower)
                .collect(Collectors.toList());
    }
}
