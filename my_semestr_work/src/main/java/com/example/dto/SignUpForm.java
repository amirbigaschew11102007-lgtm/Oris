package com.example.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString(exclude = {"password","passwordRepeat"})
public class SignUpForm {
    private String username;
    private String email;
    private String password;
    private String passwordRepeat;
    private Long city_id;
}
