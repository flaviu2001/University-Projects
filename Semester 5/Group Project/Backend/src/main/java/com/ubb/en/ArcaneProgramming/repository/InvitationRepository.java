package com.ubb.en.ArcaneProgramming.repository;

import com.ubb.en.ArcaneProgramming.model.FriendInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitationRepository extends JpaRepository<FriendInvitation, Long> {
    @Query("select f from FriendInvitation f where (f.invited.userName = :name or f.inviter.userName = :name) and f.status = 'ACCEPTED'")
    List<FriendInvitation> findThatHasInviterOrInvited(@Param("name")String name);

    @Query("select f from FriendInvitation f where f.invited.userName = :name and f.status = 'PENDING'")
    List<FriendInvitation> findPendingInvitationsOfUser(@Param("name") String name);

    @Query("select f from FriendInvitation f where f.invited.userName = :invited and f.inviter.userName = :inviter")
    List<FriendInvitation> findByInvitedAndInviter(@Param("invited") String invited, @Param("inviter") String inviter);

    @Query("select f from FriendInvitation f where f.invited.userName = :user or f.inviter.userName = :user")
    List<FriendInvitation> findByInvitedOrInviter(@Param("user") String user);
}
