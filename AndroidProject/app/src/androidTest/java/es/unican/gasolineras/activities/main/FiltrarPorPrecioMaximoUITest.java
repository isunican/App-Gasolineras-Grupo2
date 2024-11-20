package es.unican.gasolineras.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static es.unican.gasolineras.model.TipoCombustible.GASOLEO_A;
import static es.unican.gasolineras.utils.MockRepositories.getTestRepository;

import android.content.Context;
import android.view.View;

import androidx.test.espresso.DataInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import dagger.hilt.android.testing.BindValue;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.UninstallModules;
import es.unican.gasolineras.R;
import es.unican.gasolineras.injection.RepositoriesModule;
import es.unican.gasolineras.model.TipoCombustible;
import es.unican.gasolineras.repository.IGasolinerasRepository;

@UninstallModules(RepositoriesModule.class)
@HiltAndroidTest
public class FiltrarPorPrecioMaximoUITest {
    @Rule(order = 0)  // the Hilt rule must execute first
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule(order = 1)
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<>(MainView.class);

    // I need the context to access resources, such as the json with test gas stations
    final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    // Mock repository that provides data from a JSON file instead of downloading it from the internet.
    @BindValue
    public final IGasolinerasRepository repository = getTestRepository(context, R.raw.gasolineras_ccaa_06);

    View decorView;

    @Before
    public void inicializa(){
        activityRule.getScenario().onActivity(activity -> decorView = activity.getWindow().getDecorView());
    }

    @Test
    public void testFiltarGasolinerasPorPrecioMaximoCasoExito() {

        //clicka en filtrar
        onView(withId(R.id.menuFiltrar)).perform(click());

        //clicka en el selector de combustible
        onView(withId(R.id.spinnerCombustible)).perform(click());

        //elige la opcion gasoleo A
        onData(allOf(is(instanceOf(TipoCombustible.class)),
                is(GASOLEO_A))).inRoot(isPlatformPopup()).perform(click());

        //clicka en el campo de precio máximo
        onView(withId(R.id.etPrecioMax)).perform(click());

        //escribe 1.4 en el campo de precio máximo
        onView(withId(R.id.etPrecioMax)).perform(typeText("1.4"));

        //clicka el boton filtrar
        onView(withId(R.id.btnFiltrar)).perform(click());

        //comprueba la direccion de la primera gasolinera
        DataInteraction g1 = onData(anything()).inAdapterView(withId(R.id.lvStations)).atPosition(0);
        g1.onChildView(withId(R.id.tvAddress)).check(matches(withText("CALLE GUTIERREZ SOLANA 24, 24")));

        //comprueba la direccion de la segunda gasolinera
        DataInteraction g2 = onData(anything()).inAdapterView(withId(R.id.lvStations)).atPosition(1);
        g2.onChildView(withId(R.id.tvAddress)).check(matches(withText("CALLE SANTIAGO-LA LLANADA, 1")));
    }

    @Test
    public void testFiltrarGasolinerasPorPrecioMaximoNoIntroducidoError() throws InterruptedException {

        //clicka en filtrar
        onView(withId(R.id.menuFiltrar)).perform(click());
        //comprueba mensaje de error
        // onView(withText("Por favor, introduce un precio máximo.")).inRoot(RootMatchers.withDecorView(not(decorView))).check(matches(isDisplayed()));
    }

    @Test
    public void testFiltrarGasolinerasPorPrecioMaximoNoNumericoError() throws InterruptedException {

        //clicka en filtrar
        onView(withId(R.id.menuFiltrar)).perform(click());

        //clicka en el campo de precio máximo
        onView(withId(R.id.etPrecioMax)).perform(click());

        //escribe "Uno punto cuatro" en el campo de precio máximo
        onView(withId(R.id.etPrecioMax)).perform(typeText("Uno punto cuatro"));

        //comprueba mensaje de error
        // onView(withText("Por favor, introduce un número válido para el precio máximo.")).inRoot(RootMatchers.withDecorView(not(decorView))).check(matches(isDisplayed()));
    }

