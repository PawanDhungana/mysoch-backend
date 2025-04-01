package com.mysoch.repository;

import com.mysoch.model.Comment;
import com.mysoch.model.CommentLike;
import com.mysoch.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    boolean existsByUserAndComment(User user, Comment comment);

    Optional<CommentLike> findByUserAndComment(User user, Comment comment);

    long countByComment(Comment comment);
}
