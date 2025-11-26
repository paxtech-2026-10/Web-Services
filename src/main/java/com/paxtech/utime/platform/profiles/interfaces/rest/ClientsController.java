package com.paxtech.utime.platform.profiles.interfaces.rest;

import com.paxtech.utime.platform.iam.domain.model.aggregates.User;
import com.paxtech.utime.platform.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.paxtech.utime.platform.profiles.domain.model.aggregates.Client;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetAllClientsQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetClientByIdQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetClientByUserIdQuery;
import com.paxtech.utime.platform.profiles.domain.services.ClientCommandService;
import com.paxtech.utime.platform.profiles.domain.services.ClientQueryService;
import com.paxtech.utime.platform.profiles.domain.services.ObjectStorageService;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.ClientResource;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.CreateClientResource;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.UpdateClientResource;
import com.paxtech.utime.platform.profiles.domain.model.commands.UpdateClientCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.DeleteClientCommand;
import com.paxtech.utime.platform.profiles.interfaces.rest.transform.ClientResourceFrontEntityAssembler;
import com.paxtech.utime.platform.profiles.interfaces.rest.transform.CreateClientCommandFromResourceAssembler;
import com.paxtech.utime.platform.profiles.interfaces.rest.transform.UpdateClientCommandFromResourceAssembler;
import org.springframework.http.HttpStatus;
import com.paxtech.utime.platform.shared.interfaces.rest.resources.MessageResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * ClientsController
 * 
 * This controller exposes REST endpoints to manage client resources.
 * It allows creating new clients, retrieving a client by ID, and listing all clients.
 */
