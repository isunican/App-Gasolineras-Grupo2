package es.unican.gasolineras.model;

import static java.sql.Types.NULL;

import org.w3c.dom.NamedNodeMap;

public enum TipoCombustible {
    BIODIESEL,
    BIOETANOL,
    GNC,
    GNL,
    GLP,
    GASOLEO_A,
    GASOLEO_B,
    GASOLEO_PREMIUM,
    GASOLINA_95_E10,
    GASOLINA_95_E5,
    GASOLINA_95_E5_PREMIUM,
    GASOLINA_98_E10,
    GASOLINA_98_E5,
    HIDROGENO;

    public double getPrecio(Gasolinera gasolinera) {
        switch (this) {
            case BIODIESEL: return gasolinera.getBiodiesel();
            case BIOETANOL: return gasolinera.getBioetanol();
            case GNC: return gasolinera.getGnc();
            case GNL: return gasolinera.getGnl();
            case GLP: return gasolinera.getGlp();
            case GASOLEO_A: return gasolinera.getGasoleoA();
            case GASOLEO_B: return gasolinera.getGasoleoB();
            case GASOLEO_PREMIUM: return gasolinera.getGasolina95E5();
            case GASOLINA_95_E10: return gasolinera.getGasolina95E10();
            case GASOLINA_95_E5: return gasolinera.getGasolina95E5();
            case GASOLINA_95_E5_PREMIUM: return gasolinera.getGasolina95E5Premium();
            case GASOLINA_98_E10: return gasolinera.getGasolina98E10();
            case GASOLINA_98_E5: return gasolinera.getGasolina98E5();
            case HIDROGENO: return gasolinera.getHidrogeno();
        }
        return 0;
    }
}


