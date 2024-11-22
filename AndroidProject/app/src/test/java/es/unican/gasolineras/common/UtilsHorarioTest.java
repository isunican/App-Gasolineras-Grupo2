package es.unican.gasolineras.common;

import static es.unican.gasolineras.common.UtilsHorario.gasolineraAbierta;
import static es.unican.gasolineras.common.UtilsHorario.procesaHorario;
import org.junit.Assert;
import org.junit.Test;
import java.time.LocalTime;

public class UtilsHorarioTest {

    @Test
    public void procesaHorarioTest() {
        Assert.assertEquals("08:00-21:00", procesaHorario("L-D: 08:00-21:00", "L"));
        Assert.assertEquals("08:00-14:00 y 16:00-22:00", procesaHorario("L-D: 08:00-14:00 y 16:00-22:00", "L"));
        Assert.assertEquals("Todo el día", procesaHorario("M-V: 08:00-21:00", "L"));
        Assert.assertEquals("24H", procesaHorario("L-D: 24H", "L"));
        Assert.assertEquals("Sin detalles de horario", procesaHorario("", "L"));
    }

    @Test
    public void gasolineraAbiertaTest(){
        Assert.assertEquals(true, gasolineraAbierta("08:00-14:00 y 16:00-20:00", LocalTime.of(10, 00)));
        Assert.assertEquals(false, gasolineraAbierta("08:00-14:00 y 16:00-20:00", LocalTime.of(15, 00)));
        Assert.assertEquals(true, gasolineraAbierta("08:00-14:00 y 16:00-20:00", LocalTime.of(8, 00)));
        Assert.assertEquals(false, gasolineraAbierta("08:00-14:00 y 16:00-20:00", LocalTime.of(14, 00,00,1)));
        Assert.assertEquals(true, gasolineraAbierta("24H", LocalTime.of(10, 00)));
        Assert.assertEquals(false, gasolineraAbierta("Todo el día", LocalTime.of(10, 00)));
        Assert.assertEquals(false, gasolineraAbierta("Sin detalles de horario", LocalTime.of(10, 00)));


    }
}
