package com.paxtech.utime.platform.reviews.application.internal.queryservices;

import com.paxtech.utime.platform.reviews.domain.model.aggregates.Review;
import com.paxtech.utime.platform.reviews.domain.model.commands.CreateReviewCommand;
import com.paxtech.utime.platform.reviews.domain.model.queries.GetAllReviewsQuery;
import com.paxtech.utime.platform.reviews.domain.model.queries.GetReviewByIdQuery;
import com.paxtech.utime.platform.reviews.domain.model.queries.GetReviewBySalonIdAndClientIdQuery;
import com.paxtech.utime.platform.reviews.infrastructure.persistence.jpa.repositories.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewQueryServiceImplTest {

    @Mock
    ReviewRepository reviewRepository;

    @InjectMocks
    ReviewQueryServiceImpl reviewQueryService;

    //   Verifica búsqueda de review por providerId y clientId
    @Test
    void getReviewBySalonIdAndClientIdTest() {
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

        GetReviewBySalonIdAndClientIdQuery query =
                new GetReviewBySalonIdAndClientIdQuery(10L, 1L);

        when(reviewRepository.findByProviderIdAndClientId(10L, 1L))
                .thenReturn(Optional.of(review));

        // Act
        Optional<Review> result = reviewQueryService.handle(query);

        // Assert
        assertThat(result).containsSame(review);
        verify(reviewRepository).findByProviderIdAndClientId(10L, 1L);
    }

    //   Verifica que se obtiene correctamente una review por ID.
    @Test
    void getReviewByIdTest() {
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

        GetReviewByIdQuery query = new GetReviewByIdQuery(7L);

        when(reviewRepository.findById(7L))
                .thenReturn(Optional.of(review));

        // Act
        Optional<Review> result = reviewQueryService.handle(query);

        // Assert
        assertThat(result).containsSame(review);
        verify(reviewRepository).findById(7L);
    }

    //   Verifica que se retornan todas las reviews registradas.
    @Test
    void getAllReviewsTest() {
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

        List<Review> expected = List.of(review);

        when(reviewRepository.findAll())
                .thenReturn(expected);

        // Act
        List<Review> result =
                reviewQueryService.handle(new GetAllReviewsQuery());

        // Assert
        assertThat(result).isSameAs(expected);
        verify(reviewRepository).findAll();
    }
}