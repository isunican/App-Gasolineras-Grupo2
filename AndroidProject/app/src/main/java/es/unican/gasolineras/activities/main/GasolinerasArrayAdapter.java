package es.unican.gasolineras.activities.main;

import static java.util.Collections.emptyList;

import static es.unican.gasolineras.common.UtilsHorario.gasolineraAbierta;
import static es.unican.gasolineras.common.UtilsHorario.obtenerDiaActual;
import static es.unican.gasolineras.common.UtilsHorario.procesaHorario;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

import es.unican.gasolineras.R;
import es.unican.gasolineras.model.Gasolinera;

/**
 * Adapter that renders the gas stations in each row of a ListView
 */
public class GasolinerasArrayAdapter extends BaseAdapter {

    /** The list of gas stations to render */
    private final List<Gasolinera> gasolineras;

    /** Context of the application */
    private final Context context;

    /** Constante para el color verde en el caso en el que la gasolinera este abierta. */
    private static final int VERDE = 0xFF4CAF50;

    /** Constante para el color rojo en el caso en el que la gasolinera este cerrada. */
    private static final int ROJO = 0xFFF44336;

    /**
     * Constructs an adapter to handle a list of gasolineras
     * @param context the application context
     * @param objects the list of gas stations
     */
    public GasolinerasArrayAdapter(@NonNull Context context, @NonNull List<Gasolinera> objects) {
        // we know the parameters are not null because of the @NonNull annotation
        this.gasolineras = objects;
        this.context = context;
    }

    @Override
    public int getCount() {
        return gasolineras.size();
    }

    @Override
    public Object getItem(int position) {
        return gasolineras.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("DiscouragedApi")  // to remove warnings about using getIdentifier
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Gasolinera gasolinera = (Gasolinera) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.activity_main_list_item, parent, false);
        }

        // logo
        {
            String rotulo = gasolinera.getRotulo().toLowerCase();

            int imageID = context.getResources()
                    .getIdentifier(rotulo, "drawable", context.getPackageName());

            // Si el rotulo son sólo numeros, el método getIdentifier simplemente devuelve
            // como imageID esos números, pero eso va a fallar porque no tendré ningún recurso
            // que coincida con esos números
            if (imageID == 0 || TextUtils.isDigitsOnly(rotulo)) {
                imageID = context.getResources()
                        .getIdentifier("generic", "drawable", context.getPackageName());
            }

            if (imageID != 0) {
                ImageView view = convertView.findViewById(R.id.ivLogo);
                view.setImageResource(imageID);
            }
        }

        // name
        {
            TextView tv = convertView.findViewById(R.id.tvName);
            tv.setText(gasolinera.getRotulo());
        }

        // address
        {
            TextView tv = convertView.findViewById(R.id.tvAddress);
            tv.setText(gasolinera.getDireccion());
        }

        // gasolina 95 price
        {
            TextView tvLabel = convertView.findViewById(R.id.tv95Label);
            String label = context.getResources().getString(R.string.gasolina95label);
            tvLabel.setText(String.format("%s:", label));

            TextView tv = convertView.findViewById(R.id.tv95);
            tv.setText(String.valueOf(gasolinera.getGasolina95E5()));
        }

        // diesel A price
        {
            TextView tvLabel = convertView.findViewById(R.id.tvDieselALabel);
            String label = context.getResources().getString(R.string.dieselAlabel);
            tvLabel.setText(String.format("%s:", label));

            TextView tv = convertView.findViewById(R.id.tvDieselA);
            tv.setText(String.valueOf(gasolinera.getGasoleoA()));
        }

        /**
         * Texto donde se muestra el estado de la gasolinera.
         * En verde si esta abierto.
         * en rojo si esta cerrado.
         */
        {
            TextView tv = convertView.findViewById(R.id.tvAbiertoCerrado);
            String estado;
            boolean compruebaEstado = gasolineraAbierta(procesaHorario(gasolinera.getHorario(), obtenerDiaActual()), LocalDateTime.now().toLocalTime());
            if (compruebaEstado) {
                estado = "Abierto";
                tv.setTextColor(VERDE);
            } else {
                estado = "Cerrado";
                tv.setTextColor(ROJO);
            }
            tv.setText(estado);
        }

        /**
         * Texto donde se muestra el horario del dia acutal de la gasolinera.
         */
        {
            TextView tv = convertView.findViewById(R.id.tvHorarioGasolinera);
            String textoHorario;
            if (gasolinera.getHorario() == null || gasolinera.getHorario().isEmpty()) {
                textoHorario = "Sin detalles de horario";
            } else {
                textoHorario = "(" + procesaHorario(gasolinera.getHorario(), obtenerDiaActual()) + ")";
            }
            tv.setText(textoHorario);
        }

        return convertView;
    }
}
