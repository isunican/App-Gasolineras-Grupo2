package es.unican.gasolineras.activities.main;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThrows;
import static es.unican.gasolineras.utils.MockRepositories.getTestRepository;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import dagger.hilt.android.testing.BindValue;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.UninstallModules;
import es.unican.gasolineras.R;
import es.unican.gasolineras.injection.RepositoriesModule;
import es.unican.gasolineras.model.PuntoInteres;
import es.unican.gasolineras.model.TipoCombustible;
import es.unican.gasolineras.repository.AppDatabase;
import es.unican.gasolineras.repository.DbFunctions;
import es.unican.gasolineras.repository.IGasolinerasRepository;


@UninstallModules(RepositoriesModule.class)
@HiltAndroidTest
public class QuitarFiltrosYOrdenacionUITest {

    private static final String GASOLEO_A = "GASOLEO_A";
    private static final String PUNTO_INTERES = "casa";

    @Rule(order = 0)
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule(order = 1)
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<>(MainView.class);

    final static Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @BindValue
    public final IGasolinerasRepository repository = getTestRepository(context, R.raw.gasolineras_borras_filtros_ordenacion);

    @Before
    public void setUp() {
        ActivityScenario.launch(MainView.class);
    }

    @BeforeClass
    public static void inicializa() {
        AppDatabase db = DbFunctions.generaBaseDatosPuntosInteres(getApplicationContext());
        db.puntosInteresDao().deleteAll();

        PuntoInteres puntoInteres = new PuntoInteres("casa", 43.4733, -3.80111);

        db.puntosInteresDao().insertAll(puntoInteres);
    }


    @Test
    public void testQuitarFiltrosYOrdenaciones_ListaFiltradaYOrdenada() {

        assertThrows(NoMatchingViewException.class, () -> {
            onView(withId(R.id.menuQuitarFiltrosYOrdenaciones)).perform(click());
        });
        aplicarFiltro();
        aplicarOrdenacion();
        quitarFiltrosYOrdenacion();
        verificarListaOriginal();
    }

    @Test
    public void testQuitarFiltrosYOrdenaciones_ListaSinFiltrarYOrdenada() {

        assertThrows(NoMatchingViewException.class, () -> {
            onView(withId(R.id.menuQuitarFiltrosYOrdenaciones)).perform(click());
        });
        aplicarOrdenacion();
        quitarFiltrosYOrdenacion();
        verificarListaOriginal();
    }

    @Test
    public void testQuitarFiltrosYOrdenaciones_ListaFiltradaPeroSinOrdenar() {

        assertThrows(NoMatchingViewException.class, () -> {
            onView(withId(R.id.menuQuitarFiltrosYOrdenaciones)).perform(click());
        });
        aplicarFiltro();
        quitarFiltrosYOrdenacion();
        verificarListaOriginal();
    }

    @Test
    public void testQuitarFiltrosYOrdenaciones_ListaSinFiltrarYSinOrdenar() {
        assertThrows(NoMatchingViewException.class, () -> {
            onView(withId(R.id.menuQuitarFiltrosYOrdenaciones)).perform(click());
        });
    }

    @Test
    public void testQuitarFiltrosYOrdenaciones_CancelarDialogo() {
        assertThrows(NoMatchingViewException.class, () -> {
            onView(withId(R.id.menuQuitarFiltrosYOrdenaciones)).perform(click());
        });

        aplicarFiltro();

        onView(withId(R.id.menuQuitarFiltrosYOrdenaciones)).perform(click());

        onView(withText("CANCELAR"))
                .inRoot(isDialog())
                .perform(click());

        DataInteraction g1 = onData(CoreMatchers.anything()).inAdapterView(withId(R.id.lvStations)).atPosition(0);
        g1.onChildView(withId(R.id.tvAddress)).check(matches(withText("CARRETERA 6316 KM. 10,5")));
    }

    private void quitarFiltrosYOrdenacion() {
        onView(withId(R.id.menuQuitarFiltrosYOrdenaciones)).check(matches(isDisplayed()));
        onView(withId(R.id.menuQuitarFiltrosYOrdenaciones)).perform(click());

        onView(withText("ACEPTAR"))
                .inRoot(isDialog())
                .perform(click());

        assertThrows(NoMatchingViewException.class, () -> {
            onView(withId(R.id.menuQuitarFiltrosYOrdenaciones)).perform(click());
        });

    }

    private void verificarListaOriginal() {

        DataInteraction g1 = onData(CoreMatchers.anything()).inAdapterView(withId(R.id.lvStations)).atPosition(0);
        g1.onChildView(withId(R.id.tvAddress)).check(matches(withText("CARRETERA 6316 KM. 10,5")));

        DataInteraction g2 = onData(CoreMatchers.anything()).inAdapterView(withId(R.id.lvStations)).atPosition(1);
        g2.onChildView(withId(R.id.tvAddress)).check(matches(withText("CR N-629 79,7")));

        DataInteraction g3 = onData(CoreMatchers.anything()).inAdapterView(withId(R.id.lvStations)).atPosition(2);
        g3.onChildView(withId(R.id.tvAddress)).check(matches(withText("CARRETERA N-611 KM. 163,2")));

        DataInteraction g4 = onData(CoreMatchers.anything()).inAdapterView(withId(R.id.lvStations)).atPosition(3);
        g4.onChildView(withId(R.id.tvAddress)).check(matches(withText("CARRETERA CASTILLO SIETEVILLAS KM.")));
    }

    private void aplicarFiltro() {
        onView(withId(R.id.menuFiltrar)).perform(click());
        onView(withId(R.id.spinnerCombustible)).perform(click());
        onData(allOf(is(instanceOf(TipoCombustible.class)), is(TipoCombustible.valueOf(GASOLEO_A))))
                .inRoot(isPlatformPopup())
                .perform(click());
        onView(withId(R.id.etPrecioMax)).perform(clearText(), typeText("1.5"));

        Espresso.closeSoftKeyboard();
        onView(withId(R.id.btnFiltrar)).perform(click());
    }

    private void aplicarOrdenacion() {
        onView(withId(R.id.menuOrdenar)).perform(click());
        onView(withId(R.id.spinnerPtosInteres)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is(PUNTO_INTERES)))
                .inRoot(isPlatformPopup())
                .perform(click());
        onView(withId(R.id.btnOrdenar)).perform(click());
    }
}