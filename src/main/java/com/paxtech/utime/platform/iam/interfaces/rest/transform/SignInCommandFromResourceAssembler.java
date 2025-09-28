package com.paxtech.utime.platform.iam.interfaces.rest.transform;

import com.paxtech.utime.platform.iam.domain.model.commands.SignInCommand;
import com.paxtech.utime.platform.iam.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {
    public static SignInCommand toCommandFromResource(SignInResource signInResource) {
        return new SignInCommand(signInResource.email(), signInResource.password());
    }
}