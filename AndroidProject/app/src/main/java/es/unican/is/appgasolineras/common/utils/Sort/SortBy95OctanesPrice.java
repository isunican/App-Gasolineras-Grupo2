package es.unican.is.appgasolineras.common.utils.Sort;

import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import es.unican.is.appgasolineras.common.utils.PriceUtilities;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.model.Promocion;

import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;

public class SortBy95OctanesPrice implements Comparator<Gasolinera> {

    private IGasolinerasRepository gRep;
    private IPromocionesRepository pRep;
    private PriceUtilities utilities;

    public SortBy95OctanesPrice(IGasolinerasRepository gRep, IPromocionesRepository pRep) {
        this.gRep = gRep;
        this.pRep = pRep;

        utilities = new PriceUtilities();
    }
    @Override
    public int compare(Gasolinera gasolinera, Gasolinera other) {
        // Obtiene datos Gasolinera 1
        double gas1 = utilities.precioToDouble(gasolinera.getNormal95(), NumberFormat.getInstance(Locale.FRANCE));
        double gasDescontado1;
        List<Promocion> promociones1 = pRep.getPromocionesRelacionadasConGasolinera(gasolinera.getId());

        if (promociones1.isEmpty()) {
            gasDescontado1 = gas1;
        } else {
            Promocion best1 = gRep.bestPromotion(gas1, promociones1, "Gasolina");
            gasDescontado1 = utilities.calculateDiscountedPrice(gas1, best1);
        }

        // Obtiene datos Gasolinera 2
        double gas2 = utilities.precioToDouble(other.getNormal95(), NumberFormat.getInstance(Locale.FRANCE));
        double gasDescontado2;
        List<Promocion> promociones2 = pRep.getPromocionesRelacionadasConGasolinera(other.getId());

        if (promociones2.isEmpty()) {
            gasDescontado2 = gas2;
        } else {
            Promocion best2 = gRep.bestPromotion(gas2, promociones2, "Gasolina");
            gasDescontado2 = utilities.calculateDiscountedPrice(gas2, best2);
        }

        return Double.compare(gasDescontado1, gasDescontado2);
    }
}
