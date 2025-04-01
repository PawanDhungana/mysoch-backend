package com.mysoch.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "likes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "post_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User who liked the post
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Post that was liked
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;
}
