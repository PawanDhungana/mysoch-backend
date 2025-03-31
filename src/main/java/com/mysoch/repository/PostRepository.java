package com.mysoch.repository;

import com.mysoch.model.Post;
import com.mysoch.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // Fetch posts created by a specific user
    List<Post> findByAuthor(User author);

    // Fetch posts ordered by creation time
    List<Post> findAllByOrderByCreatedAtDesc();

    //Fetch all posts authored by those users
    List<Post> findByAuthorInOrderByCreatedAtDesc(List<User> authors);
}
