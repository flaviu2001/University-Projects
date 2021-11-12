package com.ubb.en.ArcaneProgramming.service;

import com.ubb.en.ArcaneProgramming.model.Review;
import com.ubb.en.ArcaneProgramming.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }

    public Review findReview(Long reviewID) {
        return reviewRepository.findById(reviewID).orElseThrow(() -> new RuntimeException("no review with this id"));
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review updateReview(Review review) {
        return reviewRepository.save(review);
    }

    public void deleteReview(Long reviewID) {
        reviewRepository.deleteById(reviewID);
    }
}
