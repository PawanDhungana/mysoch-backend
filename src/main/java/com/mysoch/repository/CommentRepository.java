package com.mysoch.repository;

import com.mysoch.model.Comment;
import com.mysoch.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Get all comments for a specific post, newest first
    List<Comment> findByPostOrderByCreatedAtDesc(Post post);
}
