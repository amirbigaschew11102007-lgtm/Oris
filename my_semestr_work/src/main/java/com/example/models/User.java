package com.example.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Long city_id;
    private String cityName;
    private Role role;
    private String avatarPath;
}
