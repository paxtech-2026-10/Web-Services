package com.paxtech.utime.platform.reviews.domain.model.aggregates;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paxtech.utime.platform.reviews.domain.model.aggregates.Review;
import com.paxtech.utime.platform.reviews.domain.model.commands.CreateReviewCommand;
import com.paxtech.utime.platform.reviews.infrastructure.persistence.jpa.repositories.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class ReviewTest {
    //   Verifica que la entidad Review se construye de un CreateReviewCommand.
    @Test
    void createReviewFromCommandTest() {
        // Arrange
        CreateReviewCommand command = new CreateReviewCommand(
                1L,
                10L,
                5,
                "Excellent service",
                false
        );

        // Act
        Review review = new Review(command);

        // Assert
        assertThat(review.getClientId()).isEqualTo(1L);
        assertThat(review.getProviderId()).isEqualTo(10L);
        assertThat(review.getRating()).isEqualTo(5);
        assertThat(review.getReview()).isEqualTo("Excellent service");
        assertThat(review.isRead()).isFalse();
    }

    //   Verifica que la entidad Review puede modificar su estado con setters
    @Test
    void updateReviewFieldsTest() {
        // Arrange
        Review review = new Review(
                new CreateReviewCommand(
                        1L,
                        10L,
                        5,
                        "Excellent",
                        false
                )
        );

        // Act
        review.setReview("Updated review");
        review.setRating(4);
        review.setRead(true);

        // Assert
        assertThat(review.getReview()).isEqualTo("Updated review");
        assertThat(review.getRating()).isEqualTo(4);
        assertThat(review.isRead()).isTrue();
    }
}