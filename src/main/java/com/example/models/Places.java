package com.example.models;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private String authorName;
    private String cityName;
    private String categoryName;
    private String previewPhotoPath;
    private LocalDateTime createdAt;
    private boolean favorite;

    @Builder.Default
    private List<Photos> photos = new ArrayList<>();
}
