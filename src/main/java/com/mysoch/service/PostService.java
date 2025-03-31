package com.mysoch.service;

import com.mysoch.model.Post;
import com.mysoch.model.User;

import java.util.List;

public interface PostService {

    Post createPost(String content, User author);

    List<Post> getPostsByUser(User user);

    List<Post> getAllPosts(); // for global feed

    List<Post> getFeedForUser(User user); //to allow a logged-in user to view posts only from users they follow
}
