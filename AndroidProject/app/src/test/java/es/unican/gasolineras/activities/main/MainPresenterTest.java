package es.unican.gasolineras.activities.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.unican.gasolineras.model.Gasolinera;
import es.unican.gasolineras.model.PuntoInteres;
import es.unican.gasolineras.model.TipoCombustible;
import es.unican.gasolineras.repository.ICallBack;
import es.unican.gasolineras.repository.IGasolinerasRepository;
import es.unican.gasolineras.repository.IPuntosInteresDAO;

public class MainPresenterTest {

    private GasolineraDistanciaComparator comparadorDistancia;

    private Gasolinera cercana;
    private Gasolinera lejana;
    private Gasolinera auxCercana;


    @Mock
    private static IPuntosInteresDAO mockPuntoInteres;

    @Mock
    private static IGasolinerasRepository mockGasolineras;

    @Mock
    private static IMainContract.View mockVista;

    private static MainPresenter sut;

    private Gasolinera gasolineraCercana;
    private Gasolinera gasolineraNeutra;
    private Gasolinera gasolineraLejana;
    private Gasolinera gasolineraMuylejana;

    private PuntoInteres universidad;

    private List<Gasolinera> listaGasolineras;

    @Mock
    private static IGasolinerasRepository mockGasolineras2;

    @Mock
    private static IMainContract.View mockVista2;

    private static MainPresenter sut2;

    private Gasolinera gasolinera1;
    private Gasolinera gasolinera2;
    private Gasolinera gasolinera3;
    private Gasolinera gasolinera4;

    private List<Gasolinera> listaGasolineras2;

    @Mock
    private static IGasolinerasRepository mockGasolineras3;

    @Mock
    private static IMainContract.View mockVista3;

    private static MainPresenter sut3;

    private List<Gasolinera> listaGasolineras3;


    @Before
    public void inicializa() {

        // Inicializo los mocks
        MockitoAnnotations.openMocks(this);

        //creo gasolineras necesarias para DistanciaComparatorTest

        cercana = new Gasolinera();
        cercana.setId("GasolineraPaco");
        cercana.setDireccion("Sardinero");
        cercana.setLatitud(43.47618775921668);
        cercana.setLongitud(-3.7933535145721233);

        lejana = new Gasolinera();
        lejana.setId("GasolineraMario");
        lejana.setDireccion("Torrelavega");
        lejana.setLatitud(43.356608665447474);
        lejana.setLongitud(-4.046146566530483);

        auxCercana = new Gasolinera();
        auxCercana.setId("GasolineraJaime");
        auxCercana.setLatitud(43.47618775921668);
        auxCercana.setLongitud(-3.7933535145721233);

        //creo un punto de interes
        universidad = new PuntoInteres();
        universidad.nombre = "Universidad";
        universidad.latitud = 43.47122194796555;
        universidad.longitud = -3.800797786772268;

        comparadorDistancia = new GasolineraDistanciaComparator(universidad);

        // Creo las gasolineras con precios ascendentes para Filtrar por precio maximo
        gasolinera1 = new Gasolinera();
        gasolinera1.setGasoleoA(1.053);
        gasolinera1.setGasolina98E5(1.153);

        gasolinera2 = new Gasolinera();
        gasolinera2.setGasoleoA(1.103);
        gasolinera2.setGasolina98E5(1.203);

        gasolinera3 = new Gasolinera();
        gasolinera3.setGasoleoA(1.153);
        gasolinera3.setGasolina98E5(1.253);
        gasolinera3.setHidrogeno(1.263);

        gasolinera4 = new Gasolinera();
        gasolinera4.setGasoleoA(1.203);
        gasolinera4.setGasolina98E5(1.303);
        gasolinera4.setHidrogeno(1.313);

        // Añado las gasolineras a la lista
        listaGasolineras2 = Arrays.asList(gasolinera1, gasolinera2, gasolinera3, gasolinera4);

        //creo las gasolineras para OrdenarGasolinerasCercanasTest
        gasolineraCercana = new Gasolinera();
        gasolineraCercana.setId("GasolineraPaco");
        gasolineraCercana.setDireccion("Sardinero");
        gasolineraCercana.setLatitud(43.47618775921668);
        gasolineraCercana.setLongitud(-3.7933535145721233);

        gasolineraLejana = new Gasolinera();
        gasolineraLejana.setId("GasolineraMario");
        gasolineraLejana.setDireccion("Torrelavega");
        gasolineraLejana.setLatitud(43.356608665447474);
        gasolineraLejana.setLongitud(-4.046146566530483);

        gasolineraNeutra = new Gasolinera();
        gasolineraNeutra.setId("GasolineraLuis");
        gasolineraNeutra.setDireccion("Bezana");
        gasolineraNeutra.setLatitud(43.406608665447474);
        gasolineraNeutra.setLongitud(-4.0);

        gasolineraMuylejana = new Gasolinera();
        gasolineraMuylejana.setId("GasolineraJuan");
        gasolineraMuylejana.setDireccion("America");
        gasolineraMuylejana.setLatitud(0.0);
        gasolineraMuylejana.setLongitud(0.0);


        when(mockPuntoInteres.loadByName("Universidad")).thenReturn(universidad);


        sut = new MainPresenter();
        sut2 = new MainPresenter();
        sut3 = new MainPresenter();
    }

