package es.unican.gasolineras.activities.puntoInteres;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import es.unican.gasolineras.model.PuntoInteres;
import es.unican.gasolineras.repository.AppDatabase;
import es.unican.gasolineras.repository.DbFunctions;
import es.unican.gasolineras.repository.IPuntosInteresDAO;

@RunWith(RobolectricTestRunner.class)
public class AnhadirPuntoInteresPresenterTest {

    AppDatabase db;

    Context context = ApplicationProvider.getApplicationContext();

    private IAnhadirPuntoInteresContract.View vistaMock;
    private IPuntosInteresDAO puntosInteresDao;
    private AnhadirPuntoInteresPresenter presenter;

    String nombreStr = "casa";
    String latitudStr = "43.462274";
    String longitudStr = "-3.809740";

    double latitud = Double.parseDouble(latitudStr);
    double longitud = Double.parseDouble(longitudStr);

    PuntoInteres p;

    @Before
    public void setUp() {

        vistaMock = mock(IAnhadirPuntoInteresContract.View.class);

        db = DbFunctions.generaBaseDatosPuntosInteres(context);
        puntosInteresDao = db.puntosInteresDao();
        //puntosInteresDao.delete(any(PuntoInteres.class));

        when(vistaMock.getPuntosInteresDAO()).thenReturn(puntosInteresDao);

        p = new PuntoInteres(nombreStr, latitud, longitud);

        presenter = new AnhadirPuntoInteresPresenter(vistaMock);
    }

    @Test
    public void TestOnGuardarPuntoInteresClicked() {

        // Caso valido
        presenter.onGuardarPuntoInteresClicked(nombreStr, latitudStr, longitudStr);
        PuntoInteres puntoCapturado = puntosInteresDao.loadByName(nombreStr);
        assertEquals(nombreStr, puntoCapturado.getNombre());
        assertEquals(latitud, puntoCapturado.getLatitud(), 0.0);
        assertEquals(longitud, puntoCapturado.getLongitud(), 0.0);
        verify(vistaMock).mostrarMensaje("Punto de interés guardado");

        // Caso no valido (Ya existe el punto de interes con ese nombre)
        presenter.onGuardarPuntoInteresClicked(nombreStr, "43.4733", "-3.80111");
        verify(vistaMock).mostrarMensaje("Error: Punto interés existente");

        // Caso no válido (No se introducen datos)
        presenter.onGuardarPuntoInteresClicked("","","");
        verify(vistaMock).mostrarMensaje("Por favor, llene todos los campos");

        // Caso no válido (Latitud incorrecta)
        presenter.onGuardarPuntoInteresClicked("Punto1","-91","0");
        verify(vistaMock).mostrarMensaje("La latitud está fuera de los límites permitidos. No se ha guardado el punto");

        // Caso no válido (Longitud incorrecta)
        presenter.onGuardarPuntoInteresClicked("Punto1","0","181");
        verify(vistaMock).mostrarMensaje("La latitud está fuera de los límites permitidos. No se ha guardado el punto");

        /* Caso no valido (Error acceso a BBDD)
        presenter.onGuardarPuntoInteresClicked("pabellon", "43.47578", "-3.76644");
        verify(vistaMock).mostrarMensaje("Ha ocurrido un error en la base de datos");*/

        puntosInteresDao.delete(puntoCapturado);

    }
}
