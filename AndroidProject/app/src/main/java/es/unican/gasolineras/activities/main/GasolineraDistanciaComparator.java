package es.unican.gasolineras.activities.main;

import java.util.Collections;
import java.util.Comparator;

import es.unican.gasolineras.model.Gasolinera;
import es.unican.gasolineras.model.PuntoInteres;

public class GasolineraDistanciaComparator implements Comparator<Gasolinera> {

    private PuntoInteres punto;

    public GasolineraDistanciaComparator(PuntoInteres punto){
        this.punto = punto;
    }

    @Override
    public int compare(Gasolinera g1, Gasolinera g2) {

        //Obtengo las distancias
        double distancia1 = harvesine(g1, punto);
        double distancia2 = harvesine(g2, punto);

        // si la primera gasolinera esta mas cerca retorno -
        // si la primera gasolinera esta mas lejos retorno +
        // si estan a la misma distancia retorno 0
        return Double.compare(distancia1, distancia2);
    }

    /**
     * Retorna la distancia real entre un punto de interes y una gasolinera.
     * @param g Gasolinera de referencia
     * @param p punto de interes de referencia
     * @return la distancia en km
     */
    public double harvesine(Gasolinera g, PuntoInteres p){

        //radio tierra
        double R = 6371;

        //calculo las distancias
        double dLat = Math.toRadians(p.latitud - g.getLatitud());
        double dLon = Math.toRadians(p.longitud - g.getLongitud());
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(g.getLatitud())) * Math.cos(Math.toRadians(p.latitud)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }




}
