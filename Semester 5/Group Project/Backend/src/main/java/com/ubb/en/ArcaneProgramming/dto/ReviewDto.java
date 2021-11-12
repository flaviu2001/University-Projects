package com.ubb.en.ArcaneProgramming.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Long ID;
    private Integer numberOfStars;
    private String text;
    private ArcaneUserDto arcaneUserDto;
    private GameDto gameDto;
}
