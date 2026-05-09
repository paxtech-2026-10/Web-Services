package com.paxtech.utime.platform.profiles.domain.model.aggregates;

import com.paxtech.utime.platform.profiles.domain.model.commands.CreateDiscountCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreateProviderProfileCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.UpdateDiscountCommand;
import com.paxtech.utime.platform.profiles.domain.model.valueobjects.DiscountType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class DiscountTest {

    private ProviderProfile providerProfile() {
        return new ProviderProfile(new CreateProviderProfileCommand(
                "p", "c", "loc", 1L, "d", LocalTime.NOON, LocalTime.MIDNIGHT));
    }

    private CreateDiscountCommand percentageCommand() {
        return new CreateDiscountCommand(
                "Summer Sale", "20% off", "Get 20% off all services",
                DiscountType.PERCENTAGE, new BigDecimal("20"), 1L);
    }

    private CreateDiscountCommand fixedCommand(BigDecimal value) {
        return new CreateDiscountCommand(
                "Fixed", "10 off", "Take 10 off",
                DiscountType.FIXED_AMOUNT, value, 1L);
    }

    @Test
    void shouldCreatePercentageDiscount() {
        Discount discount = new Discount(percentageCommand(), providerProfile());

        assertEquals("Summer Sale", discount.getTitle());
        assertEquals(DiscountType.PERCENTAGE, discount.getDiscountType());
        assertEquals(new BigDecimal("20"), discount.getDiscountValue());
    }

    @Test
    void shouldCalculatePercentageDiscountAmount() {
        Discount discount = new Discount(percentageCommand(), providerProfile());

        BigDecimal amount = discount.calculateDiscountAmount(new BigDecimal("100"));

        assertEquals(0, amount.compareTo(new BigDecimal("20")));
    }

    @Test
    void shouldCalculateFinalPriceAfterPercentage() {
        Discount discount = new Discount(percentageCommand(), providerProfile());

        BigDecimal finalPrice = discount.calculateFinalPrice(new BigDecimal("100"));

        assertEquals(0, finalPrice.compareTo(new BigDecimal("80")));
    }

    @Test
    void shouldCapFixedAmountAtOriginalPrice() {
        Discount discount = new Discount(fixedCommand(new BigDecimal("50")), providerProfile());

        BigDecimal amount = discount.calculateDiscountAmount(new BigDecimal("30"));

        assertEquals(0, amount.compareTo(new BigDecimal("30")));
        assertEquals(0, discount.calculateFinalPrice(new BigDecimal("30")).compareTo(BigDecimal.ZERO));
    }

    @Test
    void shouldRejectPercentageGreaterThan100() {
        ProviderProfile profile = providerProfile();

        assertThrows(IllegalArgumentException.class, () -> new Discount(
                new CreateDiscountCommand("t", "s", "c",
                        DiscountType.PERCENTAGE, new BigDecimal("150"), 1L),
                profile));
    }

    @Test
    void shouldRejectNonPositiveOriginalPrice() {
        Discount discount = new Discount(percentageCommand(), providerProfile());

        assertThrows(IllegalArgumentException.class,
                () -> discount.calculateDiscountAmount(BigDecimal.ZERO));
    }

    @Test
    void shouldUpdateFieldsThroughUpdateCommand() {
        Discount discount = new Discount(percentageCommand(), providerProfile());

        discount.update(new UpdateDiscountCommand(1L, "New Title", "New Sub", "New Content",
                DiscountType.PERCENTAGE, new BigDecimal("30")));

        assertEquals("New Title", discount.getTitle());
        assertEquals("New Sub", discount.getSubtitle());
        assertEquals("New Content", discount.getContent());
        assertEquals(0, discount.getDiscountValue().compareTo(new BigDecimal("30")));
    }

    @Test
    void updateShouldRejectPercentageOver100() {
        Discount discount = new Discount(percentageCommand(), providerProfile());

        assertThrows(IllegalArgumentException.class, () -> discount.update(
                new UpdateDiscountCommand(1L, null, null, null,
                        DiscountType.PERCENTAGE, new BigDecimal("120"))));
    }
}
