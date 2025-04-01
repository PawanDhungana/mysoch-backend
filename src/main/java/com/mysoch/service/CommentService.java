package com.mysoch.service;

import com.mysoch.model.Comment;
import com.mysoch.model.Post;
import com.mysoch.model.User;

import java.util.List;

public interface CommentService {

    Comment addComment(String content, User author, Post post);

    List<Comment> getCommentsForPost(Post post);

    void deleteComment(Long commentId, User requester);

    Comment getCommentById(Long id);
}
