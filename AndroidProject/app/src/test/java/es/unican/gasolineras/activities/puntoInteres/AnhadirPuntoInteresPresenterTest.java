package es.unican.gasolineras.activities.puntoInteres;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import es.unican.gasolineras.model.PuntoInteres;
import es.unican.gasolineras.repository.AppDatabase;
import es.unican.gasolineras.repository.DbFunctions;
import es.unican.gasolineras.repository.IPuntosInteresDAO;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RunWith(RobolectricTestRunner.class)
public class AnhadirPuntoInteresPresenterTest {

    AppDatabase db;

    Context context = ApplicationProvider.getApplicationContext();

    @Mock
    private IAnhadirPuntoInteresContract.View vistaMock;

    @Mock
    private IPuntosInteresDAO puntosInteresDaoMock;

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
        MockitoAnnotations.openMocks(this);
        vistaMock = mock(IAnhadirPuntoInteresContract.View.class);

        db = DbFunctions.generaBaseDatosPuntosInteres(context);
        puntosInteresDao = db.puntosInteresDao();
        //puntosInteresDao.delete(any(PuntoInteres.class));

        //when(vistaMock.getPuntosInteresDAO()).thenReturn(puntosInteresDao);
        when(vistaMock.getPuntosInteresDAO()).thenReturn(puntosInteresDaoMock);
        p = new PuntoInteres(nombreStr, latitud, longitud);
        presenter = new AnhadirPuntoInteresPresenter(vistaMock);
    }

    @After
    public void tearDown() {
        // Borrar bd creada
        if (db != null && db.isOpen()) {
            db.close();
        }
    }

//     @Test
//     public void TestOnGuardarPuntoInteresClicked() {
//
//     // Caso valido
//     presenter.onGuardarPuntoInteresClicked(nombreStr, latitudStr, longitudStr);
//     PuntoInteres puntoCapturado = puntosInteresDao.loadByName(nombreStr);
//     assertEquals(nombreStr, puntoCapturado.getNombre());
//     assertEquals(latitud, puntoCapturado.getLatitud(), 0.0);
//     assertEquals(longitud, puntoCapturado.getLongitud(), 0.0);
//     verify(vistaMock).mostrarMensaje("Punto de interés guardado");
//
//     // Caso no valido (Ya existe el punto de interes con ese nombre)
//     presenter.onGuardarPuntoInteresClicked(nombreStr, "43.4733", "-3.80111");
//     verify(vistaMock).mostrarMensaje("Error: Punto interés existente");
//
//     // Caso no válido (No se introducen datos)
//     presenter.onGuardarPuntoInteresClicked("","","");
//     verify(vistaMock).mostrarMensaje("Por favor, llene todos los campos");
//
//     // Caso no válido (Latitud incorrecta)
//     presenter.onGuardarPuntoInteresClicked("Punto1","-91","0");
//     verify(vistaMock).mostrarMensaje("Error: Latitud fuera de limites");
//
//     // Caso no válido (Longitud incorrecta)
//     presenter.onGuardarPuntoInteresClicked("Punto1","0","181");
//     verify(vistaMock).mostrarMensaje("Error: Longitud fuera de limites");
//
//     // Caso no valido (Error acceso a BBDD)
//     presenter.onGuardarPuntoInteresClicked("pabellon", "43.47578", "-3.76644");
//     verify(vistaMock).mostrarMensaje("Ha ocurrido un error en la base de datos");
//
//     puntosInteresDao.delete(puntoCapturado);
//     }

    @Test
    public void testOnGuardarPuntoInteresClicked_ValidInput() {
        String nombre = "casa";
        double latitud = 43.462274;
        double longitud = -3.809740;

        ArgumentCaptor<PuntoInteres> argumentCaptor = ArgumentCaptor.forClass(PuntoInteres.class);

        when(puntosInteresDaoMock.loadByName(nombre)).thenReturn(null);

        presenter.onGuardarPuntoInteresClicked(nombre, String.valueOf(latitud), String.valueOf(longitud));

        verify(puntosInteresDaoMock).insertAll(argumentCaptor.capture());

        PuntoInteres puntoCapturado = argumentCaptor.getValue();
        assertEquals(nombre, puntoCapturado.getNombre());
        assertEquals(latitud, puntoCapturado.getLatitud(), 0.0);
        assertEquals(longitud, puntoCapturado.getLongitud(), 0.0);
        verify(vistaMock).mostrarMensaje("Punto de interés guardado");
    }

    @Test
    public void testOnGuardarPuntoInteresClicked_DuplicateName() {
        // Arrange
        String nombre = "casa"; // Nombre duplicado
        double latitud = 43.462274;
        double longitud = -3.809740;

        doThrow(new SQLiteConstraintException()).when(puntosInteresDaoMock).insertAll(any());
        presenter.onGuardarPuntoInteresClicked(nombre, String.valueOf(latitud), String.valueOf(longitud));
        verify(vistaMock).mostrarMensaje("Error: Punto interés existente");
    }


    @Test
    public void testOnGuardarPuntoInteresClicked_EmptyFields() {
        // Campos vacíos
        presenter.onGuardarPuntoInteresClicked("", "", "");

        verify(vistaMock).mostrarMensaje("Por favor, llene todos los campos");
    }

    @Test
    public void testOnGuardarPuntoInteresClicked_InvalidLatitude() {
        // Latitud inválida
        presenter.onGuardarPuntoInteresClicked("Punto1", "-91", "0");

        verify(vistaMock).mostrarMensaje("Error: Latitud fuera de limites");
    }

    @Test
    public void testOnGuardarPuntoInteresClicked_InvalidLongitude() {
        // Longitud inválida
        presenter.onGuardarPuntoInteresClicked("Punto1", "0", "181");

        verify(vistaMock).mostrarMensaje("Error: Longitud fuera de limites");
    }
}

