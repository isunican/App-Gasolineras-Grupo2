package es.unican.gasolineras.activities.main;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
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
import androidx.test.espresso.Espresso;
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
import es.unican.gasolineras.repository.AppDatabase;
import es.unican.gasolineras.repository.DbFunctions;
import es.unican.gasolineras.repository.IGasolinerasRepository;
import es.unican.gasolineras.repository.IPuntosInteresDAO;

@UninstallModules(RepositoriesModule.class)
@HiltAndroidTest
public class OrdenarGasolineraCercanasUITest {

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
        //estado inicial base de datos Ptos Interes vacia
        AppDatabase db = DbFunctions.generaBaseDatosPuntosInteres(getApplicationContext());
        IPuntosInteresDAO ptDAO = db.puntosInteresDao();
        //ptDAO.deleteAll();
    }

    @Test
    public void testOrdenaGasolinerasCercanasCasoExito() {
        //creo un punto de interes

        openActionBarOverflowOrOptionsMenu(context);
        onView((withText("Añadir Punto interés"))).perform(click());
        onView(withId(R.id.etNombre)).perform(click());
        onView(withId(R.id.etNombre)).perform(typeText("casa"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.etLatitud)).perform(click());
        onView(withId(R.id.etLatitud)).perform(typeText("43.3089400"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.etLongitud)).perform(click());
        onView(withId(R.id.etLongitud)).perform(typeText("-4.2301600"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.buttonGuardar)).perform(click());

        //clicka en filtrar
        onView(withId(R.id.menuOrdenar)).perform(click());

        //clicka en el selector de Pto Interes
        onView(withId(R.id.spinnerPtosInteres)).perform(click());

        //elige la opcion casa
        onData(allOf(is(instanceOf(String.class)),
                is("casa"))).inRoot(isPlatformPopup()).perform(click());

        //clicka el boton ordenar
        onView(withId(R.id.btnOrdenar)).perform(click());

        //comprueba la direccion de la primera gasolinera
        DataInteraction g1 = onData(anything()).inAdapterView(withId(R.id.lvStations)).atPosition(0);
        g1.onChildView(withId(R.id.tvAddress)).check(matches(withText("CALLE CORREOS, SN")));

        //comprueba la direccion de la segunda gasolinera
        DataInteraction g2 = onData(anything()).inAdapterView(withId(R.id.lvStations)).atPosition(1);
        g2.onChildView(withId(R.id.tvAddress)).check(matches(withText("CARRETERA 634 KM. 244")));
    }

    @Test
    public void OrdenarGasolinerasCercanasNoPtoInteres(){

        //clicka en filtrar
        onView(withId(R.id.menuOrdenar)).perform(click());

        //comprueba mensaje de error
        onView(withId(R.id.tvListaVacia)).
                check(matches(withText("Error: No hay ningun punto de interes añadido")));
    }
}


