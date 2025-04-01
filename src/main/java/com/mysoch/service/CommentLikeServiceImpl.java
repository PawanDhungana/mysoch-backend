package com.mysoch.service;

import com.mysoch.model.Comment;
import com.mysoch.model.CommentLike;
import com.mysoch.model.User;
import com.mysoch.repository.CommentLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;

    @Override
    public void likeComment(User user, Comment comment) {
        if (!commentLikeRepository.existsByUserAndComment(user, comment)) {
            commentLikeRepository.save(CommentLike.builder()
                    .user(user)
                    .comment(comment)
                    .build());
        }
    }

    @Override
    public void unlikeComment(User user, Comment comment) {
        commentLikeRepository.findByUserAndComment(user, comment)
                .ifPresent(commentLikeRepository::delete);
    }

    @Override
    public boolean hasLiked(User user, Comment comment) {
        return commentLikeRepository.existsByUserAndComment(user, comment);
    }

    @Override
    public long getLikeCount(Comment comment) {
        return commentLikeRepository.countByComment(comment);
    }
}
