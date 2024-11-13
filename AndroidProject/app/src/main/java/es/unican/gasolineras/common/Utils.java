package es.unican.gasolineras.common;

import static es.unican.gasolineras.model.TipoCombustible.BIODIESEL;
import static es.unican.gasolineras.model.TipoCombustible.BIOETANOL;
import static es.unican.gasolineras.model.TipoCombustible.GASOLEO_A;
import static es.unican.gasolineras.model.TipoCombustible.GASOLEO_B;
import static es.unican.gasolineras.model.TipoCombustible.GASOLEO_PREMIUM;
import static es.unican.gasolineras.model.TipoCombustible.GASOLINA_95_E10;
import static es.unican.gasolineras.model.TipoCombustible.GASOLINA_95_E5;
import static es.unican.gasolineras.model.TipoCombustible.GASOLINA_95_E5_PREMIUM;
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

    public static List<GasolineraCombustible> rellenaListaCombustibles(List<GasolineraCombustible> combustibles, Gasolinera gasolinera) {
        combustibles.add(new GasolineraCombustible(BIODIESEL, gasolinera.getBiodiesel()));
        combustibles.add(new GasolineraCombustible(BIOETANOL, gasolinera.getBioetanol()));
        combustibles.add(new GasolineraCombustible(GNC, gasolinera.getGnc()));
        combustibles.add(new GasolineraCombustible(GNL, gasolinera.getGnl()));
        combustibles.add(new GasolineraCombustible(GLP, gasolinera.getGlp()));
        combustibles.add(new GasolineraCombustible(GASOLEO_A, gasolinera.getGasoleoA()));
        combustibles.add(new GasolineraCombustible(GASOLEO_B, gasolinera.getGasoleoB()));
        combustibles.add(new GasolineraCombustible(GASOLEO_PREMIUM, gasolinera.getGasoleoPremium()));
        combustibles.add(new GasolineraCombustible(GASOLINA_95_E10, gasolinera.getGasolina95E10()));
        combustibles.add(new GasolineraCombustible(GASOLINA_95_E5, gasolinera.getGasolina95E5()));
        combustibles.add(new GasolineraCombustible(GASOLINA_95_E5_PREMIUM, gasolinera.getGasolina95E5Premium()));
        combustibles.add(new GasolineraCombustible(GASOLINA_98_E5, gasolinera.getGasolina98E10()));
        combustibles.add(new GasolineraCombustible(GASOLINA_98_E5, gasolinera.getGasolina98E5()));
        combustibles.add(new GasolineraCombustible(HIDROGENO, gasolinera.getHidrogeno()));

        return combustibles;
    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int) px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Get padding
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();
            //setDynamicHeight(listView);
            return true;

        } else {
            return false;
        }
    }

    public static void ajustarAlturaListView(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return; // No hay adaptador, no se puede ajustar la altura
        }

        int alturaTotal = 0;
        int itemsCount = listAdapter.getCount();

        for (int i = 0; i < itemsCount; i++) {
            View item = listAdapter.getView(i, null, listView);
            item.measure(
                    View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.UNSPECIFIED
            );
            alturaTotal += item.getMeasuredHeight();
        }

        // Añadir la altura de los divisores entre elementos
        alturaTotal += listView.getDividerHeight() * (itemsCount - 1);

        // Ajustar la altura del ListView
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = alturaTotal;
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
