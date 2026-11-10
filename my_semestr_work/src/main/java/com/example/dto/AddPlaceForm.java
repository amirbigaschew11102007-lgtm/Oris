package com.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddPlaceForm {
    private String title;
    private Long photo_id;
    private String description;
    private String street_address;
}
