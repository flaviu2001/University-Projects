package com.ubb.en.ArcaneProgramming.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    private Integer numberOfStars;

    private String text;

    @ManyToOne
    @JoinColumn(name = "userID")
    private ArcaneUser arcaneUser;

    @ManyToOne
    @JoinColumn(name = "gameID")
    private Game game;
}
