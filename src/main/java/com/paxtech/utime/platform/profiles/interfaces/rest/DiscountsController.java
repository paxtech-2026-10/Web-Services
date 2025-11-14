package com.paxtech.utime.platform.profiles.interfaces.rest;

import com.paxtech.utime.platform.profiles.domain.model.commands.CreateDiscountCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.DeleteDiscountCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.UpdateDiscountCommand;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetAllDiscountsByProviderProfileIdQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetAllDiscountsQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetDiscountByIdQuery;
import com.paxtech.utime.platform.profiles.domain.services.DiscountCommandService;
import com.paxtech.utime.platform.profiles.domain.services.DiscountQueryService;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.CreateDiscountResource;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.DiscountResource;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.UpdateDiscountResource;
import com.paxtech.utime.platform.shared.interfaces.rest.resources.MessageResource;
import com.paxtech.utime.platform.profiles.interfaces.rest.transform.CreateDiscountCommandFromResourceAssembler;
import com.paxtech.utime.platform.profiles.interfaces.rest.transform.DiscountResourceFromEntityAssembler;
import com.paxtech.utime.platform.profiles.interfaces.rest.transform.UpdateDiscountCommandFromResourceAssembler;
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
@RequestMapping(value = "/api/v1/discounts", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Discounts", description = "Endpoints for discount management")
public class DiscountsController {

    private final DiscountCommandService discountCommandService;
    private final DiscountQueryService discountQueryService;

    public DiscountsController(
            DiscountCommandService discountCommandService,
            DiscountQueryService discountQueryService) {
        this.discountCommandService = discountCommandService;
        this.discountQueryService = discountQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a new discount", description = "Create a new discount for a provider profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Discount created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Provider profile not found")
    })
    public ResponseEntity<?> createDiscount(@RequestBody CreateDiscountResource resource) {
        try {
            CreateDiscountCommand command = CreateDiscountCommandFromResourceAssembler.toCommandFromResource(resource);
            var result = discountCommandService.handle(command);
            
            if (result.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResource("Provider profile not found"));
            }
            
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(DiscountResourceFromEntityAssembler.toResourceFromEntity(result.get()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResource(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get discount by ID", description = "Retrieve a discount by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Discount found"),
            @ApiResponse(responseCode = "404", description = "Discount not found")
    })
    public ResponseEntity<DiscountResource> getDiscountById(@PathVariable Long id) {
        var result = discountQueryService.handle(new GetDiscountByIdQuery(id));
        return result.map(discount -> ResponseEntity.ok(DiscountResourceFromEntityAssembler.toResourceFromEntity(discount)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/provider-profile/{providerProfileId}")
    @Operation(summary = "Get all discounts by provider profile", description = "Retrieve all discounts for a specific provider profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Discounts retrieved successfully")
    })
    public ResponseEntity<List<DiscountResource>> getDiscountsByProviderProfile(@PathVariable Long providerProfileId) {
        var discounts = discountQueryService.handle(new GetAllDiscountsByProviderProfileIdQuery(providerProfileId));
        var resources = discounts.stream()
                .map(DiscountResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping
    @Operation(summary = "Get all discounts", description = "Retrieve all discounts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Discounts retrieved successfully")
    })
    public ResponseEntity<List<DiscountResource>> getAllDiscounts() {
        var discounts = discountQueryService.handle(new GetAllDiscountsQuery());
        var resources = discounts.stream()
                .map(DiscountResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a discount", description = "Update an existing discount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Discount updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Discount not found")
    })
    public ResponseEntity<?> updateDiscount(
            @PathVariable Long id,
            @RequestBody UpdateDiscountResource resource) {
        try {
            UpdateDiscountCommand command = UpdateDiscountCommandFromResourceAssembler.toCommandFromResource(id, resource);
            var result = discountCommandService.handle(command);
            
            if (result.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResource("Discount not found"));
            }
            
            return ResponseEntity.ok(DiscountResourceFromEntityAssembler.toResourceFromEntity(result.get()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResource(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a discount", description = "Delete a discount by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Discount deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Discount not found")
    })
    public ResponseEntity<?> deleteDiscount(@PathVariable Long id) {
        try {
            discountCommandService.handle(new DeleteDiscountCommand(id));
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResource(e.getMessage()));
        }
    }
}

