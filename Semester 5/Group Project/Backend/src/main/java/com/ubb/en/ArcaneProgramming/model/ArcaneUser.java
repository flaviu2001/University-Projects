package com.ubb.en.ArcaneProgramming.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArcaneUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    private String email;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String userName;

    private byte[] hashedPassword;

    private byte[] salt;

    private Long verificationCode;

    private Boolean emailVerified;

    private String bio;
}
