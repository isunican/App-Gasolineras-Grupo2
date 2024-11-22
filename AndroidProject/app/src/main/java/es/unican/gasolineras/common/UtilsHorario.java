package es.unican.gasolineras.common;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class UtilsHorario {

    public static final String SIN_DETALLES_DE_HORARIO = "Sin detalles de horario";
    public static final String TODO_EL_DÍA = "Todo el día";
    public static final String HH_MM = "HH:mm";

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
            return SIN_DETALLES_DE_HORARIO;
        }

        // Divide el horario en secciones
        String[] secciones = horario.split(";");

        for (String seccion : secciones) {
            // Divide cada sección en días y rango horario
            String[] partes = seccion.trim().split(": ");
            String dias = partes[0];
            String rango = partes[1];

            // Divide los días para manejar rangos como "L-X" y días individuales como "D"
            String[] diasSeparados = dias.split("-");

            // Verifica si el día actual está en el rango
            if (diasSeparados.length == 1) { // Ej. "D: 08:00-21:00" , 24H
                if (diasSeparados[0].equals(dia)) return rango;
            } else { // Ej. "L-X: 08:00-21:00"
                String inicio = diasSeparados[0];
                String fin = diasSeparados[1];
                if (diaEstaEnRango(dia, inicio, fin)) return rango;
            }
        }
        return TODO_EL_DÍA;
    }

    /**
     * Metodo que retorna el dia en el que estamos.
     *
     * @return el dia en el que estamos.
     */
    public static String obtenerDiaActual(DayOfWeek diaSemana) {
        switch (diaSemana) {
            case MONDAY:
                return "L";
            case TUESDAY:
                return "M";
            case WEDNESDAY:
                return "X";
            case THURSDAY:
                return "J";
            case FRIDAY:
                return "V";
            case SATURDAY:
                return "S";
            case SUNDAY:
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
        if (TODO_EL_DÍA.equals(horarios)) {
            return false;
        }
        if (SIN_DETALLES_DE_HORARIO.equals(horarios)) {
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
        if ((rango.equals(SIN_DETALLES_DE_HORARIO))&&(rango.equals(TODO_EL_DÍA)))
        {
            return false;
        }
        // Formateador para interpretar el formato HH:mm
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(HH_MM);

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

    /**
     * Metodo que nos retorna un horario con inicio 5 horas antes de la hora actual y final 5 horas despues de la hora actual de lunes a domingo.
     * @return horario con inicio 5 horas antes de la hora actual y final 5 horas despues de la hora actual de lunes a domingo.
     */
    public static String obtenerHorarioAbiertoSimple() {
        // Obtener la fecha y hora actual
        LocalDateTime ahora = LocalDateTime.now();

        // Calcular 5 horas antes y 5 horas después
        LocalDateTime cincoHorasAntes = ahora.minus(5, ChronoUnit.HOURS);
        LocalDateTime cincoHorasDespues = ahora.plus(5, ChronoUnit.HOURS);

        // Formatear las horas en el formato HH:mm
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern(HH_MM);
        String horaInicio = cincoHorasAntes.format(formatoHora);
        String horaFin = cincoHorasDespues.format(formatoHora);

        // Construir el resultado final
        return String.format("L-D: %s-%s",horaInicio, horaFin);
    }

    /**
     * Metodo que nos retorna un horario abierto con intervalos, el cual nos garantiza que siempre va a estar abierto a nuestra hora actual
     * @return horario abierto con intervalos, el cual nos garantiza que siempre va a estar abierto a nuestra hora actual
     */
    public static String obtenerHorarioAbiertoIntervalo() {
        // Obtener la fecha y hora actual
        LocalDateTime ahora = LocalDateTime.now();

        // Calcular intervalos
        LocalDateTime inicioIntervalo1 = ahora.minus(7, ChronoUnit.HOURS);
        LocalDateTime finIntervalo1 = ahora.plus(1, ChronoUnit.HOURS);

        LocalDateTime inicioIntervalo2 = ahora.plus(3, ChronoUnit.HOURS);
        LocalDateTime finIntervalo2 = ahora.plus(10, ChronoUnit.HOURS);

        // Formatear las horas en el formato HH:mm
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern(HH_MM);
        String horaInicio1 = inicioIntervalo1.format(formatoHora);
        String horaFin1 = finIntervalo1.format(formatoHora);

        String horaInicio2 = inicioIntervalo2.format(formatoHora);
        String horaFin2 = finIntervalo2.format(formatoHora);

        // Construir el resultado final
        return String.format("L-D: %s-%s y %s-%s",horaInicio1, horaFin1, horaInicio2, horaFin2);
    }

    /**
     * Metodo que nos retorna un horario cerrado todo el dia.
     * @return horario cerrado todo el dia.
     */
    public static String obtenerHorarioCerradoTodoElDia() {
        // Obtener la fecha actual y el día de la semana actual
        LocalDateTime ahora = LocalDateTime.now();
        int diaActual = ahora.getDayOfWeek().getValue();

        // Obtener una inicial que no sea la del día actual
        String dia = obtenerInicialDia(diaActual + 1); // Siguiente día en la semana (con wrap-around)


        // Construir el resultado final
        return String.format("%s: 09:00-21:00", dia);
    }

    /**
     * Metodo que nos retorna un horario cerrado con intervalos, el cual nos garantiza que siempre va a estar cerrado a nuestra hora actual
     * @return horario cerrado con intervalos, el cual nos garantiza que siempre va a estar cerrado a nuestra hora actual
     */
    public static String obtenerHorarioCerradoIntervalo() {
        // Obtener la fecha y hora actual
        LocalDateTime ahora = LocalDateTime.now();

        // Calcular intervalos
        LocalDateTime inicioIntervalo1 = ahora.minus(9, ChronoUnit.HOURS);
        LocalDateTime finIntervalo1 = ahora.minus(1, ChronoUnit.HOURS);

        LocalDateTime inicioIntervalo2 = ahora.plus(1, ChronoUnit.HOURS);
        LocalDateTime finIntervalo2 = ahora.plus(9, ChronoUnit.HOURS);

        // Formatear las horas en el formato HH:mm
        DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern(HH_MM);
        String horaInicio1 = inicioIntervalo1.format(formatoHora);
        String horaFin1 = finIntervalo1.format(formatoHora);

        String horaInicio2 = inicioIntervalo2.format(formatoHora);
        String horaFin2 = finIntervalo2.format(formatoHora);

        // Construir el resultado final
        return String.format("L-D: %s-%s y %s-%s",horaInicio1, horaFin1, horaInicio2, horaFin2);
    }

    /**
     * Metodo que nos retorna la inicial del dia de la semana.
     * @param diaSemana dia de la semana.
     * @return la inicial del dia de la semana.
     */
    private static String obtenerInicialDia(int diaSemana) {
        switch (diaSemana) {
            case 1: return "L"; // Lunes
            case 2: return "M"; // Martes
            case 3: return "X"; // Miércoles
            case 4: return "J"; // Jueves
            case 5: return "V"; // Viernes
            case 6: return "S"; // Sábado
            case 7: return "D"; // Domingo
            default: return ""; // Caso no válido
        }
    }
}
