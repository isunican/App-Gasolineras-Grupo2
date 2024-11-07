package es.unican.gasolineras.common;

import java.time.LocalDateTime;

public class TimeProvider {
    public LocalDateTime obtenerDiaHora() {
        return LocalDateTime.now();
    }
}

