package com.mysoch.service;

import com.mysoch.model.Comment;
import com.mysoch.model.User;

public interface CommentLikeService {

    void likeComment(User user, Comment comment);

    void unlikeComment(User user, Comment comment);

    boolean hasLiked(User user, Comment comment);

    long getLikeCount(Comment comment);
}
