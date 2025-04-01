package com.mysoch.controller;

import com.mysoch.model.Comment;
import com.mysoch.model.Post;
import com.mysoch.model.User;
import com.mysoch.service.*;
import com.mysoch.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentLikeService commentLikeService;
    private final PostService postService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    // Helper to extract current user from token
    private User getCurrentUser(String authHeader) {
        String username = jwtUtil.extractUsername(authHeader.replace("Bearer ", ""));
        return userService.findByUsername(username).orElseThrow();
    }

    // POST /api/comments/{postId} - Add a comment to a post
    @PostMapping("/{postId}")
    public ResponseEntity<?> addComment(@PathVariable Long postId,
                                        @RequestBody Map<String, String> request,
                                        @RequestHeader("Authorization") String authHeader) {
        String content = request.get("content");
        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Comment content cannot be empty."));
        }

        User user = getCurrentUser(authHeader);
        Post post = postService.getPostById(postId);
        Comment comment = commentService.addComment(content, user, post);

        return ResponseEntity.ok(comment);
    }

    // GET /api/comments/{postId}/all - Get all comments for a post
    @GetMapping("/{postId}/all")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);
        List<Comment> comments = commentService.getCommentsForPost(post);
        return ResponseEntity.ok(comments);
    }

    // DELETE /api/comments/{commentId} - Delete a comment (by commenter or post owner)
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId,
                                           @RequestHeader("Authorization") String authHeader) {
        try {
            User user = getCurrentUser(authHeader);
            commentService.deleteComment(commentId, user);
            return ResponseEntity.ok(Map.of("message", "Comment deleted successfully."));
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(Map.of("message", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // POST /api/comments/{commentId}/like - Like a comment
    @PostMapping("/{commentId}/like")
    public ResponseEntity<?> likeComment(@PathVariable Long commentId,
                                         @RequestHeader("Authorization") String authHeader) {
        User user = getCurrentUser(authHeader);
        Comment comment = commentService.getCommentById(commentId);

        if (commentLikeService.hasLiked(user, comment)) {
            return ResponseEntity.badRequest().body(Map.of("message", "You already liked this comment."));
        }

        commentLikeService.likeComment(user, comment);
        return ResponseEntity.ok(Map.of("message", "Comment liked successfully."));
    }

    // DELETE /api/comments/{commentId}/like - Unlike a comment
    @DeleteMapping("/{commentId}/like")
    public ResponseEntity<?> unlikeComment(@PathVariable Long commentId,
                                           @RequestHeader("Authorization") String authHeader) {
        User user = getCurrentUser(authHeader);
        Comment comment = commentService.getCommentById(commentId);

        if (!commentLikeService.hasLiked(user, comment)) {
            return ResponseEntity.badRequest().body(Map.of("message", "You haven't liked this comment yet."));
        }

        commentLikeService.unlikeComment(user, comment);
        return ResponseEntity.ok(Map.of("message", "Comment unliked successfully."));
    }

    // GET /api/comments/{commentId}/likes/count - Count likes on comment
    @GetMapping("/{commentId}/likes/count")
    public ResponseEntity<?> getCommentLikeCount(@PathVariable Long commentId) {
        Comment comment = commentService.getCommentById(commentId);
        long count = commentLikeService.getLikeCount(comment);
        return ResponseEntity.ok(Map.of("likes", count));
    }

    // GET /api/comments/{commentId}/likes/liked - Check if user liked comment
    @GetMapping("/{commentId}/likes/liked")
    public ResponseEntity<?> hasLikedComment(@PathVariable Long commentId,
                                             @RequestHeader("Authorization") String authHeader) {
        User user = getCurrentUser(authHeader);
        Comment comment = commentService.getCommentById(commentId);
        boolean liked = commentLikeService.hasLiked(user, comment);
        return ResponseEntity.ok(Map.of("liked", liked));
    }
}
