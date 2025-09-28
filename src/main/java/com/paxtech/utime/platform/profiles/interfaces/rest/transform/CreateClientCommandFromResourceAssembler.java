    package com.paxtech.utime.platform.profiles.interfaces.rest.transform;

    import com.paxtech.utime.platform.profiles.domain.model.commands.CreateClientCommand;
    import com.paxtech.utime.platform.profiles.interfaces.rest.resources.CreateClientResource;

    public class CreateClientCommandFromResourceAssembler {
        public static CreateClientCommand toCommandFromResource(CreateClientResource resource) {
            return new CreateClientCommand(
                    resource.firstName(),
                    resource.lastName(),
                    resource.userId()
            );
        }
    }
