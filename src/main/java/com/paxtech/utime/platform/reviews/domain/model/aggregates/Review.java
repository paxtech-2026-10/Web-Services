package com.paxtech.utime.platform.reviews.domain.model.aggregates;

import com.paxtech.utime.platform.reviews.domain.model.commands.CreateReviewCommand;
import com.paxtech.utime.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Review extends AuditableAbstractAggregateRoot<Review> {
    @Column(nullable = false)
    @Getter
    @Setter
    private Long clientId;
    @Setter
    @Column(nullable = false)
    @Getter
    private Long providerId;
    @Setter
    @Getter
    @Column(nullable = false)
    private String review;
    @Setter
    @Column(nullable = false)
    @Getter
    private int rating;

    @Getter
    @Column(nullable = false)
    @Setter
    private boolean isRead;

    public Review() {}

    public Review(CreateReviewCommand command){
        this.clientId = command.clientId();
        this.providerId = command.providerId();
        this.review = command.review();
        this.rating = command.rating();
        this.isRead = command.read();
    }


}
