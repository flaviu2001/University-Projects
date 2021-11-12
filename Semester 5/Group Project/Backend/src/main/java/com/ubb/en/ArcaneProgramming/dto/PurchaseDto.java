package com.ubb.en.ArcaneProgramming.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDto {
    private Long ID;
    private Date date;
    private Long price;
    private ArcaneUserDto arcaneUserDto;
    private GameDto gameDto;
}