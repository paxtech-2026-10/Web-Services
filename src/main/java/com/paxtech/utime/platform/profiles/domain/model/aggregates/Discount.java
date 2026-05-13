package com.paxtech.utime.platform.profiles.domain.model.aggregates;

import com.paxtech.utime.platform.profiles.domain.model.commands.CreateDiscountCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.UpdateDiscountCommand;
import com.paxtech.utime.platform.profiles.domain.model.valueobjects.DiscountType;
import com.paxtech.utime.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Getter
public class Discount extends AuditableAbstractAggregateRoot<Discount> {

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "subtitle", length = 300)
    private String subtitle;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type", nullable = false, length = 20)
    private DiscountType discountType;

    @Column(name = "discount_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_profile_id", nullable = false)
    private ProviderProfile providerProfile;

    protected Discount() {}

    public Discount(CreateDiscountCommand command, ProviderProfile providerProfile) {
        if (command.title() == null || command.title().isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (command.content() == null || command.content().isBlank()) {
            throw new IllegalArgumentException("Content cannot be null or empty");
        }
        if (providerProfile == null) {
            throw new IllegalArgumentException("ProviderProfile cannot be null");
        }
        if (command.discountType() == null) {
            throw new IllegalArgumentException("DiscountType cannot be null");
        }
        if (command.discountValue() == null || command.discountValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("DiscountValue must be greater than zero");
        }
        if (command.discountType() == DiscountType.PERCENTAGE && command.discountValue().compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new IllegalArgumentException("Percentage discount cannot exceed 100%");
        }
        
        this.title = command.title();
        this.subtitle = command.subtitle();
        this.content = command.content();
        this.discountType = command.discountType();
        this.discountValue = command.discountValue();
        this.providerProfile = providerProfile;
    }

    public void update(UpdateDiscountCommand command) {
        if (command.title() != null && !command.title().isBlank()) {
            this.title = command.title();
        }
        if (command.subtitle() != null) {
            this.subtitle = command.subtitle();
        }
        if (command.content() != null && !command.content().isBlank()) {
            this.content = command.content();
        }
        if (command.discountType() != null) {
            this.discountType = command.discountType();
        }
        if (command.discountValue() != null) {
            if (command.discountValue().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("DiscountValue must be greater than zero");
            }
            if (this.discountType == DiscountType.PERCENTAGE && command.discountValue().compareTo(BigDecimal.valueOf(100)) > 0) {
                throw new IllegalArgumentException("Percentage discount cannot exceed 100%");
            }
            this.discountValue = command.discountValue();
        }
    }

    /**
     * Calcula el monto del descuento basado en el precio original
     * @param originalPrice Precio original antes del descuento
     * @return Monto del descuento a aplicar
     */
    public BigDecimal calculateDiscountAmount(BigDecimal originalPrice) {
        if (originalPrice == null || originalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Original price must be greater than zero");
        }
        
        return switch (discountType) {
            case PERCENTAGE -> originalPrice.multiply(discountValue.divide(BigDecimal.valueOf(100), 4, java.math.RoundingMode.HALF_UP));
            case FIXED_AMOUNT -> discountValue.min(originalPrice); // No puede exceder el precio original
        };
    }

    /**
     * Calcula el precio final después de aplicar el descuento
     * @param originalPrice Precio original antes del descuento
     * @return Precio final después del descuento
     */
    public BigDecimal calculateFinalPrice(BigDecimal originalPrice) {
        BigDecimal discountAmount = calculateDiscountAmount(originalPrice);
        return originalPrice.subtract(discountAmount).max(BigDecimal.ZERO);
    }
}
