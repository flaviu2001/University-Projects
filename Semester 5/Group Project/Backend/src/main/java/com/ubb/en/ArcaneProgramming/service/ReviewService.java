package com.ubb.en.ArcaneProgramming.service;

import com.ubb.en.ArcaneProgramming.model.Review;
import com.ubb.en.ArcaneProgramming.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;

    @Transactional
    public void addReview(Review review) {
        List<Review> reviewsSoFar = reviewRepository.getReviewsOfGameAndUsername(review.getGame().getTitle(), review.getArcaneUser().getUserName());
        if (reviewsSoFar.size() == 1) {
            Review reviewToChange = reviewsSoFar.get(0);
            reviewToChange.setText(review.getText());
            reviewToChange.setNumberOfStars(review.getNumberOfStars());
        } else reviewRepository.save(review);
    }

    public Review findReview(Long reviewID) {
        return reviewRepository.findById(reviewID).orElseThrow(() -> new RuntimeException("no review with this id"));
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public void deleteReview(Long reviewID) {
        reviewRepository.deleteById(reviewID);
    }

    public List<Review> getAllReviewsByTitle(String title) {
        return this.reviewRepository.getReviewsOfGame(title);
    }

    public List<Review> getAllReviewsByUsername(String username) {
        return this.reviewRepository.getReviewsOfUsername(username);
    }
}
