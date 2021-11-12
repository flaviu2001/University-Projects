package com.ubb.en.ArcaneProgramming.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    private Date date;

    private Long price;

    @ManyToOne
    @JoinColumn(name = "userID")
    private ArcaneUser arcaneUser;

    @ManyToOne
    @JoinColumn(name = "gameID")
    private Game game;
}
