package com.example.models;

import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class Photos {
    private Long id;
    private Long place_id;
    private Long author_id;
    private String original_file_path;
    private String storage_file_path;
    private LocalDateTime createdAt;
}
