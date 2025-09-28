package com.paxtech.utime.platform.reviews.interfaces.rest;

import com.paxtech.utime.platform.reviews.domain.model.commands.DeleteReviewCommand;
import com.paxtech.utime.platform.reviews.domain.model.queries.GetAllReviewsQuery;
import com.paxtech.utime.platform.reviews.domain.model.queries.GetReviewByIdQuery;
import com.paxtech.utime.platform.reviews.domain.services.ReviewCommandService;
import com.paxtech.utime.platform.reviews.domain.services.ReviewQueryService;
import com.paxtech.utime.platform.reviews.interfaces.rest.resources.CreateReviewResource;
import com.paxtech.utime.platform.reviews.interfaces.rest.resources.ReviewResource;
import com.paxtech.utime.platform.reviews.interfaces.rest.transform.CreateReviewCommandFromResourceAssembler;
import com.paxtech.utime.platform.reviews.interfaces.rest.transform.ReviewResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/reviews", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Reviews", description = "Available Review Endpoints")
public class ReviewsController {
    private final ReviewCommandService reviewCommandService;
    private final ReviewQueryService reviewQueryService;


    /**
     * Constructor
     *
     * @param reviewCommandService The {@link ReviewCommandService} instance
     * @param reviewQueryService   The {@link ReviewQueryService} instance
     */
    public ReviewsController(ReviewCommandService reviewCommandService, ReviewQueryService reviewQueryService) {
        this.reviewCommandService = reviewCommandService;
        this.reviewQueryService = reviewQueryService;
    }

    /**
     * Create a new review
     *
     * @param resource The {@link CreateReviewResource} instance
     * @return The {@link ReviewResource} resource for the created review
     */
    @PostMapping
    @Operation(summary = "Create a new review", description = "Create a new review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Review created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Review not found")})
    public ResponseEntity<ReviewResource> createReview(@RequestBody CreateReviewResource resource) {
        var createReviewCommand = CreateReviewCommandFromResourceAssembler.toCommandFromResource(resource);
        var reviewId = reviewCommandService.handle(createReviewCommand);
        if (reviewId.isEmpty()) return ResponseEntity.badRequest().build();
        var reviewEntity = reviewId.get();
        var reviewResource = ReviewResourceFromEntityAssembler.toResourceFromEntity(reviewEntity);
        return new ResponseEntity<>(reviewResource, HttpStatus.CREATED);
    }

    /**
     * Get review by id
     *
     * @param reviewId The review id
     * @return The {@link ReviewResource} resource for the review
     */
    @GetMapping("/{reviewId}")
    @Operation(summary = "Get review by id", description = "Get review by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review found"),
            @ApiResponse(responseCode = "404", description = "Review not found")})
    public ResponseEntity<ReviewResource> getReviewById(@PathVariable Long reviewId) {
        var getReviewByIdQuery = new GetReviewByIdQuery(reviewId);
        var review = reviewQueryService.handle(getReviewByIdQuery);
        if (review.isEmpty()) return ResponseEntity.notFound().build();
        var reviewEntity = review.get();
        var reviewResource = ReviewResourceFromEntityAssembler.toResourceFromEntity(reviewEntity);
        return ResponseEntity.ok(reviewResource);
    }

    /**
     * Get all reviews
     *
     * @return The list of {@link ReviewResource} resources for all reviews
     */
    @GetMapping
    @Operation(summary = "Get all reviews", description = "Get all reviews")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reviews found"),
            @ApiResponse(responseCode = "404", description = "Reviews not found")})
    public ResponseEntity<List<ReviewResource>> getAllReviews() {
        var reviews = reviewQueryService.handle(new GetAllReviewsQuery());
        if (reviews.isEmpty()) return ResponseEntity.notFound().build();
        var reviewResources = reviews.stream()
                .map(ReviewResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(reviewResources);
    }

    /**
     * Delete review
     *
     * @param reviewId The review id
     * @return The message for the deleted review
     */
    @DeleteMapping("/{reviewId}")
    @Operation(summary = "Delete review", description = "Delete review")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review deleted"),
            @ApiResponse(responseCode = "404", description = "Review not found")})
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        var deleteReviewCommand = new DeleteReviewCommand(reviewId);
        reviewCommandService.handle(deleteReviewCommand);
        return ResponseEntity.ok("Review with given id successfully deleted");
    }
}
