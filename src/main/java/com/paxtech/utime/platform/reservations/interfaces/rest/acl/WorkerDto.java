package com.paxtech.utime.platform.reservations.interfaces.rest.acl;

public record WorkerDto(Long id, String name, String specialization) {
    /**
     * Crea un WorkerDto placeholder para Workers que han sido eliminados
     * @param id El ID del Worker eliminado
     * @return Un WorkerDto con valores por defecto indicando que fue eliminado
     */
    public static WorkerDto deleted(Long id) {
        return new WorkerDto(id, "Worker eliminado", "N/A");
    }
}
