package com.mysoch.repository;

import com.mysoch.model.Like;
import com.mysoch.model.Post;
import com.mysoch.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    // Check if a user already liked a post
    boolean existsByUserAndPost(User user, Post post);

    // Fetch like object for toggle/remove
    Optional<Like> findByUserAndPost(User user, Post post);

    // Get all likes for a specific post
    List<Like> findAllByPost(Post post);

    // Optional: Count likes on a post
    long countByPost(Post post);
}
