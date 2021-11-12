package com.ubb.en.ArcaneProgramming.converter;

import com.ubb.en.ArcaneProgramming.dto.ReviewDto;
import com.ubb.en.ArcaneProgramming.model.Review;

public class ReviewConverter {
    public static ReviewDto convertToDto(Review review) {
        return new ReviewDto(
                review.getID(),
                review.getNumberOfStars(),
                review.getText(),
                ArcaneUserConverter.convertToDto(review.getArcaneUser()),
                GameConverter.convertToDto(review.getGame())
        );
    }

    public static Review convertToModel(ReviewDto reviewDto) {
        return new Review(
                reviewDto.getID(),
                reviewDto.getNumberOfStars(),
                reviewDto.getText(),
                ArcaneUserConverter.convertToModel(reviewDto.getArcaneUserDto()),
                GameConverter.convertToModel(reviewDto.getGameDto())
        );
    }
}
