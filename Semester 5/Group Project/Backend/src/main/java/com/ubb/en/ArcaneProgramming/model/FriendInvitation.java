package com.ubb.en.ArcaneProgramming.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendInvitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @ManyToOne
    private ArcaneUser inviter;

    @ManyToOne
    private ArcaneUser invited;

    @Enumerated(EnumType.STRING)
    private FriendInvitationStatus status;
}
