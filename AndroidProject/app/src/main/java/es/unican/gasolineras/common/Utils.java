package es.unican.gasolineras.common;

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
import static es.unican.gasolineras.repository.GasolinerasService.deserializer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import es.unican.gasolineras.model.Gasolinera;
import es.unican.gasolineras.model.GasolineraCombustible;
import es.unican.gasolineras.model.GasolinerasResponse;

/**
 * Utility methods that may be used by several classes
 */
public class Utils {

    /**
     * Parses a list of gas stations from a json resource file.
     * The json must contain a serialized GasolinerasResponse object.
     * It uses GSON to parse the json file
     * @param context the application context
     * @param jsonId the resource id of the json file
     * @return list of gas stations parsed from the file
     */
    public static List<Gasolinera> parseGasolineras(Context context, int jsonId) {
        InputStream is = context.getResources().openRawResource(jsonId);
        Type typeToken = new TypeToken<GasolinerasResponse>() { }.getType();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        GasolinerasResponse response = new GsonBuilder()
                .registerTypeAdapter(double.class, deserializer)
                .create()
                .fromJson(reader, typeToken);
        List<Gasolinera> gasolineras = response.getGasolineras();
        return gasolineras;
    }

    /**
     * Rellena una lista de los combustibles disponibles y su precio de la gasolinera
     * @param gasolinera gasolionera de obtener los combustibles
     * @return
     */
    public static List<GasolineraCombustible> rellenaListaCombustibles(Gasolinera gasolinera) {

        List<GasolineraCombustible> combustibles = new ArrayList<>();

        if (gasolinera.getBiodiesel() != 0) {
            combustibles.add(new GasolineraCombustible(BIODIESEL, gasolinera.getBiodiesel()));
        }
        if (gasolinera.getBioetanol() != 0) {
            combustibles.add(new GasolineraCombustible(BIOETANOL, gasolinera.getBioetanol()));
        }
        if (gasolinera.getGnc() != 0) {
            combustibles.add(new GasolineraCombustible(GNC, gasolinera.getGnc()));
        }
        if (gasolinera.getGnl() != 0) {
            combustibles.add(new GasolineraCombustible(GNL, gasolinera.getGnl()));
        }
        if (gasolinera.getGlp() != 0) {
            combustibles.add(new GasolineraCombustible(GLP, gasolinera.getGlp()));
        }
        if (gasolinera.getGasoleoA() != 0) {
            combustibles.add(new GasolineraCombustible(GASOLEO_A, gasolinera.getGasoleoA()));
        }
        if (gasolinera.getGasoleoB() != 0) {
            combustibles.add(new GasolineraCombustible(GASOLEO_B, gasolinera.getGasoleoB()));
        }
        if (gasolinera.getGasoleoPremium() != 0) {
            combustibles.add(new GasolineraCombustible(GASOLEO_PREMIUM, gasolinera.getGasoleoPremium()));
        }
        if (gasolinera.getGasolina95E10() != 0) {
            combustibles.add(new GasolineraCombustible(GASOLINA_95_E10, gasolinera.getGasolina95E10()));
        }
        if (gasolinera.getGasolina95E5() != 0) {
            combustibles.add(new GasolineraCombustible(GASOLINA_95_E5, gasolinera.getGasolina95E5()));
        }
        if (gasolinera.getGasolina95E5Premium() != 0) {
            combustibles.add(new GasolineraCombustible(GASOLINA_95_E5_PREMIUM, gasolinera.getGasolina95E5Premium()));
        }
        if (gasolinera.getGasolina98E10() != 0) {
            combustibles.add(new GasolineraCombustible(GASOLINA_98_E10, gasolinera.getGasolina98E10()));
        }
        if (gasolinera.getGasolina98E5() != 0) {
            combustibles.add(new GasolineraCombustible(GASOLINA_98_E5, gasolinera.getGasolina98E5()));
        }
        if (gasolinera.getHidrogeno() != 0) {
            combustibles.add(new GasolineraCombustible(HIDROGENO, gasolinera.getHidrogeno()));
        }

        return combustibles;
    }

    /**
     * establece la altura a la listView segun cuantos elementos tenga en ella de modo que no se
     * pueda scrollear
     * @param listView
     */
    public static void setListViewHeightBasedOnItemCount(ListView listView) {
        // Altura de cada elemento en píxeles
        int itemHeight = (int) (64 * listView.getContext().getResources().getDisplayMetrics().density);

        // Altura total de la lista
        int totalHeight = itemHeight * listView.getAdapter().getCount();

        // Ajustar la altura de la ListView
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listView.getAdapter().getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


}
