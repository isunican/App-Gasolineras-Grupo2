package es.unican.gasolineras.common;

import static es.unican.gasolineras.common.Utils.rellenaListaCombustibles;
import static es.unican.gasolineras.model.TipoCombustible.BIODIESEL;
import static es.unican.gasolineras.model.TipoCombustible.BIOETANOL;
import static es.unican.gasolineras.model.TipoCombustible.GASOLEO_A;
import static es.unican.gasolineras.model.TipoCombustible.GASOLEO_B;
import static es.unican.gasolineras.model.TipoCombustible.GASOLEO_PREMIUM;
import static es.unican.gasolineras.model.TipoCombustible.GASOLINA_95_E10;
import static es.unican.gasolineras.model.TipoCombustible.GASOLINA_95_E5;
import static es.unican.gasolineras.model.TipoCombustible.GASOLINA_95_E5_PREMIUM;
import static es.unican.gasolineras.model.TipoCombustible.GASOLINA_98_E10;
import static es.unican.gasolineras.model.TipoCombustible.GASOLINA_98_E5;
import static es.unican.gasolineras.model.TipoCombustible.GLP;
import static es.unican.gasolineras.model.TipoCombustible.GNC;
import static es.unican.gasolineras.model.TipoCombustible.GNL;
import static es.unican.gasolineras.model.TipoCombustible.HIDROGENO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import es.unican.gasolineras.model.Gasolinera;
import es.unican.gasolineras.model.GasolineraCombustible;

public class UtilsTest {

    private Gasolinera gasolinera1;
    private Gasolinera gasolinera2;
    private Gasolinera gasolinera3;

    private List<GasolineraCombustible> listaCombustiblesParaRellenar = new ArrayList<>();

    private List<GasolineraCombustible>  listaCompleta = new ArrayList<>();
    private List<GasolineraCombustible>  listaParcial = new ArrayList<>();
    private List<GasolineraCombustible>  listaVacia = new ArrayList<>();


    @Before
    public void inicializa() {

        //Datos para el primer caso.
        gasolinera1 = new Gasolinera();

        gasolinera1.setBiodiesel(1.1);
        gasolinera1.setBioetanol(1.2);
        gasolinera1.setGnc(1.3);
        gasolinera1.setGnl(1.4);
        gasolinera1.setGlp(1.5);
        gasolinera1.setGasoleoA(1.6);
        gasolinera1.setGasoleoB(1.7);
        gasolinera1.setGasoleoPremium(1.8);
        gasolinera1.setGasolina95E10(1.9);
        gasolinera1.setGasolina95E5(2.0);
        gasolinera1.setGasolina95E5Premium(2.1);
        gasolinera1.setGasolina98E10(2.2);
        gasolinera1.setGasolina98E5(2.3);
        gasolinera1.setHidrogeno(2.4);

        listaCompleta.add(new GasolineraCombustible(BIODIESEL, 1.1));
        listaCompleta.add(new GasolineraCombustible(BIOETANOL, 1.2));
        listaCompleta.add(new GasolineraCombustible(GNC, 1.3));
        listaCompleta.add(new GasolineraCombustible(GNL, 1.4));
        listaCompleta.add(new GasolineraCombustible(GLP, 1.5));
        listaCompleta.add(new GasolineraCombustible(GASOLEO_A, 1.6));
        listaCompleta.add(new GasolineraCombustible(GASOLEO_B, 1.7));
        listaCompleta.add(new GasolineraCombustible(GASOLEO_PREMIUM, 1.8));
        listaCompleta.add(new GasolineraCombustible(GASOLINA_95_E10, 1.9));
        listaCompleta.add(new GasolineraCombustible(GASOLINA_95_E5, 2.0));
        listaCompleta.add(new GasolineraCombustible(GASOLINA_95_E5_PREMIUM, 2.1));
        listaCompleta.add(new GasolineraCombustible(GASOLINA_98_E10, 2.2));
        listaCompleta.add(new GasolineraCombustible(GASOLINA_98_E5, 2.3));
        listaCompleta.add(new GasolineraCombustible(HIDROGENO, 2.4));

        //Datos para el segundo caso.
        gasolinera2 = new Gasolinera();

        gasolinera2.setBiodiesel(1.1);
        gasolinera2.setGnc(1.3);
        gasolinera2.setGasoleoA(1.6);
        gasolinera2.setGasoleoB(1.7);
        gasolinera2.setGasoleoPremium(1.8);
        gasolinera2.setHidrogeno(2.4);

        listaParcial.add(new GasolineraCombustible(BIODIESEL, 1.1));
        listaParcial.add(new GasolineraCombustible(GNC, 1.3));
        listaParcial.add(new GasolineraCombustible(GASOLEO_A, 1.6));
        listaParcial.add(new GasolineraCombustible(GASOLEO_B, 1.7));
        listaParcial.add(new GasolineraCombustible(GASOLEO_PREMIUM, 1.8));
        listaParcial.add(new GasolineraCombustible(HIDROGENO, 2.4));

        //Datos para el tercer caso
        gasolinera3 = new Gasolinera();

    }

    @Test
    public void rellenaListaCombusitblesTest() {

        //Caso con gasolinera con todos los precios posibles.

        //Relleno la lista con los datos de la gasolinera
        listaCombustiblesParaRellenar = rellenaListaCombustibles(gasolinera1);

        //Compruebo que las dos listas tienen el mismo tamaño
        Assert.assertEquals("Caso lleno: Las listas no tienen el mismo tamaño",
                listaCompleta.size(),
                listaCombustiblesParaRellenar.size());

        //Compruebo que la lista tiene todos los elementos
        Assert.assertEquals("La lista no contiene todos los combustibles.",
                14,
                listaCombustiblesParaRellenar.size());

        // Verificar elementos uno por uno
        for (int i = 0; i < listaCompleta.size(); i++) {
            Assert.assertEquals(
                    "Error en el elemento en la posición " + i,
                    listaCompleta.get(i),
                    listaCombustiblesParaRellenar.get(i)
            );
        }

        //Caso con gasolinera con algunos precios.

        //Relleno la lista con los datos de la gasolinera
        listaCombustiblesParaRellenar = rellenaListaCombustibles(gasolinera2);

        Assert.assertEquals("Caso parcial: Las listas no tienen el mismo tamaño",
                listaParcial.size(),
                listaCombustiblesParaRellenar.size());

        //Compruebo que la lista tiene todos los elementos
        Assert.assertEquals("La lista no contiene todos los combustibles.",
                6,
                listaCombustiblesParaRellenar.size());

        // Verificar elementos uno por uno
        for (int i = 0; i < listaParcial.size(); i++) {
            Assert.assertEquals(
                    "Error en el elemento en la posición " + i,
                    listaParcial.get(i),
                    listaCombustiblesParaRellenar.get(i)
            );
        }

        //Caso con gasolinera sin precios.

        //Relleno la lista con los datos de la gasolinera
        listaCombustiblesParaRellenar = rellenaListaCombustibles(gasolinera3);

        Assert.assertEquals("Caso vacio: Las listas no tienen el mismo tamaño",
                listaVacia.size(),
                listaCombustiblesParaRellenar.size());

        //Compruebo que la lista tiene todos los elementos
        Assert.assertEquals("La lista contiene algun combustible.",
                0,
                listaCombustiblesParaRellenar.size());

        // Verificar elementos uno por uno
        for (int i = 0; i < listaVacia.size(); i++) {
            Assert.assertEquals(
                    "Error en el elemento en la posición " + i,
                    listaVacia.get(i),
                    listaCombustiblesParaRellenar.get(i)
            );
        }


    }
}
