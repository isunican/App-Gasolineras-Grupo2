package es.unican.gasolineras.common;

import static es.unican.gasolineras.common.UtilsHorario.gasolineraAbierta;
import static es.unican.gasolineras.common.UtilsHorario.procesaHorario;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;

import es.unican.gasolineras.activities.main.GasolinerasArrayAdapter;

public class UtilsHorarioTest {

    private GasolinerasArrayAdapter adapter;


    @Test
    public void procesaHorarioTest() {
        Assert.assertEquals(procesaHorario("L-D: 08:00-21:00", "L"), "08:00-21:00");
        Assert.assertEquals(procesaHorario("L-D: 08:00-14:00 y 16:00-22:00", "L"), "08:00-14:00 y 16:00-22:00");
        Assert.assertEquals(procesaHorario("M-V: 08:00-21:00", "L"), "Todo el día");
        Assert.assertEquals(procesaHorario("L-D: 24H", "L"), "24H");
        Assert.assertEquals(procesaHorario("", "L"), "Sin detalles de horario");
    }

    @Test
    public void gasolineraAbiertaTest(){
        Assert.assertEquals(gasolineraAbierta("08:00-14:00 y 16:00-20:00", LocalTime.of(10, 00)), true);
        Assert.assertEquals(gasolineraAbierta("08:00-14:00 y 16:00-20:00", LocalTime.of(15, 00)), false);
        Assert.assertEquals(gasolineraAbierta("08:00-14:00 y 16:00-20:00", LocalTime.of(8, 00)), true);
        Assert.assertEquals(gasolineraAbierta("08:00-14:00 y 16:00-20:00", LocalTime.of(14, 00,00,1)), false);
        Assert.assertEquals(gasolineraAbierta("24H", LocalTime.of(10, 00)), true);
        Assert.assertEquals(gasolineraAbierta("Todo el día", LocalTime.of(10, 00)), false);
        Assert.assertEquals(gasolineraAbierta("Sin detalles de horario", LocalTime.of(10, 00)), false);


    }
}
