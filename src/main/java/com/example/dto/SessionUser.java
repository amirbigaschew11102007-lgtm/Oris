package com.example.dto;

import com.example.enums.Role;
import com.example.models.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SessionUser {
    private Long id;
    private String username;
    private Role role;
    private Long cityId;
    private String cityName;

    public boolean isAdmin(){
        return role==Role.ADMIN;
    }

    public static SessionUser from(User user){
        return SessionUser.builder()
                .id(user.getId())
                .username(user.getName())
                .role(user.getRole())
                .cityId(user.getCity_id())
                .cityName(user.getCityName())
                .build();
    }
}
