package com.ubb.en.ArcaneProgramming.controller;

import com.ubb.en.ArcaneProgramming.converter.ReviewConverter;
import com.ubb.en.ArcaneProgramming.dto.ReviewDto;
import com.ubb.en.ArcaneProgramming.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/review")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/")
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        return new ResponseEntity<>(reviewService.getAllReviews().stream().map(ReviewConverter::convertToDto).collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id) {
        return new ResponseEntity<>(ReviewConverter.convertToDto(reviewService.findReview(id)), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<HttpStatus> addReview(@RequestBody ReviewDto reviewDto) {
        reviewService.addReview(ReviewConverter.convertToModel(reviewDto));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<HttpStatus> updateReview(@RequestBody ReviewDto reviewDto) {
        reviewService.updateReview(ReviewConverter.convertToModel(reviewDto));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
