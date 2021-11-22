package com.ubb.en.ArcaneProgramming.converter;

import com.ubb.en.ArcaneProgramming.dto.ArcaneUserDto;
import com.ubb.en.ArcaneProgramming.model.ArcaneUser;

public class ArcaneUserConverter {
    public static ArcaneUserDto convertToDto(ArcaneUser user) {
        ArcaneUserDto arcaneUserDto = new ArcaneUserDto();
        arcaneUserDto.setUserName(user.getUserName());
        arcaneUserDto.setEmail(user.getEmail());
        arcaneUserDto.setFirstName(user.getFirstName());
        arcaneUserDto.setLastName(user.getLastName());
        arcaneUserDto.setBio(user.getBio());
        arcaneUserDto.setPassword(null);
        arcaneUserDto.setID(user.getID());
        if (user.getEmailVerified())
            arcaneUserDto.setEmailVerified("true");
        else
            arcaneUserDto.setEmailVerified("false");
        arcaneUserDto.setAvatarUrl(user.getAvatarUrl());
        return arcaneUserDto;
    }

    // Daniel, you left return null previously. Did you mean that this method would never get called or that you didn't know how to implement it?
    public static ArcaneUser convertToModel(ArcaneUserDto arcaneUserDto) {
        return new ArcaneUser(
                arcaneUserDto.getID(),
                arcaneUserDto.getEmail(),
                arcaneUserDto.getFirstName(),
                arcaneUserDto.getLastName(),
                arcaneUserDto.getUserName(),
                null,
                null,
                null,
                arcaneUserDto.getEmailVerified().equals("true"),
                arcaneUserDto.getBio(),
                arcaneUserDto.getAvatarUrl());
    }
}
