package es.unican.gasolineras.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static es.unican.gasolineras.utils.MockRepositories.getTestRepository;

import android.content.Context;

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


    @Before
    public void inicializa(){
    }

    @Test
    public void testFiltarGasolinerasPorPrecioMaximoCasoExito() {

        //clicka en filtrar
        onView(withId(R.id.menuFiltrar)).perform(click());

        //clicka en el selector de combustible
        onView(withId(R.id.spinnerCombustible)).perform(click());

        //elige la opcion gasolina 95 E5
        onData(allOf(is(instanceOf(String.class)),
                is("Gasoleo_A"))).inRoot(isPlatformPopup()).perform(click());

        //clicka en el campo de precio máximo
        onView(withId(R.id.etPrecioMax)).perform(click());

        //escribe 1.512 en el campo de precio máximo
        onView(withId(R.id.etPrecioMax)).perform(typeText("1.4"));

        //clicka el boton filtrar
        onView(withId(R.id.btnFiltrar)).perform(click());

        //comprueba la direccion de la primera gasolinera
        DataInteraction g1 = onData(anything()).inAdapterView(withId(R.id.lvStations)).atPosition(0);
        g1.onChildView(withId(R.id.tvAddress)).check(matches(withText("CALLE GUTIERREZ SOLANA 24, 24")));

        //comprueba la direccion de la segunda gasolinera
        DataInteraction g2 = onData(anything()).inAdapterView(withId(R.id.lvStations)).atPosition(1);
        g2.onChildView(withId(R.id.tvAddress)).check(matches(withText("P.I. CROS km CENTRO COMERCIAL M")));
    }

    @Test
    public void testFiltrarGasolinerasPorPrecioMaximoError(){

        //clicka en filtrar
        onView(withId(R.id.menuFiltrar)).perform(click());

        //comprueba mensaje de error
        onView(withId(R.id.tvListaVacia)).
                check(matches(withText("Error: No hay ningun punto de interes añadido")));
    }
}
