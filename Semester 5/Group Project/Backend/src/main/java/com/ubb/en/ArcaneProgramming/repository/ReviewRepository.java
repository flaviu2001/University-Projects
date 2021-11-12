package com.ubb.en.ArcaneProgramming.repository;

import com.ubb.en.ArcaneProgramming.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
