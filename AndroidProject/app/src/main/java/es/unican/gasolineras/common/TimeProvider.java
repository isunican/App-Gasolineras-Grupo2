package es.unican.gasolineras.common;

import java.time.LocalDateTime;

public class TimeProvider {
    /**
     * Metodo que devuelve la hora actual.
     * @return la hora actual.
     */
    public LocalDateTime obtenerDiaHora()
    {
        return LocalDateTime.now();
    }
}
