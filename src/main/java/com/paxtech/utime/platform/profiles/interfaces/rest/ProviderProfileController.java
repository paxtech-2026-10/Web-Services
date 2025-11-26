package com.paxtech.utime.platform.profiles.interfaces.rest;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Social;
import com.paxtech.utime.platform.profiles.domain.model.commands.*;
import com.paxtech.utime.platform.profiles.domain.model.queries.*;
import com.paxtech.utime.platform.profiles.domain.services.*;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.*;
import com.paxtech.utime.platform.profiles.interfaces.rest.transform.CreatePortfolioImageCommandFromResourceAssembler;
import com.paxtech.utime.platform.profiles.interfaces.rest.transform.CreateProviderProfileCommandFromResourceAssembler;
import com.paxtech.utime.platform.profiles.interfaces.rest.transform.CreateSocialCommandFromResourceAssembler;
import com.paxtech.utime.platform.shared.interfaces.rest.resources.MessageResource;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.SocialInProfileRepository;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.PortfolioInProfileRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/provider-profiles")
@Tag(name = "Provider Profiles", description = "Endpoints for managing provider profiles and their relations")
public class ProviderProfileController {

    private final ProviderProfileQueryService providerProfileQueryService;
    private final ProviderQueryService providerQueryService;
    private final SocialInProfileQueryService socialsInProfileQueryService;
    private final SocialQueryService socialQueryService;
    private final PortfolioInProfileQueryService portfolioInProfileQueryService;
    private final ProviderProfileCommandService providerProfileCommandService;
    private final SocialCommandService socialCommandService;
    private final PortfolioImageCommandService portfolioImageCommandService;
    private final SocialInProfileCommandService socialInProfileCommandService;
    private final PortfolioInProfileCommandService portfolioInProfileCommandService;
    private final ProviderCommandService providerCommandService;
    private final SocialInProfileRepository socialInProfileRepository;
    private final PortfolioInProfileRepository portfolioInProfileRepository;


    public ProviderProfileController(
            ProviderProfileQueryService providerProfileQueryService,
            ProviderQueryService providerQueryService,
            SocialInProfileQueryService socialsInProfileQueryService,
            SocialQueryService socialQueryService,
            PortfolioInProfileQueryService portfolioInProfileQueryService,
            SocialCommandService socialCommandService,
            ProviderProfileCommandService providerProfileCommandService,
            PortfolioImageCommandService portfolioCommandService, 
            SocialInProfileCommandService socialInProfileCommandService, 
            PortfolioInProfileCommandService portfolioInProfileCommandService, 
            ProviderCommandService providerCommandService,
            SocialInProfileRepository socialInProfileRepository,
            PortfolioInProfileRepository portfolioInProfileRepository) {
        this.providerProfileQueryService = providerProfileQueryService;
        this.providerQueryService = providerQueryService;
        this.socialsInProfileQueryService = socialsInProfileQueryService;
        this.socialQueryService = socialQueryService;
        this.portfolioInProfileQueryService = portfolioInProfileQueryService;
        this.providerProfileCommandService = providerProfileCommandService;
        this.socialCommandService = socialCommandService;
        this.portfolioImageCommandService = portfolioCommandService;
        this.socialInProfileCommandService = socialInProfileCommandService;
        this.portfolioInProfileCommandService = portfolioInProfileCommandService;
        this.providerCommandService = providerCommandService;
        this.socialInProfileRepository = socialInProfileRepository;
        this.portfolioInProfileRepository = portfolioInProfileRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFullProfile(@PathVariable Long id) {
        var profileOpt = providerProfileQueryService.handle(new GetProviderProfileByIdQuery(id));
        if (profileOpt.isEmpty())
            return ResponseEntity.status(404).body(new MessageResource("Profile not found"));

        var profile = profileOpt.get();

        var providerQuery = new GetProviderByIdQuery(profile.getProviderId());
        var providerOpt = providerQueryService.handle(providerQuery);
        if (providerOpt.isEmpty())
            return ResponseEntity.status(404).body(new MessageResource("Provider not found"));

        var provider = providerOpt.get();


        // Relaciones
        var socialLinks = socialsInProfileQueryService.handle(new GetAllSocialsInProfileByProviderProfileIdQuery(profile.getId()));
        Map<String, String> socialsMap = socialLinks.stream()
                .map(link -> socialQueryService.handle(new GetSocialByIdQuery(link.getSocial().getId())))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toMap(
                        Social::getSocialIcon,  // clave del mapa (icono)
                        Social::getSocialUrl,   // valor del mapa (URL)
                        (existing, replacement) -> existing // manejo de duplicados
                ));



        var portfolioLinks = portfolioInProfileQueryService.handle(new GetAllPortfolioInProfilesByProviderProfileIdQuery(profile.getId()));
        List<PortfolioImageResource> portfolioImages = portfolioLinks.stream()
                .map(link -> new PortfolioImageResource(link.getId(), link.getPortfolio().getImageUrl()))
                .toList();

        var resource = new ProfileResource(
                profile.getId(),
                provider.getId(),
                provider.getCompanyName(),
                profile.getLocation() != null ? profile.getLocation() : "",
                provider.getUser().getEmail(),
                profile.getProfileImageUrl(),
                profile.getCoverImageUrl(),
                socialsMap,
                portfolioImages
        );


        return ResponseEntity.ok(resource);
    }