    @Test
    public void testComparadorDistancia() {

        //caso que la primera gasolinera esta mas cerca
        assertEquals(comparadorDistancia.compare(cercana, lejana), -1);

        //caso que la primera gasolinera esta mas lejos
        assertEquals(comparadorDistancia.compare(lejana, cercana), 1);

        //caso que esten a la misma distancia
        assertEquals(comparadorDistancia.compare(cercana, auxCercana), 0);
    }


    @Test
    public void testFiltraGasolinerasPorPrecioMaximoLimiteBajo() {
        double precioMaximo = 1.06;

        doAnswer(invocation -> {
            ICallBack callBack = invocation.getArgument(0);
            callBack.onSuccess(listaGasolineras2);
            return null;
        }).when(mockGasolineras2).requestGasolineras(any(ICallBack.class), any(String.class));

        when(mockVista2.getGasolinerasRepository()).thenReturn(mockGasolineras2);

        ArgumentCaptor<List<Gasolinera>> captor = ArgumentCaptor.forClass(List.class);

        sut2.init(mockVista2);
        sut2.filtraGasolinerasPorPrecioMaximo(precioMaximo, TipoCombustible.GASOLEO_A);

        verify(mockVista2, times(2)).showStations(captor.capture());

        List<Gasolinera> gasolinerasFiltradas = captor.getValue();
        assertEquals(1, gasolinerasFiltradas.size());
        assertEquals(gasolinera1, gasolinerasFiltradas.get(0));
    }

    @Test
    public void testFiltraGasolinerasPorPrecioMaximoLimiteAlto() {
        double precioMaximo = 1.26;

        doAnswer(invocation -> {
            ICallBack callBack = invocation.getArgument(0);
            callBack.onSuccess(listaGasolineras2);
            return null;
        }).when(mockGasolineras2).requestGasolineras(any(ICallBack.class), any(String.class));

        when(mockVista2.getGasolinerasRepository()).thenReturn(mockGasolineras2);

        ArgumentCaptor<List<Gasolinera>> captor = ArgumentCaptor.forClass(List.class);

        sut2.init(mockVista2);
        sut2.filtraGasolinerasPorPrecioMaximo(precioMaximo, TipoCombustible.GASOLINA_98_E5);

        verify(mockVista2, times(2)).showStations(captor.capture());

        List<Gasolinera> gasolinerasFiltradas = captor.getValue();
        assertEquals(3, gasolinerasFiltradas.size());
        assertEquals(gasolinera1, gasolinerasFiltradas.get(0));
        assertEquals(gasolinera2, gasolinerasFiltradas.get(1));
        assertEquals(gasolinera3, gasolinerasFiltradas.get(2));
    }

    @Test
    public void testFiltraGasolinerasPorPrecioMaximoLimiteIgual() {
        double precioMaximo = 1.203;

        doAnswer(invocation -> {
            ICallBack callBack = invocation.getArgument(0);
            callBack.onSuccess(listaGasolineras2);
            return null;
        }).when(mockGasolineras2).requestGasolineras(any(ICallBack.class), any(String.class));

        when(mockVista2.getGasolinerasRepository()).thenReturn(mockGasolineras2);

        ArgumentCaptor<List<Gasolinera>> captor = ArgumentCaptor.forClass(List.class);

        sut2.init(mockVista2);
        sut2.filtraGasolinerasPorPrecioMaximo(precioMaximo, TipoCombustible.GASOLINA_98_E5);

        verify(mockVista2, times(2)).showStations(captor.capture());

        List<Gasolinera> gasolinerasFiltradas = captor.getValue();
        assertEquals(2, gasolinerasFiltradas.size());
        assertEquals(gasolinera1, gasolinerasFiltradas.get(0));
        assertEquals(gasolinera2, gasolinerasFiltradas.get(1));
    }