    @Test
    public void testFiltrarGasolinerasPorPrecioMaximoNegativoError() throws InterruptedException {

        //clicka en filtrar
        onView(withId(R.id.menuFiltrar)).perform(click());

        //clicka en el campo de precio máximo
        onView(withId(R.id.etPrecioMax)).perform(click());

        //escribe -1.4 en el campo de precio máximo
        onView(withId(R.id.etPrecioMax)).perform(typeText("-1.4"));

        //comprueba mensaje de error
        // onView(withText("Por favor, el precio máximo debe ser positivo.")).inRoot(RootMatchers.withDecorView(not(decorView))).check(matches(isDisplayed()));
    }

    @Test
    public void testPersistenciaDeDatosFiltradosAlReiniciar() throws InterruptedException {
        //clicka en filtrar
        onView(withId(R.id.menuFiltrar)).perform(click());

        //clicka en el selector de combustible
        onView(withId(R.id.spinnerCombustible)).perform(click());

        //elige la opcion gasoleo A
        onData(allOf(is(instanceOf(TipoCombustible.class)),
                is(GASOLEO_A))).inRoot(isPlatformPopup()).perform(click());

        //clicka en el campo de precio máximo
        onView(withId(R.id.etPrecioMax)).perform(click());

        //escribe 1.4 en el campo de precio máximo
        onView(withId(R.id.etPrecioMax)).perform(typeText("1.4"));

        //clicka el botón filtrar
        onView(withId(R.id.btnFiltrar)).perform(click());

        // Cierra y reinicia la actividad (simula reiniciar la app)
        activityRule.getScenario().recreate();

        //clicka en filtrar de nuevo
        onView(withId(R.id.menuFiltrar)).perform(click());

        //verifica que el filtro persiste
        onView(withId(R.id.etPrecioMax)).check(matches(withText("1.4")));
        onView(withId(R.id.spinnerCombustible)).check(matches(withSpinnerText("Gasoleo A")));
    }
    @Test
    public void testCancelacionDelFiltro() throws InterruptedException {
        //clicka en filtrar
        onView(withId(R.id.menuFiltrar)).perform(click());

        //clicka en el selector de combustible
        onView(withId(R.id.spinnerCombustible)).perform(click());

        //elige la opcion gasoleo A
        onData(allOf(is(instanceOf(TipoCombustible.class)),
                is(GASOLEO_A))).inRoot(isPlatformPopup()).perform(click());

        //clicka en el campo de precio máximo
        onView(withId(R.id.etPrecioMax)).perform(click());

        //escribe 1.4 en el campo de precio máximo
        onView(withId(R.id.etPrecioMax)).perform(typeText("1.4"));

        //clicka el botón cancelar
        onView(withId(R.id.btnCancelar)).perform(click());

        //comprueba la direccion de la primera gasolinera
        DataInteraction g1 = onData(anything()).inAdapterView(withId(R.id.lvStations)).atPosition(0);
        g1.onChildView(withId(R.id.tvAddress)).check(matches(withText("CARRETERA 6316 KM. 10,5")));

        //comprueba la direccion de la segunda gasolinera
        DataInteraction g2 = onData(anything()).inAdapterView(withId(R.id.lvStations)).atPosition(1);
        g2.onChildView(withId(R.id.tvAddress)).check(matches(withText("CR N-629 79,7")));
    }

    @Test
    public void testFiltroSinResultados() throws InterruptedException {
        //clicka en filtrar
        onView(withId(R.id.menuFiltrar)).perform(click());

        //clicka en el selector de combustible
        onView(withId(R.id.spinnerCombustible)).perform(click());

        //elige la opcion gasoleo A
        onData(allOf(is(instanceOf(TipoCombustible.class)),
                is(GASOLEO_A))).inRoot(isPlatformPopup()).perform(click());

        //clicka en el campo de precio máximo
        onView(withId(R.id.etPrecioMax)).perform(click());

        //escribe un precio que no tiene coincidencias
        onView(withId(R.id.etPrecioMax)).perform(typeText("0.5"));

        //clicka el botón filtrar
        onView(withId(R.id.btnFiltrar)).perform(click());
    }
}
