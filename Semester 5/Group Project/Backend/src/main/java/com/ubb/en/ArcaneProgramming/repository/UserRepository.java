package com.ubb.en.ArcaneProgramming.repository;

import com.ubb.en.ArcaneProgramming.model.ArcaneUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<ArcaneUser, Long> {
    Optional<ArcaneUser> findByUserName(String userName);
}
