package com.paxtech.utime.platform.reviews.application.internal.commandservices;

import com.paxtech.utime.platform.reviews.domain.model.aggregates.Review;
import com.paxtech.utime.platform.reviews.domain.model.commands.CreateReviewCommand;
import com.paxtech.utime.platform.reviews.domain.model.commands.DeleteReviewCommand;
import com.paxtech.utime.platform.reviews.domain.services.ReviewCommandService;
import com.paxtech.utime.platform.reviews.infrastructure.persistence.jpa.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewCommandServiceImpl implements ReviewCommandService {
    private final ReviewRepository reviewRepository;

    public ReviewCommandServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public Optional<Review> handle(CreateReviewCommand command){
        var alreadyExists = reviewRepository.findByProviderIdAndClientId(command.providerId(), command.clientId());
        if (alreadyExists.isPresent()) {
            throw new IllegalArgumentException("Review for this salon already exists");
        }
        var review = new Review(command);
        try{
            reviewRepository.save(review);
        } catch (Exception e) {
            throw new RuntimeException("Error saving review");
        }
        return Optional.of(review);
    }

    @Override
    public void handle(DeleteReviewCommand command){
        if(!reviewRepository.existsById(command.id())){
            throw new IllegalArgumentException("Review does not exist");
        }
        try{
            reviewRepository.deleteById(command.id());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error deleting review");
        }
    }
}
