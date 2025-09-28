package com.paxtech.utime.platform.services.domain.model.commands;

public record DeleteServiceCommand(Long id) {

    public DeleteServiceCommand{
        if (id == null || id <= 0){
            throw new IllegalArgumentException("id must be greater than 0");
        }
    }
}
