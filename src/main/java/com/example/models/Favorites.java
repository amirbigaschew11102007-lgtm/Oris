package com.example.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class Favorites {
    private Long user_id;
    private Long place_id;
}