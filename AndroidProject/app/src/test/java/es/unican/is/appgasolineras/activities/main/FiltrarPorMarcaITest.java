package es.unican.is.appgasolineras.activities.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import es.unican.is.appgasolineras.common.utils.EnumTypes.CombustibleType;
import es.unican.is.appgasolineras.repository.GasolinerasRepository;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.db.GasolineraDatabase;
import es.unican.is.appgasolineras.repository.rest.GasolinerasService;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

@Config(manifest=Config.NONE, sdk = {Build.VERSION_CODES.O_MR1})

@RunWith(RobolectricTestRunner.class)
public class FiltrarPorMarcaITest {

    @BeforeClass
    public static void setUp() {
        GasolinerasServiceConstants.setStaticURL2();
    }

    @AfterClass
    public static void clean() {

        GasolinerasService.resetAPI();
        GasolinerasServiceConstants.setMinecoURL();
    }

    private List<String> brandsList = new ArrayList<>();
    private List<String> wrongBrandList = new ArrayList<>();

    private MainPresenter sut;
    @Mock
    private IMainContract.View mockMainView;
    private IGasolinerasRepository  fuelStationRepository;

    @Before
    public void start(){
        MockitoAnnotations.openMocks(this);
        sut = new MainPresenter(mockMainView);

        Context context = ApplicationProvider.getApplicationContext();

        fuelStationRepository = new GasolinerasRepository(context);
        when(mockMainView.getGasolineraRepository()).thenReturn(fuelStationRepository);

        sut.init();

        brandsList.add("REPSOL");
        brandsList.add("CEPSA");
        wrongBrandList.add("REPSOLITO");
    }

    @After
    public void end() {
        GasolineraDatabase db = GasolineraDatabase.getDB(ApplicationProvider.getApplicationContext());
        db.close();
    }

    @Test
    public void testFilterByBrand() {
        // Caso valido: lista con una marca existente
        sut.filter(CombustibleType.ALL_COMB, brandsList.subList(0, 1), false);
        assertEquals(2, sut.getShownGasolineras().size());
        assertEquals("REPSOL", sut.getShownGasolineras().get(0).getRotulo());
        assertEquals("REPSOL", sut.getShownGasolineras().get(1).getRotulo());

        // Caso v??lido: lista con mas de una marca existente
        sut.filter(CombustibleType.ALL_COMB, brandsList, false);
        assertEquals(3, sut.getShownGasolineras().size());
        assertEquals("REPSOL", sut.getShownGasolineras().get(0).getRotulo());
        assertEquals("REPSOL", sut.getShownGasolineras().get(1).getRotulo());
        assertEquals("CEPSA", sut.getShownGasolineras().get(2).getRotulo());

        // Caso valido: lista vacia
        sut.filter(CombustibleType.ALL_COMB, brandsList.subList(0, 0), false);
        assertEquals(10, sut.getShownGasolineras().size());
        assertEquals("CEPSA", sut.getShownGasolineras().get(0).getRotulo());
        assertEquals("G2", sut.getShownGasolineras().get(5).getRotulo());
        assertEquals("REPSOL", sut.getShownGasolineras().get(9).getRotulo());

        // Caso no valido: lista con una marca no existente
        sut.filter(CombustibleType.ALL_COMB, wrongBrandList, false);
        assertNull(sut.getShownGasolineras());
    }
}
