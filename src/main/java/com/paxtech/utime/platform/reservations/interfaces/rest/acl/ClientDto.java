package com.paxtech.utime.platform.reservations.interfaces.rest.acl;

public record ClientDto(Long id, String fullName) {
    public static ClientDto unknown(Long id) {
        return new ClientDto(id, "Cliente desconocido");
    }
}
