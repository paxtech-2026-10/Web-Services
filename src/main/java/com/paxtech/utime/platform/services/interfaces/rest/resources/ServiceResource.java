package com.paxtech.utime.platform.services.interfaces.rest.resources;

public record ServiceResource(
        Long id,
        String name,
        Integer duration,
        Long price,
        Long providerId
) {
    /**
     * Crea un ServiceResource placeholder para Services que han sido eliminados
     * @param id El ID del Service eliminado
     * @param providerId El ID del Provider (se mantiene para referencia)
     * @return Un ServiceResource con valores por defecto indicando que fue eliminado
     */
    public static ServiceResource deleted(Long id, Long providerId) {
        return new ServiceResource(id, "Servicio eliminado", 0, 0L, providerId);
    }
}
