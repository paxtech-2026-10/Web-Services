package com.paxtech.utime.platform.profiles.interfaces.rest;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Social;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreateSocialCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreateSocialInProfileCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.DeleteSocialCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.UpdateSocialCommand;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetSocialByIdQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetSocialInProfileByIdQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetSocialsInProfileByProviderProfileIdQuery;
import com.paxtech.utime.platform.profiles.domain.services.SocialCommandService;
import com.paxtech.utime.platform.profiles.domain.services.SocialInProfileCommandService;
import com.paxtech.utime.platform.profiles.domain.services.SocialInProfileQueryService;
import com.paxtech.utime.platform.profiles.domain.services.SocialQueryService;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.CreateSocialResource;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.SocialResource;
import com.paxtech.utime.platform.shared.interfaces.rest.resources.MessageResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/providerProfiles/{providerProfileId}/socials", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Socials", description = "Endpoints for managing socials and their relations with provider profiles")
public class SocialsController {
    private final SocialQueryService socialQueryService;
    private final SocialCommandService socialCommandService;
    private final SocialInProfileCommandService socialInProfileCommandService;
    private final SocialInProfileQueryService socialInProfileQueryService;

    public SocialsController(SocialQueryService socialQueryService, SocialCommandService socialCommandService, SocialInProfileCommandService socialInProfileCommandService, SocialInProfileQueryService socialInProfileQueryService) {
        this.socialQueryService = socialQueryService;
        this.socialCommandService = socialCommandService;
        this.socialInProfileCommandService = socialInProfileCommandService;
        this.socialInProfileQueryService = socialInProfileQueryService;
    }

    @PostMapping
    @Operation(summary = "Crear una nueva red social y asociarla a un perfil")
    public ResponseEntity<SocialResource> createSocialForProfile(
            @PathVariable Long providerProfileId,
            @RequestBody CreateSocialResource resource) {

        // 1. Crear el Social
        var social = socialCommandService.handle(
                new CreateSocialCommand(resource.socialUrl(), resource.socialIcon())
        ).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error al crear social"));

        // 2. Crear la relación
        socialInProfileCommandService.handle(
                new CreateSocialInProfileCommand(providerProfileId, social.getId())
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new SocialResource(social.getId(), social.getSocialIcon(), social.getSocialUrl())
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una red social y sus relaciones")
    @Transactional
    public ResponseEntity<?> deleteSocial(@PathVariable Long id) {
        var deleteSocialCommand = new DeleteSocialCommand(id);
        socialCommandService.handle(deleteSocialCommand);
        return ResponseEntity.ok(new MessageResource("Social deleted successfully"));
    }

    @GetMapping
    @Operation(summary = "Obtener todas las redes sociales asociadas a un perfil")
    public ResponseEntity<Iterable<SocialResource>> getSocialsForProfile(@PathVariable Long providerProfileId) {
        var socials = socialInProfileQueryService.handle(new GetSocialsInProfileByProviderProfileIdQuery(providerProfileId));
        if (socials.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.ok(socials.stream()
                    .map(link -> new SocialResource(link.getSocial().getId(), link.getSocial().getSocialIcon(), link.getSocial().getSocialUrl()))
                    .toList());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una red social existente")
    public ResponseEntity<SocialResource> updateSocial(
            @PathVariable Long providerProfileId,
            @PathVariable Long id,
            @RequestBody CreateSocialResource resource) {

        var command = new UpdateSocialCommand(
                id,
                resource.socialUrl(),
                resource.socialIcon()
        );

        var updated = socialCommandService.handle(command);

        if (updated.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Social not found");
        }

        return ResponseEntity.ok(new SocialResource(
                updated.get().getId(),
                updated.get().getSocialIcon(),
                updated.get().getSocialUrl()
        ));
    }


}
