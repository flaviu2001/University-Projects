package com.ubb.en.ArcaneProgramming.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArcaneUserDto {
    private Long ID;

    private String email;

    private String firstName;

    private String lastName;

    private String userName;

    private String bio;

    private String password;

    private String emailVerified;

    private String avatarUrl;
}
