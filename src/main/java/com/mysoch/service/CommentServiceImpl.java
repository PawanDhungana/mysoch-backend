package com.mysoch.service;

import com.mysoch.model.Comment;
import com.mysoch.model.Post;
import com.mysoch.model.User;
import com.mysoch.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Comment addComment(String content, User author, Post post) {
        Comment comment = Comment.builder()
                .content(content)
                .createdAt(LocalDateTime.now())
                .author(author)
                .post(post)
                .build();

        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> getCommentsForPost(Post post) {
        return commentRepository.findByPostOrderByCreatedAtDesc(post);
    }

    @Override
    public void deleteComment(Long commentId, User requester) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found."));

        boolean isAuthor = comment.getAuthor().getId().equals(requester.getId());
        boolean isPostOwner = comment.getPost().getAuthor().getId().equals(requester.getId());

        if (!isAuthor && !isPostOwner) {
            throw new SecurityException("You are not allowed to delete this comment.");
        }

        commentRepository.delete(comment);
    }

    @Override
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + id));
    }
}
