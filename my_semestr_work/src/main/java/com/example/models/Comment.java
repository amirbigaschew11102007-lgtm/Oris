package com.example.models;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Comment {
    private Long id;
    private Long photo_id;
    private Long user_id;
    private String content;
    private String attachment_path;
    private Long recomment_id;
    private Timestamp createdAt;
}
