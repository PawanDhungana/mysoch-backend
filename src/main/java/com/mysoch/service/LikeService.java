package com.mysoch.service;

import com.mysoch.model.Post;
import com.mysoch.model.User;

import java.util.List;

public interface LikeService {

    void likePost(User user, Post post);

    void unlikePost(User user, Post post);

    boolean hasLiked(User user, Post post);

    long getLikeCount(Post post);

    List<User> getUsersWhoLiked(Post post);
}
