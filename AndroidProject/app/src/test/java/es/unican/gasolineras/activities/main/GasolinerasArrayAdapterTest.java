package es.unican.gasolineras.activities.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;

public class GasolinerasArrayAdapterTest {

    private GasolinerasArrayAdapter adapter;

    @Before
    public void setUp() throws Exception {
        adapter = new GasolinerasArrayAdapter(null, null);
    }

    @Test
    public void procesaHorarioTest() {
        assertEquals(adapter.procesaHorario("L-D: 08:00-21:00", "L"), "08:00-21:00");
        assertEquals(adapter.procesaHorario("L-D: 08:00-14:00 y 16:00-22:00", "L"), "08:00-14:00, 16:00-22:00");
        assertEquals(adapter.procesaHorario("M-V: 08:00-21:00", "L"), "Todo el día");
        assertEquals(adapter.procesaHorario("L-D: 24H", "L"), "24H");
        assertEquals(adapter.procesaHorario("", "L"), "Sin detalles de horario");
    }

    @Test
    public void gasolineraAbierta(){
        assertEquals(adapter.gasolineraAbierta("08:00-14:00 y 16:00-20:00", LocalTime.of(10, 00)), true);
        assertEquals(adapter.gasolineraAbierta("08:00-14:00 y 16:00-20:00", LocalTime.of(15, 00)), false);
        assertEquals(adapter.gasolineraAbierta("08:00-14:00 y 16:00-20:00", LocalTime.of(8, 00)), true);
        assertEquals(adapter.gasolineraAbierta("08:00-14:00 y 16:00-20:00", LocalTime.of(14, 00,00,1)), false);
        assertEquals(adapter.gasolineraAbierta("24H", LocalTime.of(10, 00)), true);
        assertEquals(adapter.gasolineraAbierta("Todo el día", LocalTime.of(10, 00)), false);
        assertEquals(adapter.gasolineraAbierta("Sin detalles de horario", LocalTime.of(10, 00)), false);


    }
}
