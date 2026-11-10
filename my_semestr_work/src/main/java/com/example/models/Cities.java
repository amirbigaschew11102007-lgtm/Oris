package com.example.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class Cities {
    private Long id;
    private String name;
    private String country;
}
