package es.unican.gasolineras.activities.main;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import es.unican.gasolineras.model.Gasolinera;
import es.unican.gasolineras.model.TipoCombustible;
import es.unican.gasolineras.repository.ICallBack;
import es.unican.gasolineras.repository.IGasolinerasRepository;

public class FiltrarPorPrecioMaximoCombustibleTest {

    @Mock
    private static IGasolinerasRepository mockGasolineras;

    @Mock
    private static IMainContract.View mockVista;

    private static MainPresenter sut;

    private Gasolinera gasolinera1;
    private Gasolinera gasolinera2;
    private Gasolinera gasolinera3;
    private Gasolinera gasolinera4;

    private List<Gasolinera> listaGasolineras;

    @Before
    public void inicializa() {

        // Inicializo los mocks
        MockitoAnnotations.openMocks(this);

        // Creo las gasolineras con precios ascendentes
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

        sut = new MainPresenter();
    }

    @Test
    public void testFiltraGasolinerasPorPrecioMaximoLimiteBajo() {
        double precioMaximo = 1.06;

        doAnswer(invocation -> {
            ICallBack callBack = invocation.getArgument(0);
            callBack.onSuccess(listaGasolineras);
            return null;
        }).when(mockGasolineras).requestGasolineras(any(ICallBack.class), any(String.class));

        when(mockVista.getGasolinerasRepository()).thenReturn(mockGasolineras);

        ArgumentCaptor<List<Gasolinera>> captor = ArgumentCaptor.forClass(List.class);

        sut.init(mockVista);
        sut.filtraGasolinerasPorPrecioMaximo(precioMaximo, TipoCombustible.GASOLEO_A);

        verify(mockVista, times(2)).showStations(captor.capture());

        List<Gasolinera> gasolinerasFiltradas = captor.getValue();
        assertEquals(1, gasolinerasFiltradas.size());
        assertEquals(gasolinera1, gasolinerasFiltradas.get(0));
    }

    @Test
    public void testFiltraGasolinerasPorPrecioMaximoLimiteAlto() {
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

        verify(mockVista, times(2)).showStations(captor.capture());

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
            callBack.onSuccess(listaGasolineras);
            return null;
        }).when(mockGasolineras).requestGasolineras(any(ICallBack.class), any(String.class));

        when(mockVista.getGasolinerasRepository()).thenReturn(mockGasolineras);

        ArgumentCaptor<List<Gasolinera>> captor = ArgumentCaptor.forClass(List.class);

        sut.init(mockVista);
        sut.filtraGasolinerasPorPrecioMaximo(precioMaximo, TipoCombustible.GASOLINA_98_E5);

        verify(mockVista, times(2)).showStations(captor.capture());

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
            callBack.onSuccess(listaGasolineras);
            return null;
        }).when(mockGasolineras).requestGasolineras(any(ICallBack.class), any(String.class));

        when(mockVista.getGasolinerasRepository()).thenReturn(mockGasolineras);

        ArgumentCaptor<List<Gasolinera>> captor = ArgumentCaptor.forClass(List.class);

        sut.init(mockVista);
        sut.filtraGasolinerasPorPrecioMaximo(precioMaximo, TipoCombustible.HIDROGENO);

        verify(mockVista, times(2)).showStations(captor.capture());

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
            callBack.onSuccess(listaGasolineras);
            return null;
        }).when(mockGasolineras).requestGasolineras(any(ICallBack.class), any(String.class));

        when(mockVista.getGasolinerasRepository()).thenReturn(mockGasolineras);

        ArgumentCaptor<List<Gasolinera>> captor = ArgumentCaptor.forClass(List.class);

        sut.init(mockVista);
        sut.filtraGasolinerasPorPrecioMaximo(precioMaximo, TipoCombustible.GASOLINA_98_E5);

        verify(mockVista, times(2)).showStations(captor.capture());

        List<Gasolinera> gasolinerasFiltradas = captor.getValue();
        assertEquals(0, gasolinerasFiltradas.size());
    }

    @Test
    public void testFiltraGasolinerasPrecioMuyAlto() {
        double precioMaximo = 10.0;

        doAnswer(invocation -> {
            ICallBack callBack = invocation.getArgument(0);
            callBack.onSuccess(listaGasolineras);
            return null;
        }).when(mockGasolineras).requestGasolineras(any(ICallBack.class), any(String.class));

        when(mockVista.getGasolinerasRepository()).thenReturn(mockGasolineras);

        ArgumentCaptor<List<Gasolinera>> captor = ArgumentCaptor.forClass(List.class);

        sut.init(mockVista);
        sut.filtraGasolinerasPorPrecioMaximo(precioMaximo, TipoCombustible.GASOLINA_98_E5);

        verify(mockVista, times(2)).showStations(captor.capture());

        List<Gasolinera> gasolinerasFiltradas = captor.getValue();
        assertEquals(4, gasolinerasFiltradas.size());
        assertEquals(gasolinera1, gasolinerasFiltradas.get(0));
        assertEquals(gasolinera2, gasolinerasFiltradas.get(1));
        assertEquals(gasolinera3, gasolinerasFiltradas.get(2));
        assertEquals(gasolinera4, gasolinerasFiltradas.get(3));
    }

}
