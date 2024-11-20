package es.unican.gasolineras.activities.puntoInteres;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import es.unican.gasolineras.activities.main.MainView;
import es.unican.gasolineras.model.PuntoInteres;
import es.unican.gasolineras.repository.AppDatabase;
import es.unican.gasolineras.repository.DbFunctions;
import es.unican.gasolineras.repository.IPuntosInteresDAO;

@RunWith(RobolectricTestRunner.class)
public class AnhadirPuntoInteresPresenterITest {

    private IAnhadirPuntoInteresContract.View vistaMock;
    private AnhadirPuntoInteresPresenter presenter;
    private AppDatabase database;

    Context context = ApplicationProvider.getApplicationContext();

    private PuntoInteres p;

    private IPuntosInteresDAO dao;
    private final String nombreStr = "casa";
    private final String latitudStr = "43.462274";
    private final String longitudStr = "-3.809740";

    double latitud = Double.parseDouble(latitudStr);
    double longitud = Double.parseDouble(longitudStr);

    @Before
    public void setUp() {
        // Inicializar la base de datos en memoria
        database = DbFunctions.generaBaseDatosPuntosInteres(context);

        // Obtener la DAO
        dao = database.puntosInteresDao();
        
        vistaMock = mock(IAnhadirPuntoInteresContract.View.class);
        
        // Crear el presenter
        presenter = new AnhadirPuntoInteresPresenter(vistaMock);

        p = new PuntoInteres(nombreStr, latitud, longitud);

    }

    @After
    public void tearDown() {
        // Cerrar la base de datos
        database.close();
    }

    @Test
    public void TestOnGuardarPuntoInteresClicked() {
        // Caso válido
        presenter.onGuardarPuntoInteresClicked(nombreStr, latitudStr, longitudStr);
        PuntoInteres puntoCapturado = dao.loadByName(nombreStr);
        assertNotNull(puntoCapturado);
        assertEquals(puntoCapturado,p);
        verify(vistaMock).mostrarMensaje("Punto de interés guardado");

        // Caso no válido (Ya existe el punto de interés con ese nombre)
        presenter.onGuardarPuntoInteresClicked(nombreStr, "43.4733", "-3.80111");
        verify(vistaMock).mostrarMensaje("Ya existe un punto de interés con ese nombre");

        // Caso no válido (No se introducen datos)
        presenter.onGuardarPuntoInteresClicked("", "", "");
        verify(vistaMock).mostrarMensaje("Por favor, llene todos los campos");

        // Caso no válido (Latitud incorrecta)
        presenter.onGuardarPuntoInteresClicked("Punto2", "-91", "0");
        verify(vistaMock).mostrarMensaje("La latitud está fuera de los límites permitidos. No se ha guardado el punto");

        // Caso no válido (Longitud incorrecta)
        presenter.onGuardarPuntoInteresClicked("Punto3", "0", "181");
        verify(vistaMock).mostrarMensaje("La longitud está fuera de los límites permitidos. No se ha guardado el punto");

        // Eliminar el punto de interés insertado
        dao.delete(puntoCapturado);
    }
}
