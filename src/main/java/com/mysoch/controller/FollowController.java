package com.mysoch.controller;

import com.mysoch.model.User;
import com.mysoch.service.FollowService;
import com.mysoch.service.UserService;
import com.mysoch.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    // Helper to extract current user from JWT token
    private User getCurrentUser(String authHeader) {
        String username = jwtUtil.extractUsername(authHeader.replace("Bearer ", ""));
        return userService.findByUsername(username).orElseThrow();
    }

    // POST /api/follow/{username} - Follow a user
    @PostMapping("/{username}")
    public ResponseEntity<?> followUser(@PathVariable String username,
                                        @RequestHeader("Authorization") String authHeader) {
        User follower = getCurrentUser(authHeader);

        // Check if the user to follow exists
        return userService.findByUsername(username)
                .map(following -> {
                    if (follower.equals(following)) {
                        return ResponseEntity.badRequest().body(Map.of("message", "You cannot follow yourself."));
                    }

                    if (followService.isFollowing(follower, following)) {
                        return ResponseEntity.badRequest().body(Map.of("message", "You are already following this user."));
                    }

                    followService.followUser(follower, following);
                    return ResponseEntity.ok(Map.of("message", "Followed successfully."));
                })
                .orElse(ResponseEntity.badRequest().body(Map.of("message", "User to follow does not exist.")));
    }

    // DELETE /api/follow/{username} - Unfollow a user
    @DeleteMapping("/{username}")
    public ResponseEntity<?> unfollowUser(@PathVariable String username,
                                          @RequestHeader("Authorization") String authHeader) {
        User follower = getCurrentUser(authHeader);

        return userService.findByUsername(username)
                .map(following -> {
                    boolean wasFollowing = followService.isFollowing(follower, following);
                    followService.unfollowUser(follower, following);
                    if (wasFollowing) {
                        return ResponseEntity.ok(Map.of("message", "Unfollowed successfully."));
                    } else {
                        return ResponseEntity.ok(Map.of("message", "You were not following this user."));
                    }
                })
                .orElse(ResponseEntity.badRequest().body(Map.of("message", "User to unfollow does not exist.")));
    }

    // GET /api/follow/is-following/{username} - Check if current user is following another user
    @GetMapping("/is-following/{username}")
    public ResponseEntity<?> isFollowing(@PathVariable String username,
                                         @RequestHeader("Authorization") String authHeader) {
        User follower = getCurrentUser(authHeader);

        // If the username doesn't exist, return false instead of throwing
        return userService.findByUsername(username)
                .map(following -> ResponseEntity.ok(Map.of("following", followService.isFollowing(follower, following))))
                .orElse(ResponseEntity.ok(Map.of("following", false)));
    }

    // GET /api/follow/following - List of users current user follows
    @GetMapping("/following")
    public ResponseEntity<?> getFollowedUsers(@RequestHeader("Authorization") String authHeader) {
        User currentUser = getCurrentUser(authHeader);
        List<User> followings = followService.getFollowings(currentUser);

        // Optionally map to DTOs if needed; currently returns full user objects (with @JsonIgnore on password)
        return ResponseEntity.ok(followings);
    }

    // GET /api/follow/followers - List of users who follow the current user
    @GetMapping("/followers")
    public ResponseEntity<?> getFollowers(@RequestHeader("Authorization") String authHeader) {
        User currentUser = getCurrentUser(authHeader);
        List<User> followers = followService.getFollowers(currentUser);
        return ResponseEntity.ok(followers);
    }
}
