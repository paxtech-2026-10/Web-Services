package com.paxtech.utime.platform.reviews.application.internal.commandservices;


import com.paxtech.utime.platform.reviews.domain.model.aggregates.Review;
import com.paxtech.utime.platform.reviews.domain.model.commands.CreateReviewCommand;
import com.paxtech.utime.platform.reviews.domain.model.commands.DeleteReviewCommand;
import com.paxtech.utime.platform.reviews.infrastructure.persistence.jpa.repositories.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewCommandServiceImplTest {

    @Mock
    ReviewRepository reviewRepository;

    @InjectMocks
    ReviewCommandServiceImpl reviewCommandService;

    //   Verifica que se puede crear una review
    @Test
    void createReviewFunctioningTest() {
        // Arrange
        CreateReviewCommand command = new CreateReviewCommand(
                1L,
                10L,
                5,
                "Excellent service",
                false
        );

        when(reviewRepository.findByProviderIdAndClientId(10L, 1L))
                .thenReturn(Optional.empty());

        // Act
        Optional<Review> result = reviewCommandService.handle(command);

        // Assert
        assertThat(result).isPresent();

        ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
        verify(reviewRepository).save(captor.capture());

        Review saved = captor.getValue();

        assertThat(saved.getClientId()).isEqualTo(1L);
        assertThat(saved.getProviderId()).isEqualTo(10L);
        assertThat(saved.getRating()).isEqualTo(5);
        assertThat(saved.getReview()).isEqualTo("Excellent service");
        assertThat(saved.isRead()).isFalse();
    }

    //   Verifica que no se puede crear una review duplicada
    @Test
    void createDuplicateReviewTest() {
        // Arrange
        CreateReviewCommand command = new CreateReviewCommand(
                1L,
                10L,
                5,
                "Excellent service",
                false
        );

        Review existing = new Review(command);

        when(reviewRepository.findByProviderIdAndClientId(10L, 1L))
                .thenReturn(Optional.of(existing));

        // Act & Assert
        assertThatThrownBy(() -> reviewCommandService.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");

        verify(reviewRepository, never()).save(any());
    }

    //   Verifica que si la review existe, se elimina correctamente
    @Test
    void deleteReviewOkTest() {
        // Arrange
        DeleteReviewCommand command = new DeleteReviewCommand(5L);

        when(reviewRepository.existsById(5L))
                .thenReturn(true);

        // Act
        reviewCommandService.handle(command);

        // Assert
        verify(reviewRepository).deleteById(5L);
    }

    //   Verifica que si la review no existe no se elimina nada
    @Test
    void deleteReviewNotFoundTest() {
        // Arrange
        DeleteReviewCommand command = new DeleteReviewCommand(5L);

        when(reviewRepository.existsById(5L))
                .thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> reviewCommandService.handle(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("does not exist");

        verify(reviewRepository, never()).deleteById(any());
    }

    // Verifica que no se puede crear un CreateReviewCommand con rating inválido
    @Test
    void createReview_invalidRatingTest() {
        assertThatThrownBy(() ->
                new CreateReviewCommand(
                        1L,
                        10L,
                        0,
                        "Bad service",
                        false
                )
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Rating must be between 1 and 5");
    }
}