package com.ubb.en.ArcaneProgramming.repository;

import com.ubb.en.ArcaneProgramming.model.Wish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {
}