    @PostMapping
    @Operation(summary = "Create a new profile", description = "Create a new profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Profile created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Profile not found")})
    public ResponseEntity<ProfileResource> createProfile(@RequestBody CreateFullProfileResource resource) {
        var providerProfileResource = new CreateProviderProfileResource(
                resource.profileImageUrl(),
                resource.coverImageUrl(),
                resource.location(),
                resource.providerId()
        );
        var createProviderProfileCommand =
                CreateProviderProfileCommandFromResourceAssembler.toCommandFromResource(providerProfileResource);
        var providerProfile = providerProfileCommandService.handle(createProviderProfileCommand);

        // Crear y vincular redes sociales
        if (resource.socials() != null && !resource.socials().isEmpty()) {
            resource.socials().forEach((socialIcon, socialUrl) -> {
                var socialResource = new CreateSocialResource(socialUrl, socialIcon);
                var createSocialCommand = CreateSocialCommandFromResourceAssembler.toCommandFromResource(socialResource);
                var social = socialCommandService.handle(createSocialCommand);
                if (social.isEmpty()) return;
                socialInProfileCommandService.handle(new CreateSocialInProfileCommand(
                        providerProfile.get().getId(), social.get().getId()
                ));
            });
        }

        // Crear y vincular portfolio images
        List<PortfolioImageResource> portfolioResources = new ArrayList<>();
        if (resource.portfolioImages() != null && !resource.portfolioImages().isEmpty()) {
            for (String imageUrl : resource.portfolioImages()) {
                var portfolioImageResource = new CreatePortfolioImageResource(imageUrl);
                var command = CreatePortfolioImageCommandFromResourceAssembler.toCommandFromResource(portfolioImageResource);
                var portfolioImage = portfolioImageCommandService.handle(command);
                if (portfolioImage.isEmpty()) continue;

                portfolioInProfileCommandService.handle(new CreatePortfolioInProfileCommand(
                        providerProfile.get().getId(), portfolioImage.get().getId()
                ));

                portfolioResources.add(new PortfolioImageResource(
                        portfolioImage.get().getId(),
                        portfolioImage.get().getImageUrl()
                ));
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ProfileResource(
                        providerProfile.get().getId(),
                        providerProfile.get().getProviderId(),
                        resource.companyName(),
                        resource.location(),
                        resource.email(),
                        providerProfile.get().getProfileImageUrl(),
                        providerProfile.get().getCoverImageUrl(),
                        resource.socials(),
                        portfolioResources
                )
        );
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a provider profile", description = "Delete a provider profile by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Profile deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<?> deleteProfile(@PathVariable Long id) {
        try {
            providerProfileCommandService.handle(new DeleteProviderProfileCommand(id));
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResource(e.getMessage()));
        }
    }

    /**
     * Retrieves every provider profile with its relations
     * @return 200 + array de ProfileResource o 404 si no hay perfiles
     */
    @Operation(
            summary = "Get all provider profiles",
            description = "Retrieve every provider profile with socials and portfolio images"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profiles retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No profiles found")
    })
    @GetMapping
    public ResponseEntity<List<ProfileResource>> getAllProfiles() {

        // 1. Traer todos los profiles
        var profiles = providerProfileQueryService.handle(new GetAllProviderProfilesQuery());
        if (profiles.isEmpty()) return ResponseEntity.notFound().build();

        // 2. Convertir cada uno a ProfileResource
        List<ProfileResource> resources = profiles.stream().map(profile -> {

            /* 2A. Provider */
            var provider = providerQueryService
                    .handle(new GetProviderByIdQuery(profile.getProviderId()))
                    .orElse(null);                       // debería existir

            /* 2B. Socials */
            var socialLinks = socialsInProfileQueryService
                    .handle(new GetAllSocialsInProfileByProviderProfileIdQuery(profile.getId()));

            Map<String, String> socialsMap = socialLinks.stream()
                    .map(link -> socialQueryService.handle(new GetSocialByIdQuery(link.getSocial().getId())))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toMap(
                            Social::getSocialIcon,
                            Social::getSocialUrl,
                            (a, b) -> a
                    ));

            /* 2C. Portfolio */
            var portfolioLinks = portfolioInProfileQueryService
                    .handle(new GetAllPortfolioInProfilesByProviderProfileIdQuery(profile.getId()));

            List<PortfolioImageResource> portfolioImages = portfolioLinks.stream()
                    .map(l -> new PortfolioImageResource(l.getId(), l.getPortfolio().getImageUrl()))
                    .toList();

            /* 2D. Construir recurso */
            return new ProfileResource(
                    profile.getId(),
                    profile.getProviderId(),
                    provider != null ? provider.getCompanyName() : null,
                    profile.getLocation() != null ? profile.getLocation() : "",
                    provider != null ? provider.getUser().getEmail() : null,
                    profile.getProfileImageUrl(),
                    profile.getCoverImageUrl(),
                    socialsMap,
                    portfolioImages
            );
        }).toList();

        // 3. Respuesta
        return ResponseEntity.ok(resources);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update full profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found"),
            @ApiResponse(responseCode = "400", description = "Invalid Input")
    })
    @Transactional
    public ResponseEntity<?> updateProfile(
            @PathVariable Long id,
            @RequestBody UpdateProviderProfileResource resource) {

        try {
            var profileOpt = providerProfileQueryService.handle(new GetProviderProfileByIdQuery(id));
            if (profileOpt.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResource("Profile not found"));
            }
            var profile = profileOpt.get();

            // 1. Actualizar ProviderProfile básico (solo campos del profile)
            var command = new UpdateProviderProfileCommand(
                    id,
                    resource.profileImageUrl(),
                    resource.coverImageUrl(),
                    resource.location(),
                    resource.companyName(),
                    resource.socials(),
                    resource.portfolioImages()
            );

            var updated = providerProfileCommandService.handle(command);

            if (updated.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResource("Profile not found"));
            }

            // 2. Actualizar Provider (companyName) si se proporciona
            if (resource.companyName() != null && !resource.companyName().isBlank()) {
                var providerOpt = providerQueryService.handle(
                        new GetProviderByIdQuery(profile.getProviderId())
                );
                if (providerOpt.isPresent()) {
                    var updateProviderCommand = new UpdateProviderCommand(
                            providerOpt.get().getId(),
                            resource.companyName()
                    );
                    providerCommandService.handle(updateProviderCommand);
                }
            }

            // 3. Actualizar redes sociales si se proporcionan
            if (resource.socials() != null) {
                // Obtener socials actuales
                var currentSocialLinks = socialsInProfileQueryService.handle(
                        new GetAllSocialsInProfileByProviderProfileIdQuery(id)
                );

                // IMPORTANTE: Eliminar primero las RELACIONES (SocialInProfile) antes de eliminar los Socials
                currentSocialLinks.forEach(link -> {
                    socialInProfileRepository.deleteById(link.getId());
                });

                // Luego eliminar los Socials (ahora ya no tienen referencias)
                currentSocialLinks.forEach(link -> {
                    socialCommandService.handle(new DeleteSocialCommand(link.getSocial().getId()));
                });

                // Crear nuevas redes sociales
                resource.socials().forEach((socialIcon, socialUrl) -> {
                    var socialResource = new CreateSocialResource(socialUrl, socialIcon);
                    var createSocialCommand = CreateSocialCommandFromResourceAssembler.toCommandFromResource(socialResource);
                    var social = socialCommandService.handle(createSocialCommand);
                    if (social.isPresent()) {
                        socialInProfileCommandService.handle(new CreateSocialInProfileCommand(
                                id, social.get().getId()
                        ));
                    }
                });
            }

            // 4. Actualizar portfolio images si se proporcionan
            if (resource.portfolioImages() != null) {
                // Obtener portfolio actual
                var currentPortfolioLinks = portfolioInProfileQueryService.handle(
                        new GetAllPortfolioInProfilesByProviderProfileIdQuery(id)
                );

                // IMPORTANTE: Eliminar primero las RELACIONES (PortfolioInProfile) antes de eliminar las imágenes
                currentPortfolioLinks.forEach(link -> {
                    portfolioInProfileRepository.deleteById(link.getId());
                });

                // Luego eliminar las imágenes del portfolio
                currentPortfolioLinks.forEach(link -> {
                    portfolioImageCommandService.handle(
                            new DeletePortfolioImageCommand(link.getPortfolio().getId())
                    );
                });

                // Crear nuevas imágenes
                resource.portfolioImages().forEach(imageUrl -> {
                    var portfolioImageResource = new CreatePortfolioImageResource(imageUrl);
                    var createCommand = CreatePortfolioImageCommandFromResourceAssembler.toCommandFromResource(portfolioImageResource);
                    var portfolioImage = portfolioImageCommandService.handle(createCommand);
                    if (portfolioImage.isPresent()) {
                        portfolioInProfileCommandService.handle(new CreatePortfolioInProfileCommand(
                                portfolioImage.get().getId(), id
                        ));
                    }
                });
            }

            // 5. Obtener el perfil actualizado completo para retornarlo
            var updatedProfileOpt = providerProfileQueryService.handle(new GetProviderProfileByIdQuery(id));
            if (updatedProfileOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResource("Profile not found after update"));
            }

            var updatedProfile = updatedProfileOpt.get();
            var providerOpt = providerQueryService.handle(new GetProviderByIdQuery(updatedProfile.getProviderId()));
            if (providerOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResource("Provider not found"));
            }

            var provider = providerOpt.get();

            // Obtener socials actualizados
            var updatedSocialLinks = socialsInProfileQueryService.handle(
                    new GetAllSocialsInProfileByProviderProfileIdQuery(id)
            );
            Map<String, String> socialsMap = updatedSocialLinks.stream()
                    .map(link -> socialQueryService.handle(new GetSocialByIdQuery(link.getSocial().getId())))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toMap(
                            Social::getSocialIcon,
                            Social::getSocialUrl,
                            (existing, replacement) -> existing
                    ));

            // Obtener portfolio actualizado
            var updatedPortfolioLinks = portfolioInProfileQueryService.handle(
                    new GetAllPortfolioInProfilesByProviderProfileIdQuery(id)
            );
            List<PortfolioImageResource> portfolioImages = updatedPortfolioLinks.stream()
                    .map(link -> new PortfolioImageResource(link.getId(), link.getPortfolio().getImageUrl()))
                    .toList();

            // Construir y retornar el recurso completo actualizado
            var responseResource = new ProfileResource(
                    updatedProfile.getId(),
                    provider.getId(),
                    provider.getCompanyName(),
                    updatedProfile.getLocation() != null ? updatedProfile.getLocation() : "",
                    provider.getUser().getEmail(),
                    updatedProfile.getProfileImageUrl(),
                    updatedProfile.getCoverImageUrl(),
                    socialsMap,
                    portfolioImages
            );

            return ResponseEntity.ok(responseResource);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResource(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResource("Error updating profile: " + e.getMessage()));
        }
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search provider profiles by company name",
            description = "Search provider profiles by company name (partial match, case-insensitive)"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profiles found"),
            @ApiResponse(responseCode = "404", description = "No profiles found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<?> searchProfilesByCompanyName(@RequestParam String companyName){
        if (companyName == null || companyName.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResource("Company name cannot be empty"));
        }

        try {
            var query = new GetProviderProfilesByCompanyNameQuery(
                    new com.paxtech.utime.platform.profiles.domain.model.valueobjects.CompanyName(companyName)
            );

            var profiles = providerProfileQueryService.handle(query);

            if (profiles.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new MessageResource("No profiles found with company name: " + companyName));
            }

            // Convertir a ProfileResource (similar a getAllProfiles)
            List<ProfileResource> resources = profiles.stream().map(profile -> {
                var provider = providerQueryService
                        .handle(new GetProviderByIdQuery(profile.getProviderId()))
                        .orElse(null);

                var socialLinks = socialsInProfileQueryService
                        .handle(new GetAllSocialsInProfileByProviderProfileIdQuery(profile.getId()));

                Map<String, String> socialsMap = socialLinks.stream()
                        .map(link -> socialQueryService.handle(new GetSocialByIdQuery(link.getSocial().getId())))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toMap(
                                Social::getSocialIcon,
                                Social::getSocialUrl,
                                (a, b) -> a
                        ));

                var portfolioLinks = portfolioInProfileQueryService
                        .handle(new GetAllPortfolioInProfilesByProviderProfileIdQuery(profile.getId()));

                List<PortfolioImageResource> portfolioImages = portfolioLinks.stream()
                        .map(l -> new PortfolioImageResource(l.getId(), l.getPortfolio().getImageUrl()))
                        .toList();

                return new ProfileResource(
                        profile.getId(),
                        profile.getProviderId(),
                        provider != null ? provider.getCompanyName() : null,
                        profile.getLocation() != null ? profile.getLocation() : "",
                        provider != null ? provider.getUser().getEmail() : null,
                        profile.getProfileImageUrl(),
                        profile.getCoverImageUrl(),
                        socialsMap,
                        portfolioImages
                );
            }).toList();

            return ResponseEntity.ok(resources);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResource("Error searching profiles: " + e.getMessage()));
        }
    }

}