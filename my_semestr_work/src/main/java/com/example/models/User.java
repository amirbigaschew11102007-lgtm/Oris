package com.example.models;

import com.example.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "password")
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
