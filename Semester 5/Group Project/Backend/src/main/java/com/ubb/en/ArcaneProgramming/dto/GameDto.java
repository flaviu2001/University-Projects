package com.ubb.en.ArcaneProgramming.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDto {
    private Long ID;

    private String title;

    private String description;

    private Long price;

    private String pictureUrl;
}