@RestController
@RequestMapping(value = "/api/v1/clients", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Clients", description = "Endpoints for client management")
public class ClientsController {

    private static final Logger logger = LoggerFactory.getLogger(ClientsController.class);

    private final ClientCommandService clientCommandService;
    private final ClientQueryService clientsQueryService;
    private final UserRepository userRepository;
    private final ObjectStorageService objectStorageService;


    /**
     * Constructor
     * @param clientCommandService The service responsible for command operations (create client)
     * @param clientsQueryService The service responsible for query operations (retrieve clients)
     * @param userRepository The repository used to fetch User entities
     */
    public ClientsController(ClientCommandService clientCommandService, ClientQueryService clientsQueryService, UserRepository userRepository, ObjectStorageService objectStorageService) {
        this.clientCommandService = clientCommandService;
        this.clientsQueryService = clientsQueryService;
        this.userRepository = userRepository;
        this.objectStorageService = objectStorageService;
    }

    /**
     * Creates a new client associated with a given user ID
     * @param resource The {@link CreateClientResource} with client creation data
     * @return A {@link ClientResource} for the created client, or a bad request response if creation fails
     */
    @Operation(summary = "Create a new client", description = "Registers a new client in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PostMapping
    public ResponseEntity<ClientResource> createClient(@RequestBody CreateClientResource resource) {
        var command = CreateClientCommandFromResourceAssembler.toCommandFromResource(resource);

        // Look up the user by ID from the command
        Optional<User> userOptional = userRepository.findById(command.userId());

        // Return 400 Bad Request if user is not found
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Create the client using the command service
        Optional<Client> client = clientCommandService.handle(command, userOptional.get());

        // Return 201 Created if successful, otherwise 400 Bad Request
        return client
                .map(value -> new ResponseEntity<>(ClientResourceFrontEntityAssembler.toResourceFromEntity(value), CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * Retrieves a client by its ID
     * @param id The ID of the client
     * @return A {@link ClientResource} if found, or 404 Not Found if the client does not exist
     */
    @Operation(
            summary = "Get a client by ID",
            description = "Retrieve a client by its ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client found"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ClientResource> getClientByIdPath(@PathVariable Long id) {
        var result = clientsQueryService.handle(new GetClientByIdQuery(id));
        return result.map(client -> ResponseEntity.ok(ClientResourceFrontEntityAssembler.toResourceFromEntity(client)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Retrieves all clients
     * @return A list of {@link ClientResource}, or 404 Not Found if no clients exist
     */
    @Operation(summary = "Get all clients", description = "Retrieve all clients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clients retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Clients not found"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping
    public ResponseEntity<?> getClients() {
        return getAllClients();
    }

    /**
     * Internal helper method to get all clients from the service
     * @return A response entity containing the list of clients
     */
    private ResponseEntity<List<ClientResource>> getAllClients() {
        List<Client> clients = clientsQueryService.handle(new GetAllClientsQuery());
        if (clients.isEmpty()) return ResponseEntity.notFound().build();
        var resources = clients.stream()
                .map(ClientResourceFrontEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    /**
     * Obtiene el client ligado a un usuario específico
     * @param userId id del usuario autenticado
     * @return ClientResource si existe, 404 si no
     */
    @Operation(
            summary = "Get client by userId",
            description = "Retrieve the client associated to the given user id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client found"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<ClientResource> getClientByUserId(@PathVariable Long userId) {
        var result = clientsQueryService.handle(new GetClientByUserIdQuery(userId));
        return result
                .map(client -> ResponseEntity.ok(ClientResourceFrontEntityAssembler.toResourceFromEntity(client)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Update a client by ID
     * @param id The client ID
     * @param resource The {@link UpdateClientResource} with updated data
     * @return A {@link ClientResource} if updated, or 404 Not Found
     */
    @Operation(
            summary = "Update a client",
            description = "Update client information"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client updated successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestBody UpdateClientResource resource) {
        try {
            var command = UpdateClientCommandFromResourceAssembler.toCommandFromResource(id, resource);
            var updated = clientCommandService.handle(command);
            
            if (updated.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResource("Client not found"));
            }
            
            return ResponseEntity.ok(ClientResourceFrontEntityAssembler.toResourceFromEntity(updated.get()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResource(e.getMessage()));
        }
    }

    /**
     * Delete a client by ID
     * @param id The client ID
     * @return 204 No Content if successful, 404 Not Found if client doesn't exist
     */
    @Operation(
            summary = "Delete a client",
            description = "Delete client by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        try {
            clientCommandService.handle(new DeleteClientCommand(id));
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResource(e.getMessage()));
        }
    }

    @Operation(
            summary = "Upload client profile image",
            description = "Upload a profile image for a client. Accepts JPEG, PNG WEBP, GIF up to 5MB"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file or request"),
            @ApiResponse(responseCode = "404", description = "Client not found"),
            @ApiResponse(responseCode = "500", description = "Error uploading image")
    })
    @PostMapping(value = "/{id}/profile-image", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadProfileImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            var clientOpt = clientsQueryService.handle(new GetClientByIdQuery(id));
            if (clientOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResource("Client not found"));
            }
            var client = clientOpt.get();
            if (client.getProfileImageUrl() != null && !client.getProfileImageUrl().isEmpty()) {
                try {
                    objectStorageService.deleteProfileImage(client.getProfileImageUrl());
                } catch (Exception e) {
                    // Log el error pero no fallar - el archivo puede no existir
                    logger.warn("No se pudo eliminar la imagen anterior (puede que no exista): {}", e.getMessage());
                    // Continuar con la subida de la nueva imagen
                }
            }
            // Validar y subir la nueva imagen
            byte[] fileContent = file.getBytes();
            String contentType = file.getContentType();
            objectStorageService.validateImageFile(fileContent, contentType, file.getSize());

            String imageUrl = objectStorageService.uploadProfileImage(fileContent, contentType, id);

            // Actualizar el cliente con la nueva URL
            var updateCommand = new UpdateClientCommand(
                    id,
                    null,  // firstName sin cambios
                    null,  // lastName sin cambios
                    imageUrl  // nueva URL de imagen
            );

            var updated = clientCommandService.handle(updateCommand);

            if (updated.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResource("Error updating client"));
            }

            return ResponseEntity.ok(ClientResourceFrontEntityAssembler.toResourceFromEntity(updated.get()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResource(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResource("Error uploading image: " + e.getMessage()));
        }
    }
}
