package es.unican.gasolineras.activities.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static es.unican.gasolineras.common.UtilsHorario.gasolineraAbierta;
import static es.unican.gasolineras.common.UtilsHorario.procesaHorario;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;

import es.unican.gasolineras.activities.main.GasolinerasArrayAdapter;

public class UtilsHorarioTest {

    private GasolinerasArrayAdapter adapter;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void procesaHorarioTest() {
        assertEquals(procesaHorario("L-D: 08:00-21:00", "L"), "08:00-21:00");
        assertEquals(procesaHorario("L-D: 08:00-14:00 y 16:00-22:00", "L"), "08:00-14:00, 16:00-22:00");
        assertEquals(procesaHorario("M-V: 08:00-21:00", "L"), "Todo el día");
        assertEquals(procesaHorario("L-D: 24H", "L"), "24H");
        assertEquals(procesaHorario("", "L"), "Sin detalles de horario");
    }

    @Test
    public void gasolineraAbiertaTest(){
        assertEquals(gasolineraAbierta("08:00-14:00 y 16:00-20:00", LocalTime.of(10, 00)), true);
        assertEquals(gasolineraAbierta("08:00-14:00 y 16:00-20:00", LocalTime.of(15, 00)), false);
        assertEquals(gasolineraAbierta("08:00-14:00 y 16:00-20:00", LocalTime.of(8, 00)), true);
        assertEquals(gasolineraAbierta("08:00-14:00 y 16:00-20:00", LocalTime.of(14, 00,00,1)), false);
        assertEquals(gasolineraAbierta("24H", LocalTime.of(10, 00)), true);
        assertEquals(gasolineraAbierta("Todo el día", LocalTime.of(10, 00)), false);
        assertEquals(gasolineraAbierta("Sin detalles de horario", LocalTime.of(10, 00)), false);


    }
}
