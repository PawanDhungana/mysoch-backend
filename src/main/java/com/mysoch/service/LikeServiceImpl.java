package com.mysoch.service;

import com.mysoch.model.Like;
import com.mysoch.model.Post;
import com.mysoch.model.User;
import com.mysoch.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    @Override
    public void likePost(User user, Post post) {
        if (!likeRepository.existsByUserAndPost(user, post)) {
            likeRepository.save(Like.builder()
                    .user(user)
                    .post(post)
                    .build());
        }
    }

    @Override
    public void unlikePost(User user, Post post) {
        likeRepository.findByUserAndPost(user, post)
                .ifPresent(likeRepository::delete);
    }

    @Override
    public boolean hasLiked(User user, Post post) {
        return likeRepository.existsByUserAndPost(user, post);
    }

    @Override
    public long getLikeCount(Post post) {
        return likeRepository.countByPost(post);
    }

    @Override
    public List<User> getUsersWhoLiked(Post post) {
        return likeRepository.findAllByPost(post).stream()
                .map(Like::getUser)
                .collect(Collectors.toList());
    }
}
