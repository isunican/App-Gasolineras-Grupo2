package es.unican.gasolineras.common;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class UtilsHorario {
    /**
     * Metodo que retorna el horario del dia de hoy de la gasolinera.
     *
     * @param horario horario de la gasolinera.
     *
     * @return el horario del dia de hoy que tiene la gasolinera o
     *         "Horario no disponible" si en la base de datos no aparece el horario.
     */
    public static String procesaHorario(String horario, String dia) {
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
    public static String obtenerDiaActual() {
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
    public static boolean diaEstaEnRango(String dia, String inicio, String fin) {
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
    public static boolean gasolineraAbierta(String horarios, LocalTime horaActual) {
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
    public static boolean horaEnRango(String rango, LocalTime horaActual) {
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
