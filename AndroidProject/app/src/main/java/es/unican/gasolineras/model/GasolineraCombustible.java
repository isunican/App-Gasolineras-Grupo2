package es.unican.gasolineras.model;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GasolineraCombustible {

    private TipoCombustible combustible;
    private double precio;

    public GasolineraCombustible(TipoCombustible combustible, double precio){
        this.combustible = combustible;
        this.precio = precio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true; // Mismo objeto
        if (o == null || getClass() != o.getClass())
            return false; // Verifica tipo
        GasolineraCombustible that = (GasolineraCombustible) o;
        // Compara contenido: tipo de combustible y precio
        return Double.compare(that.precio, precio) == 0 &&
                Objects.equals(combustible, that.combustible);
    }
}
