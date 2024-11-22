package es.unican.gasolineras.activities.main;


import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static es.unican.gasolineras.utils.MockRepositories.getTestRepositoryList;

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

public class MainPresenterITest {
    private Gasolinera cercana;
    private Gasolinera lejana;
    private Gasolinera auxCercana;

    @Mock
    private static IGasolinerasRepository mockGasolineras;

    @Mock
    private static IMainContract.View mockVista;

    private static MainPresenter sut;

    private Gasolinera gasolinera1;
    private Gasolinera gasolinera2;
    private Gasolinera gasolinera3;
    private Gasolinera gasolinera4;

    private List<Gasolinera> listaGasolineras = new ArrayList<>();

    @Mock
    private static IPuntosInteresDAO mockPuntoInteres;

    private Gasolinera gasolineraCercana;
    private Gasolinera gasolineraNeutra;
    private Gasolinera gasolineraLejana;
    private Gasolinera gasolineraMuylejana;

    private PuntoInteres universidad;

    @Mock
    private IMainContract.View mockView;

    private IGasolinerasRepository mockRepository;;

    private MainPresenter presenter;

    @Before
    public void inicializa(){

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
        listaGasolineras = Arrays.asList(gasolinera1, gasolinera2, gasolinera3, gasolinera4);

        mockRepository =  getTestRepositoryList(listaGasolineras);

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

        gasolineraMuylejana= new Gasolinera();
        gasolineraMuylejana.setId("GasolineraJuan");
        gasolineraMuylejana.setDireccion("America");
        gasolineraMuylejana.setLatitud(0.0);
        gasolineraMuylejana.setLongitud(0.0);


        when(mockPuntoInteres.loadByName("Universidad")).thenReturn(universidad);
        
        sut = new MainPresenter();
        presenter = new MainPresenter();
    }

    @Test
    public void testFiltraYOrdenaGasolinerasPorPrecioMaximoYDistanciaLimiteBajo() {
        double precioMaximo = 1.06;

        // Mockeo de gasolineras y llamada al repositorio
        doAnswer(invocation -> {
            ICallBack callBack = invocation.getArgument(0);
            callBack.onSuccess(listaGasolineras); // Lista de gasolineras definida en la prueba
            return null;
        }).when(mockGasolineras).requestGasolineras(any(ICallBack.class), any(String.class));

        when(mockVista.getGasolinerasRepository()).thenReturn(mockGasolineras);

        ArgumentCaptor<List<Gasolinera>> captor = ArgumentCaptor.forClass(List.class);

        // Inicializo y aplico filtros de precio y distancia
        sut.init(mockVista);
        sut.filtraGasolinerasPorPrecioMaximo(precioMaximo, TipoCombustible.GASOLEO_A);
        sut.ordenarGasolinerasCercanasPtoInteres(universidad);

        // Verifico que se muestre la lista filtrada y ordenada
        verify(mockVista, times(3)).showStations(captor.capture());

        List<Gasolinera> gasolinerasFiltradas = captor.getValue();
        assertEquals(1, gasolinerasFiltradas.size());
        assertEquals(gasolinera1, gasolinerasFiltradas.get(0));
    }

    @Test
    public void testFiltraYOrdenaGasolinerasPorPrecioMaximoYDistanciaLimiteAlto() {
        double precioMaximo = 1.26;

        doAnswer(invocation -> {
            ICallBack callBack = invocation.getArgument(0);
            callBack.onSuccess(listaGasolineras);
            return null;
        }).when(mockGasolineras).requestGasolineras(any(ICallBack.class), any(String.class));

        when(mockVista.getGasolinerasRepository()).thenReturn(mockGasolineras);

        ArgumentCaptor<List<Gasolinera>> captor = ArgumentCaptor.forClass(List.class);

        sut.init(mockVista);
        sut.filtraGasolinerasPorPrecioMaximo(precioMaximo, TipoCombustible.GASOLINA_98_E5);
        sut.ordenarGasolinerasCercanasPtoInteres(universidad);

        verify(mockVista, times(3)).showStations(captor.capture());

        List<Gasolinera> gasolinerasFiltradas = captor.getValue();
        assertEquals(3, gasolinerasFiltradas.size());
        assertEquals(gasolinera1, gasolinerasFiltradas.get(0));
        assertEquals(gasolinera2, gasolinerasFiltradas.get(1));
        assertEquals(gasolinera3, gasolinerasFiltradas.get(2));
    }

