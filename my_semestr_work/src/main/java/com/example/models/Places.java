package com.example.models;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Places {
    private Long id;
    private String title;
    private String description;
    private String streetAddress;
    private Long authorId;
    private Long cityId;
    private Long categoryId;
    private Timestamp createdAt;
}
