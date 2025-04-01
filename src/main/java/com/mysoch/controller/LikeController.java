package com.mysoch.controller;

import com.mysoch.model.Post;
import com.mysoch.model.User;
import com.mysoch.service.LikeService;
import com.mysoch.service.PostService;
import com.mysoch.service.UserService;
import com.mysoch.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;
    private final PostService postService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    // Helper to get logged-in user from JWT
    private User getCurrentUser(String authHeader) {
        String username = jwtUtil.extractUsername(authHeader.replace("Bearer ", ""));
        return userService.findByUsername(username).orElseThrow();
    }

    // POST /api/likes/{postId} - Like a post
    @PostMapping("/{postId}")
    public ResponseEntity<?> likePost(@PathVariable Long postId,
                                      @RequestHeader("Authorization") String authHeader) {
        User user = getCurrentUser(authHeader);
        Post post = postService.getPostById(postId);

        if (likeService.hasLiked(user, post)) {
            return ResponseEntity.badRequest().body(Map.of("message", "You already liked this post."));
        }

        likeService.likePost(user, post);
        return ResponseEntity.ok(Map.of("message", "Post liked successfully."));
    }

    // DELETE /api/likes/{postId} - Unlike a post
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> unlikePost(@PathVariable Long postId,
                                        @RequestHeader("Authorization") String authHeader) {
        User user = getCurrentUser(authHeader);
        Post post = postService.getPostById(postId);

        if (!likeService.hasLiked(user, post)) {
            return ResponseEntity.badRequest().body(Map.of("message", "You haven't liked this post yet."));
        }

        likeService.unlikePost(user, post);
        return ResponseEntity.ok(Map.of("message", "Post unliked successfully."));
    }

    // GET /api/likes/{postId}/count - Get number of likes on a post
    @GetMapping("/{postId}/count")
    public ResponseEntity<?> getLikeCount(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);
        long count = likeService.getLikeCount(post);
        return ResponseEntity.ok(Map.of("likes", count));
    }

    // GET /api/likes/{postId}/liked - Check if current user liked a post
    @GetMapping("/{postId}/liked")
    public ResponseEntity<?> hasLiked(@PathVariable Long postId,
                                      @RequestHeader("Authorization") String authHeader) {
        User user = getCurrentUser(authHeader);
        Post post = postService.getPostById(postId);
        boolean liked = likeService.hasLiked(user, post);
        return ResponseEntity.ok(Map.of("liked", liked));
    }
}
