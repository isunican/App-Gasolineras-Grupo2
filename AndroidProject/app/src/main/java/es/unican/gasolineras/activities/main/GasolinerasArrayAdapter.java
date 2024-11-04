package es.unican.gasolineras.activities.main;

import static java.util.Collections.emptyList;

import static es.unican.gasolineras.common.Utils.obtenerFechaActual;

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
            boolean compruebaEstado = gasolineraAbierta(procesaHorario(gasolinera.getHorario(), obtenerDiaActual()), obtenerFechaActual().toLocalTime());
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

    /**
     * Metodo que retorna el horario del dia de hoy de la gasolinera.
     *
     * @param horario horario de la gasolinera.
     *
     * @return el horario del dia de hoy que tiene la gasolinera o
     *         "Horario no disponible" si en la base de datos no aparece el horario.
     */
    public String procesaHorario(String horario, String dia) {
        if (horario.equals("")) {
            return "Sin detalles de horario";
        }

        // Divide el horario en secciones
        String[] secciones = horario.split(";");

        for (String seccion : secciones) {
            // Divide cada sección en días y rango horario
            String[] partes = seccion.trim().split(": ");
            String dias = partes[0];
            String rango = partes[1];
            if (rango.equals("24H")) {
                return "24H";
            }

            // Divide los días para manejar rangos como "L-X" y días individuales como "D"
            String[] diasSeparados = dias.split("-");

            // Verifica si el día actual está en el rango
            if (diasSeparados.length == 1) { // Ej. "D: 08:00-21:00"
                if (diasSeparados[0].equals(dia)) return rango.replace(" y", ",");
            } else { // Ej. "L-X: 08:00-21:00"
                String inicio = diasSeparados[0];
                String fin = diasSeparados[1];
                if (diaEstaEnRango(dia, inicio, fin)) return rango.replace(" y", ",");
                else return "Todo el día";
            }
        }
        return "Sin detalles de horario";
    }

    /**
     * Metodo que retorna el dia en el que estamos.
     *
     * @return el dia en el que estamos.
     */
    private String obtenerDiaActual() {
        Calendar calendario = Calendar.getInstance();
        int diaSemana = calendario.get(Calendar.DAY_OF_WEEK);
        switch (diaSemana) {
            case Calendar.MONDAY:
                return "L";
            case Calendar.TUESDAY:
                return "M";
            case Calendar.WEDNESDAY:
                return "X";
            case Calendar.THURSDAY:
                return "J";
            case Calendar.FRIDAY:
                return "V";
            case Calendar.SATURDAY:
                return "S";
            case Calendar.SUNDAY:
                return "D";
            default:
                return "";
        }
    }

    // Verifica si un día está dentro del rango de días (Ej. "L-X")

    /**
     * Metodo que devuelve si nuestro dia actual esta o no dentro del rango de la gasolinera.
     *
     * @param dia dia de la semana en el que estamos.
     * @param inicio inicio del horario de la gosalinera.
     * @param fin fin del horario de la gasolinera.
     *
     * @return true si el dia de hoy esta dentro del rango de la gasolinera.
     *         false si el dia de hoy no esta dentro del rango de la gasolinera.
     */
    private boolean diaEstaEnRango(String dia, String inicio, String fin) {
        String[] diasSemana = {"L", "M", "X", "J", "V", "S", "D"};
        int indiceInicio = java.util.Arrays.asList(diasSemana).indexOf(inicio);
        int indiceFin = java.util.Arrays.asList(diasSemana).indexOf(fin);
        int indiceDia = java.util.Arrays.asList(diasSemana).indexOf(dia);

        if (indiceInicio <= indiceFin) {
            return indiceDia >= indiceInicio && indiceDia <= indiceFin;
        } else { // Caso especial para cuando el rango cruza el fin de semana (Ej. "V-D")
            return indiceDia >= indiceInicio || indiceDia <= indiceFin;
        }
    }

    /**
     * Metodo que nos indica si la gasolinera esta habierta o no.
     *
     * @param horarios horarios que tiene la gasolinera a lo largo de la semana.
     *
     * @return true si la gasolinera esta abierta o
     *         false si la gasolinera esta cerrada
     */
    public boolean gasolineraAbierta(String horarios, LocalTime horaActual) {
        if ("24H".equals(horarios)) {
            return true; // Siempre abierta
        }
        if ("Todo el día".equals(horarios)) {
            return false;
        }
        if ("Sin detalles de horario".equals(horarios)) {
            return false;
        }
        // Divide los días para manejar rangos como "L-X" y días individuales como "D"
        String[] diasSeparados = horarios.split("y");
        Boolean aux = false;

        for (String dia : diasSeparados) {
            if (!aux) {
                aux = horaEnRango(dia, horaActual);
            } else {
                break;
            }
        }
        return aux;
    }

    /**
     * Metodo que indica si una hora esta dentro de un rango o no.
     *
     * @param rango rango de horas a comprobar.
     *
     * @return true si el rango esta dentro o
     *         false si no esta dentro del rango.
     */
    private static boolean horaEnRango(String rango, LocalTime horaActual) {
        // Formateador para interpretar el formato HH:mm
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // Separar el rango en hora de inicio y hora de fin
        String[] horas = rango.split("-");
        LocalTime inicio = LocalTime.parse(horas[0].trim(), formatter);
        LocalTime fin = LocalTime.parse(horas[1].trim(), formatter);

        // Comprobar si la hora actual está en el rango
        if (inicio.isBefore(fin)) {
            return !horaActual.isBefore(inicio) && !horaActual.isAfter(fin);
        } else {
            // Si el rango cruza la medianoche
            return !horaActual.isBefore(inicio) || !horaActual.isAfter(fin);
        }
    }
}
