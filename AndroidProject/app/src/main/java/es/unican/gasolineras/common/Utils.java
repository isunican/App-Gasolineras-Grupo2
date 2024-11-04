package es.unican.gasolineras.common;

import static es.unican.gasolineras.repository.GasolinerasService.deserializer;

import android.content.Context;

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
import es.unican.gasolineras.model.GasolinerasResponse;

/**
 * Utility methods that may be used by several classes
 */
public class Utils {
    private static boolean pruebas = false;

    private static LocalDateTime fechaPruebas = LocalDateTime.of(2024, 11, 4, 18, 0, 0);;

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
     * Obtiene la fecha actual
     * @return fecha actual
     */
    public static LocalDateTime obtenerFechaActual()
    {
        if (pruebas)
            return fechaPruebas;
        return LocalDateTime.now();
    }

    /**
     * Obtiene la hora actual
     * @param pruebas indica si se está en modo pruebas
     */
    public static void setPruebas(boolean pruebas)
    {
        Utils.pruebas = pruebas;
    }

    /**
     * Obtiene la hora actual
     * @return hora actual
     */
    public static void setHoraActual(LocalDateTime fecha)
    {
        fechaPruebas = fecha;
    }
}
