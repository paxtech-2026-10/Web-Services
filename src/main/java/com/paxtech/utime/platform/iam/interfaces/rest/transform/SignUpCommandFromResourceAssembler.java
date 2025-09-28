package com.paxtech.utime.platform.iam.interfaces.rest.transform;

import com.paxtech.utime.platform.iam.domain.model.commands.SignUpCommand;
import com.paxtech.utime.platform.iam.interfaces.rest.resources.SignUpResource;

import java.util.*;

public class SignUpCommandFromResourceAssembler {
    public static SignUpCommand toCommandFromResource(SignUpResource resource) {
        return new SignUpCommand(resource.email(), resource.password());
    }
}