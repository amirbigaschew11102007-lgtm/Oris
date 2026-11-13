package com.example.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceForm {
    private String title;
    private String description;
    private String streetAddress;
    private Long cityId;
    private Long categoryId;
}