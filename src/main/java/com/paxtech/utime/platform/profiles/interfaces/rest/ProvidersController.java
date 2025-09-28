package com.paxtech.utime.platform.profiles.interfaces.rest;

import com.paxtech.utime.platform.iam.domain.model.aggregates.User;
import com.paxtech.utime.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.paxtech.utime.platform.profiles.domain.model.aggregates.Provider;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreateProviderProfileCommand;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetAllProvidersQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetProviderByIdQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetProviderByUserIdQuery;
import com.paxtech.utime.platform.profiles.domain.services.ProviderCommandService;
import com.paxtech.utime.platform.profiles.domain.services.ProviderProfileCommandService;
import com.paxtech.utime.platform.profiles.domain.services.ProviderQueryService;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.CreateProviderResource;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.ProviderResource;
import com.paxtech.utime.platform.profiles.interfaces.rest.transform.CreateProviderCommandFromResourceAssembler;
import com.paxtech.utime.platform.profiles.interfaces.rest.transform.ProviderResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * ProvidersController
 *
 * REST controller that manages operations related to providers,
 * such as creating, retrieving by ID, and listing all providers.
 */
@RestController
@RequestMapping(value = "/api/v1/providers", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Providers", description = "Endpoints for provider management")
public class ProvidersController {

    private final ProviderCommandService providerCommandService;
    private final ProviderQueryService providerQueryService;
    private final UserRepository userRepository;
    private final ProviderProfileCommandService providerProfileCommandService;

    /**
     * Constructor
     * @param providerCommandService Service for handling provider commands (e.g., create)
     * @param providerQueryService Service for querying provider data
     * @param userRepository Repository to fetch User entities
     */
    public ProvidersController(ProviderCommandService providerCommandService, ProviderQueryService providerQueryService, UserRepository userRepository, ProviderProfileCommandService providerProfileCommandService) {
        this.providerCommandService = providerCommandService;
        this.providerQueryService = providerQueryService;
        this.userRepository = userRepository;
        this.providerProfileCommandService = providerProfileCommandService;
    }

    /**
     * Create a new provider
     * @param resource The {@link CreateProviderResource} containing the data to create the provider
     * @return A {@link ProviderResource} if created successfully, or 400 Bad Request if creation fails
     */
    @Operation(summary = "Create a new provider", description = "Registers a new provider in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Provider created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public ResponseEntity<ProviderResource> createProvider(@RequestBody CreateProviderResource resource) {
        var command = CreateProviderCommandFromResourceAssembler.toCommandFromResource(resource);

        // Look up the user by ID
        Optional<User> userOptional = userRepository.findById(command.userId());

        // Return 400 Bad Request if user is not found
        if (userOptional.isEmpty()) return ResponseEntity.badRequest().build();

        // Attempt to create provider with found user
        Optional<Provider> provider = providerCommandService.handle(command, userOptional.get());

        //Create ProviderProfile
        var createdProfileCommand = new CreateProviderProfileCommand("to Choose", "to Choose",provider.get().getId());
        providerProfileCommandService.handle(createdProfileCommand);
        // Return 201 Created if successful, otherwise 400 Bad Request
        return provider
                .map(value -> new ResponseEntity<>(ProviderResourceFromEntityAssembler.toResourceFromEntity(value), CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Retrieve a provider by ID
     * @param id The provider ID
     * @return A {@link ProviderResource} if found, or 404 Not Found if not
     */
    @Operation(
            summary = "Get a provider by ID",
            description = "Retrieve a provider by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Provider found"),
            @ApiResponse(responseCode = "404", description = "Provider not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProviderResource> getProviderByIdPath(@PathVariable Long id) {
        var result = providerQueryService.handle(new GetProviderByIdQuery(id));
        return result.map(provider -> ResponseEntity.ok(ProviderResourceFromEntityAssembler.toResourceFromEntity(provider)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Internal helper method to retrieve a provider by ID
     * (not exposed as an endpoint)
     * @param id The provider ID
     * @return A {@link ProviderResource} if found, or 404 Not Found
     */
    private ResponseEntity<ProviderResource> getProviderById(Long id) {
        Optional<Provider> provider = providerQueryService.handle(new GetProviderByIdQuery(id));
        return provider.map(value -> ResponseEntity.ok(ProviderResourceFromEntityAssembler.toResourceFromEntity(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all providers
     * @return A list of {@link ProviderResource}, or 404 Not Found if none exist
     */
    @Operation(
            summary = "Get all providers",
            description = "Gets all providers in endpoint"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Providers found"),
            @ApiResponse(responseCode = "404", description = "Providers not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping
    public ResponseEntity<?> AllProviders() {
        return getAllProviders();
    }

    /**
     * Internal helper method to get all providers
     * @return A response with a list of {@link ProviderResource} or 404 if empty
     */
    private ResponseEntity<List<ProviderResource>> getAllProviders() {
        List<Provider> providers = providerQueryService.handle(new GetAllProvidersQuery());
        if (providers.isEmpty()) return ResponseEntity.notFound().build();
        var resources = providers.stream()
                .map(ProviderResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ProviderResource> getProviderByUserId(@PathVariable Long userId) {
        Optional<Provider> providerOptional = providerQueryService.handle(new GetProviderByUserIdQuery(userId));
        return providerOptional
                .map(provider -> ResponseEntity.ok(ProviderResourceFromEntityAssembler.toResourceFromEntity(provider)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
