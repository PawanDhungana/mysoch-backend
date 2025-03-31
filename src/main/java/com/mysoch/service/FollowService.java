package com.mysoch.service;

import com.mysoch.model.User;

import java.util.List;

public interface FollowService {

    void followUser(User follower, User following);

    void unfollowUser(User follower, User following);

    boolean isFollowing(User follower, User following);

    List<User> getFollowings(User follower);

    List<User> getFollowers(User user);
}