    @Test
    public void testFiltraGasolinerasPorPrecioMaximoSinCombustible() {
        double precioMaximo = 1.5;

        doAnswer(invocation -> {
            ICallBack callBack = invocation.getArgument(0);
            callBack.onSuccess(listaGasolineras2);
            return null;
        }).when(mockGasolineras2).requestGasolineras(any(ICallBack.class), any(String.class));

        when(mockVista2.getGasolinerasRepository()).thenReturn(mockGasolineras2);

        ArgumentCaptor<List<Gasolinera>> captor = ArgumentCaptor.forClass(List.class);

        sut2.init(mockVista2);
        sut2.filtraGasolinerasPorPrecioMaximo(precioMaximo, TipoCombustible.HIDROGENO);

        verify(mockVista2, times(2)).showStations(captor.capture());

        List<Gasolinera> gasolinerasFiltradas = captor.getValue();
        assertEquals(2, gasolinerasFiltradas.size());
        assertEquals(gasolinera3, gasolinerasFiltradas.get(0));
        assertEquals(gasolinera4, gasolinerasFiltradas.get(1));
    }

    @Test
    public void testFiltraGasolinerasPrecioMuyBajo() {
        double precioMaximo = 0.001;

        doAnswer(invocation -> {
            ICallBack callBack = invocation.getArgument(0);
            callBack.onSuccess(listaGasolineras2);
            return null;
        }).when(mockGasolineras2).requestGasolineras(any(ICallBack.class), any(String.class));

        when(mockVista2.getGasolinerasRepository()).thenReturn(mockGasolineras2);

        ArgumentCaptor<List<Gasolinera>> captor = ArgumentCaptor.forClass(List.class);

        sut2.init(mockVista2);
        sut2.filtraGasolinerasPorPrecioMaximo(precioMaximo, TipoCombustible.GASOLINA_98_E5);

        verify(mockVista2, times(2)).showStations(captor.capture());

        List<Gasolinera> gasolinerasFiltradas = captor.getValue();
        assertEquals(0, gasolinerasFiltradas.size());
    }

    @Test
    public void testFiltraGasolinerasPrecioMuyAlto() {
        double precioMaximo = 10.0;

        doAnswer(invocation -> {
            ICallBack callBack = invocation.getArgument(0);
            callBack.onSuccess(listaGasolineras2);
            return null;
        }).when(mockGasolineras2).requestGasolineras(any(ICallBack.class), any(String.class));

        when(mockVista2.getGasolinerasRepository()).thenReturn(mockGasolineras2);

        ArgumentCaptor<List<Gasolinera>> captor = ArgumentCaptor.forClass(List.class);

        sut2.init(mockVista2);
        sut2.filtraGasolinerasPorPrecioMaximo(precioMaximo, TipoCombustible.GASOLINA_98_E5);

        verify(mockVista2, times(2)).showStations(captor.capture());

        List<Gasolinera> gasolinerasFiltradas = captor.getValue();
        assertEquals(4, gasolinerasFiltradas.size());
        assertEquals(gasolinera1, gasolinerasFiltradas.get(0));
        assertEquals(gasolinera2, gasolinerasFiltradas.get(1));
        assertEquals(gasolinera3, gasolinerasFiltradas.get(2));
        assertEquals(gasolinera4, gasolinerasFiltradas.get(3));
    }


    @Test
    public void testOrdenaGasolinerasMasCercanas2Gasos() {

        //creo la lista de gasolineras que voy a mockear
        listaGasolineras = new ArrayList<>();
        listaGasolineras.add(gasolineraLejana);
        listaGasolineras.add(gasolineraCercana);

        doAnswer(invocation -> {
            ICallBack callBack = invocation.getArgument(0);
            callBack.onSuccess(listaGasolineras);
            return null;
        }).when(mockGasolineras).requestGasolineras(any(ICallBack.class), any(String.class));

        when(mockVista.getGasolinerasRepository()).thenReturn(mockGasolineras);

        //creo capturacion de la lista de las gasolineras ya ordenadas
        ArgumentCaptor<List<Gasolinera>> captor = ArgumentCaptor.forClass(List.class);

        //inicializo
        sut.init(mockVista);

        //llamo al metodo que pruebo
        sut.ordenarGasolinerasCercanasPtoInteres(universidad);

        //capturo la lista
        verify(mockVista, times(2)).showStations(captor.capture());

        //verifico que la lista este bien ordenada
        List<Gasolinera> listaCapturada = captor.getValue();
        assertEquals(gasolineraCercana, listaCapturada.get(0));
        assertEquals(gasolineraLejana, listaCapturada.get(1));
    }

