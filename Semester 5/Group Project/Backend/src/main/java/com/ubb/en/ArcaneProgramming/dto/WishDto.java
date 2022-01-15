package com.ubb.en.ArcaneProgramming.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishDto {
    private Long ID;
    private String arcaneUserName;
    private Long gameID;
}
