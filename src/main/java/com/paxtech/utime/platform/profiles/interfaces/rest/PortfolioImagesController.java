package com.paxtech.utime.platform.profiles.interfaces.rest;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.PortfolioImage;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreatePortfolioImageCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreatePortfolioInProfileCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.DeletePortfolioImageCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.UpdatePortfolioImageCommand;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetPortfolioInProfilesByProviderProfileIdQuery;
import com.paxtech.utime.platform.profiles.domain.services.PortfolioImageCommandService;
import com.paxtech.utime.platform.profiles.domain.services.PortfolioInProfileCommandService;
import com.paxtech.utime.platform.profiles.domain.services.PortfolioInProfileQueryService;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.CreatePortfolioImageResource;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.PortfolioImageResource;
import com.paxtech.utime.platform.shared.interfaces.rest.resources.MessageResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/providerProfiles/{providerProfileId}/portfolio", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Portfolio Images", description = "Endpoints for managing portfolio images and their relations with provider profiles")
public class PortfolioImagesController {

    private final PortfolioImageCommandService portfolioImageCommandService;
    private final PortfolioInProfileCommandService portfolioInProfileCommandService;
    private final PortfolioInProfileQueryService portfolioInProfileQueryService;

    public PortfolioImagesController(PortfolioImageCommandService portfolioImageCommandService,
                                     PortfolioInProfileCommandService portfolioInProfileCommandService,
                                     PortfolioInProfileQueryService portfolioInProfileQueryService) {
        this.portfolioImageCommandService = portfolioImageCommandService;
        this.portfolioInProfileCommandService = portfolioInProfileCommandService;
        this.portfolioInProfileQueryService = portfolioInProfileQueryService;
    }

    @PostMapping
    @Operation(summary = "Crear una imagen de portafolio y asociarla al perfil")
    public ResponseEntity<PortfolioImageResource> createPortfolioImage(
            @PathVariable Long providerProfileId,
            @RequestBody CreatePortfolioImageResource resource) {

        // Crear la imagen
        var image = portfolioImageCommandService.handle(
                new CreatePortfolioImageCommand(resource.imageUrl())
        ).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al crear imagen"));

        // Asociar al perfil
        portfolioInProfileCommandService.handle(
                new CreatePortfolioInProfileCommand(image.getId(),providerProfileId)
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new PortfolioImageResource(image.getId(), image.getImageUrl()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una imagen del portafolio")
    @Transactional
    public ResponseEntity<?> deletePortfolioImage(@PathVariable Long id) {
        portfolioImageCommandService.handle(new DeletePortfolioImageCommand(id));
        return ResponseEntity.ok(new MessageResource("Portfolio image deleted successfully"));
    }

    @GetMapping
    @Operation(summary = "Obtener todas las imágenes del portafolio asociadas a un perfil")
    public ResponseEntity<List<PortfolioImageResource>> getPortfolioImages(@PathVariable Long providerProfileId) {
        var relations = portfolioInProfileQueryService.handle(new GetPortfolioInProfilesByProviderProfileIdQuery(providerProfileId));

        var result = relations.stream()
                .map(link -> new PortfolioImageResource(link.getPortfolio().getId(), link.getPortfolio().getImageUrl()))
                .toList();

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una imagen del portafolio")
    public ResponseEntity<PortfolioImageResource> updatePortfolioImage(
            @PathVariable Long providerProfileId,
            @PathVariable Long id,
            @RequestBody CreatePortfolioImageResource resource) {

        var command = new UpdatePortfolioImageCommand(id, resource.imageUrl());

        var updated = portfolioImageCommandService.handle(command);

        if (updated.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio image not found");
        }

        return ResponseEntity.ok(new PortfolioImageResource(
                updated.get().getId(),
                updated.get().getImageUrl()
        ));
    }

}