    @Test
    public void testOrdenaGasolinerasMasCercanasMuchasGasos() {

        //creo la lista de gasolineras que voy a mockear
        listaGasolineras = new ArrayList<>();
        listaGasolineras.add(gasolineraLejana);
        listaGasolineras.add(gasolineraCercana);
        listaGasolineras.add(gasolineraMuylejana);
        listaGasolineras.add(gasolineraNeutra);

        doAnswer(invocation -> {
            ICallBack callBack = invocation.getArgument(0);
            callBack.onSuccess(listaGasolineras);
            return null;
        }).when(mockGasolineras).requestGasolineras(any(ICallBack.class), any(String.class));

        when(mockVista.getGasolinerasRepository()).thenReturn(mockGasolineras);

        doAnswer(invocation -> {
            ICallBack callBack = invocation.getArgument(0);
            callBack.onSuccess(listaGasolineras);
            return null;
        }).when(mockGasolineras).requestGasolineras(any(ICallBack.class), any(String.class));

        when(mockVista.getGasolinerasRepository()).thenReturn(mockGasolineras);

        //creo capturacion de la lista de las gasolineras ya ordenadas
        ArgumentCaptor<List<Gasolinera>> captor = ArgumentCaptor.forClass(List.class);

        //inicializo
        sut.init(mockVista);

        //llamo al metodo que pruebo
        sut.ordenarGasolinerasCercanasPtoInteres(universidad);

        //capturo la lista
        verify(mockVista, times(2)).showStations(captor.capture());

        //verifico que la lista este bien ordenada
        List<Gasolinera> listaCapturada = captor.getValue();
        assertEquals(gasolineraCercana, listaCapturada.get(0));
        assertEquals(gasolineraNeutra, listaCapturada.get(1));
        assertEquals(gasolineraLejana, listaCapturada.get(2));
        assertEquals(gasolineraMuylejana, listaCapturada.get(3));
    }

    @Test
    public void testQuitarFiltrosYOrdenacionesClicked_ListaFiltradaYOrdenada() {
        listaGasolineras3 = new ArrayList<>();
        listaGasolineras3.add(gasolineraLejana);
        listaGasolineras3.add(gasolineraCercana);
        listaGasolineras3.add(gasolineraMuylejana);
        listaGasolineras3.add(gasolineraNeutra);

        doAnswer(invocation -> {
            ICallBack callBack = invocation.getArgument(0);
            callBack.onSuccess(listaGasolineras3);
            return null;
        }).when(mockGasolineras3).requestGasolineras(any(ICallBack.class), any(String.class));

        when(mockVista3.getGasolinerasRepository()).thenReturn(mockGasolineras3);
        sut3.init(mockVista3);

        sut3.ordenarGasolinerasCercanasPtoInteres(universidad);
        sut3.filtraGasolinerasPorPrecioMaximo(1.5, TipoCombustible.GASOLEO_A);
        sut3.quitarFiltrosYOrdenaciones();


        assertFalse(sut3.estaFiltrada());
        assertFalse(sut3.estaOrdenada());

        ArgumentCaptor<List<Gasolinera>> captor = ArgumentCaptor.forClass(List.class);
        verify(mockVista3, times(4)).showStations(captor.capture());
        List<Gasolinera> listaCapturada = captor.getValue();
        assertEquals(gasolineraLejana, listaCapturada.get(0));
        assertEquals(gasolineraCercana, listaCapturada.get(1));
        assertEquals(gasolineraMuylejana, listaCapturada.get(2));
        assertEquals(gasolineraNeutra, listaCapturada.get(3));
    }

