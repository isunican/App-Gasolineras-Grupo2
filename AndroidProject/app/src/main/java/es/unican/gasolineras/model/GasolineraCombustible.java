package es.unican.gasolineras.model;

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
}
