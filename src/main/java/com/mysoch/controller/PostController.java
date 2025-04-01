package com.mysoch.controller;

import com.mysoch.model.Post;
import com.mysoch.model.User;
import com.mysoch.service.PostService;
import com.mysoch.service.UserService;
import com.mysoch.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    // Helper to extract user from token
    private User getUserFromToken(String token) {
        String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
        return userService.findByUsername(username).orElseThrow();
    }

    // POST /api/posts - Create new post
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Map<String, String> request,
                                           @RequestHeader("Authorization") String authHeader) {
        String content = request.get("content");

        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        User author = getUserFromToken(authHeader);
        Post post = postService.createPost(content, author);
        return ResponseEntity.ok(post);
    }

    // GET /api/posts - Get all posts (global feed)
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    // GET /api/posts/user/{username} - Get posts by specific user
    @GetMapping("/user/{username}")
    public ResponseEntity<List<Post>> getUserPosts(@PathVariable String username) {
        User user = userService.findByUsername(username).orElseThrow();
        return ResponseEntity.ok(postService.getPostsByUser(user));
    }

    // Get all posts authored by user
    @GetMapping("/feed")
    public ResponseEntity<?> getFollowingFeed(@RequestHeader("Authorization") String authHeader) {
        User user = getUserFromToken(authHeader);
        List<Post> feed = postService.getFeedForUser(user);
        return ResponseEntity.ok(feed);
    }
}
