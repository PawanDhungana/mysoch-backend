package com.mysoch.service;

import com.mysoch.model.Follow;
import com.mysoch.model.Post;
import com.mysoch.model.User;
import com.mysoch.repository.FollowRepository;
import com.mysoch.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final FollowRepository followRepository;

    @Override
    public Post createPost(String content, User author) {
        Post post = Post.builder()
                .content(content)
                .createdAt(LocalDateTime.now())
                .author(author)
                .build();
        return postRepository.save(post);
    }

    @Override
    public List<Post> getPostsByUser(User user) {
        return postRepository.findByAuthor(user);
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public List<Post> getFeedForUser(User user) {
        // Step 1: Get all users the current user is following
        List<User> followings = followRepository.findAllByFollower(user)
                .stream()
                .map(Follow::getFollowing)
                .toList();

        // Step 2: Get all posts authored by those users
        return postRepository.findByAuthorInOrderByCreatedAtDesc(followings);
    }
}
