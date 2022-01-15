package com.ubb.en.ArcaneProgramming.converter;

import com.ubb.en.ArcaneProgramming.dto.FriendInvitationDto;
import com.ubb.en.ArcaneProgramming.model.FriendInvitation;

public class FriendInvitationConverter {
    public static FriendInvitationDto convertToDto(FriendInvitation friendInvitation) {
        return new FriendInvitationDto(
                friendInvitation.getID(),
                ArcaneUserConverter.convertToDto(friendInvitation.getInviter()),
                ArcaneUserConverter.convertToDto(friendInvitation.getInvited()),
                friendInvitation.getStatus()
        );
    }

    public static FriendInvitation convertToModel(FriendInvitationDto friendInvitationDto) {
        return new FriendInvitation(
                0L,
                ArcaneUserConverter.convertToModel(friendInvitationDto.getInviterDto()),
                ArcaneUserConverter.convertToModel(friendInvitationDto.getInvitedDto()),
                friendInvitationDto.getStatus());
    }
}