    @Test
    public void testFiltraYOrdenaGasolinerasPorPrecioMaximoYDistanciaLimiteIgual() {
        double precioMaximo = 1.203;

        doAnswer(invocation -> {
            ICallBack callBack = invocation.getArgument(0);
            callBack.onSuccess(listaGasolineras);
            return null;
        }).when(mockGasolineras).requestGasolineras(any(ICallBack.class), any(String.class));

        when(mockVista.getGasolinerasRepository()).thenReturn(mockGasolineras);

        ArgumentCaptor<List<Gasolinera>> captor = ArgumentCaptor.forClass(List.class);

        sut.init(mockVista);
        sut.filtraGasolinerasPorPrecioMaximo(precioMaximo, TipoCombustible.GASOLINA_98_E5);
        sut.ordenarGasolinerasCercanasPtoInteres(universidad);

        verify(mockVista, times(3)).showStations(captor.capture());

        List<Gasolinera> gasolinerasFiltradas = captor.getValue();
        assertEquals(2, gasolinerasFiltradas.size());
        assertEquals(gasolinera1, gasolinerasFiltradas.get(0));
        assertEquals(gasolinera2, gasolinerasFiltradas.get(1));
    }

//    @Test
//    public void testFiltraYOrdenaGasolinerasPorPrecioMaximoYDistanciaSinResultados() {
//        double precioMaximo = 0.5;
//
//        doAnswer(invocation -> {
//            ICallBack callBack = invocation.getArgument(0);
//            callBack.onSuccess(listaGasolineras);
//            return null;
//        }).when(mockGasolineras).requestGasolineras(any(ICallBack.class), any(String.class));
//
//        when(mockVista.getGasolinerasRepository()).thenReturn(mockGasolineras);
//
//        ArgumentCaptor<List<Gasolinera>> captor = ArgumentCaptor.forClass(List.class);
//
//        sut.init(mockVista);
//        sut.filtraGasolinerasPorPrecioMaximo(precioMaximo, TipoCombustible.GASOLEO_A);
//        sut.ordenarGasolinerasCercanasPtoInteres(universidad);
//
//        verify(mockVista, times(3)).showStations(captor.capture());
//
//        List<Gasolinera> gasolinerasFiltradas = captor.getValue();
//        assertEquals(0, gasolinerasFiltradas.size());
//    }

    @Test
    public void testFiltraYOrdenaGasolinerasPorPrecioMaximoMuyAlto() {
        double precioMaximo = 10.0;

        doAnswer(invocation -> {
            ICallBack callBack = invocation.getArgument(0);
            callBack.onSuccess(listaGasolineras);
            return null;
        }).when(mockGasolineras).requestGasolineras(any(ICallBack.class), any(String.class));

        when(mockVista.getGasolinerasRepository()).thenReturn(mockGasolineras);

        ArgumentCaptor<List<Gasolinera>> captor = ArgumentCaptor.forClass(List.class);

        sut.init(mockVista);
        sut.filtraGasolinerasPorPrecioMaximo(precioMaximo, TipoCombustible.GASOLEO_A);
        sut.ordenarGasolinerasCercanasPtoInteres(universidad);

        verify(mockVista, times(3)).showStations(captor.capture());

        List<Gasolinera> gasolinerasFiltradas = captor.getValue();
        assertEquals(4, gasolinerasFiltradas.size());
        assertEquals(gasolinera1, gasolinerasFiltradas.get(0));
        assertEquals(gasolinera2, gasolinerasFiltradas.get(1));
        assertEquals(gasolinera3, gasolinerasFiltradas.get(2));
        assertEquals(gasolinera4, gasolinerasFiltradas.get(3));
    }

    @Test
    public void testInit() {
        // Configura mocks
        when(mockView.getGasolinerasRepository()).thenReturn(mockRepository);

        // Asocia la vista y ejecuta init
        presenter.init(mockView);

        // Verifica que los metodos de la vista fue llamado
        verify(mockView).init();
        verify(mockView).getGasolinerasRepository();
        verify(mockView).showStations(listaGasolineras);
        verify(mockView).showLoadCorrect(4);
        verify(mockView).getPuntosInteresDAO();
    }
}
