package com.ubb.en.ArcaneProgramming.dto;

import com.ubb.en.ArcaneProgramming.model.FriendInvitationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendInvitationDto {
    private Long id;
    private ArcaneUserDto inviterDto;
    private ArcaneUserDto invitedDto;
    private FriendInvitationStatus status;
}