    @Test
    public void testQuitarFiltrosYOrdenacionesClicked_ListaSinFiltrarYOrdenada() {
        listaGasolineras3 = new ArrayList<>();
        listaGasolineras3.add(gasolineraLejana);
        listaGasolineras3.add(gasolineraCercana);
        listaGasolineras3.add(gasolineraMuylejana);
        listaGasolineras3.add(gasolineraNeutra);

        doAnswer(invocation -> {
            ICallBack callBack = invocation.getArgument(0);
            callBack.onSuccess(listaGasolineras3);
            return null;
        }).when(mockGasolineras3).requestGasolineras(any(ICallBack.class), any(String.class));

        when(mockVista3.getGasolinerasRepository()).thenReturn(mockGasolineras3);
        sut3.init(mockVista3);

        sut3.ordenarGasolinerasCercanasPtoInteres(universidad);
        sut3.quitarFiltrosYOrdenaciones();

        assertFalse(sut3.estaFiltrada());
        assertFalse(sut3.estaOrdenada());

        ArgumentCaptor<List<Gasolinera>> captor = ArgumentCaptor.forClass(List.class);
        verify(mockVista3, times(3)).showStations(captor.capture());
        List<Gasolinera> listaCapturada = captor.getValue();
        assertEquals(gasolineraLejana, listaCapturada.get(0));
        assertEquals(gasolineraCercana, listaCapturada.get(1));
        assertEquals(gasolineraMuylejana, listaCapturada.get(2));
        assertEquals(gasolineraNeutra, listaCapturada.get(3));
    }

    @Test
    public void testQuitarFiltrosYOrdenacionesClicked_ListaFiltradaYSinOrdenar() {
        listaGasolineras3 = new ArrayList<>();
        listaGasolineras3.add(gasolineraLejana);
        listaGasolineras3.add(gasolineraCercana);
        listaGasolineras3.add(gasolineraMuylejana);
        listaGasolineras3.add(gasolineraNeutra);

        doAnswer(invocation -> {
            ICallBack callBack = invocation.getArgument(0);
            callBack.onSuccess(listaGasolineras3);
            return null;
        }).when(mockGasolineras3).requestGasolineras(any(ICallBack.class), any(String.class));

        when(mockVista3.getGasolinerasRepository()).thenReturn(mockGasolineras3);
        sut3.init(mockVista3);

        sut3.filtraGasolinerasPorPrecioMaximo(1.5, TipoCombustible.GASOLEO_A);
        sut3.quitarFiltrosYOrdenaciones();

        assertFalse(sut3.estaFiltrada());
        assertFalse(sut3.estaOrdenada());

        ArgumentCaptor<List<Gasolinera>> captor = ArgumentCaptor.forClass(List.class);
        verify(mockVista3, times(3)).showStations(captor.capture());
        List<Gasolinera> listaCapturada = captor.getValue();
        assertEquals(gasolineraLejana, listaCapturada.get(0));
        assertEquals(gasolineraCercana, listaCapturada.get(1));
        assertEquals(gasolineraMuylejana, listaCapturada.get(2));
        assertEquals(gasolineraNeutra, listaCapturada.get(3));
    }

    @Test
    public void testQuitarFiltrosYOrdenacionesClicked_ListaSinFiltrarYSinOrdenar() {
        listaGasolineras3 = new ArrayList<>();
        listaGasolineras3.add(gasolineraLejana);
        listaGasolineras3.add(gasolineraCercana);
        listaGasolineras3.add(gasolineraMuylejana);
        listaGasolineras3.add(gasolineraNeutra);

        doAnswer(invocation -> {
            ICallBack callBack = invocation.getArgument(0);
            callBack.onSuccess(listaGasolineras3);
            return null;
        }).when(mockGasolineras3).requestGasolineras(any(ICallBack.class), any(String.class));

        when(mockVista3.getGasolinerasRepository()).thenReturn(mockGasolineras3);
        sut3.init(mockVista3);

        sut3.quitarFiltrosYOrdenaciones();


        assertFalse(sut3.estaFiltrada());
        assertFalse(sut3.estaOrdenada());

        ArgumentCaptor<List<Gasolinera>> captor = ArgumentCaptor.forClass(List.class);
        verify(mockVista3, times(1)).showStations(captor.capture());
        List<Gasolinera> listaCapturada = captor.getValue();
        assertEquals(gasolineraLejana, listaCapturada.get(0));
        assertEquals(gasolineraCercana, listaCapturada.get(1));
        assertEquals(gasolineraMuylejana, listaCapturada.get(2));
        assertEquals(gasolineraNeutra, listaCapturada.get(3));
    }
}