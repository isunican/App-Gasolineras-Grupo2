package es.unican.gasolineras.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static es.unican.gasolineras.common.UtilsHorario.obtenerDiaActual;
import static es.unican.gasolineras.common.UtilsHorario.procesaHorario;
import static es.unican.gasolineras.utils.MockRepositories.getTestRepositoryList;

import android.content.Context;

import androidx.test.espresso.DataInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.testing.BindValue;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.UninstallModules;
import es.unican.gasolineras.R;
import es.unican.gasolineras.common.UtilsHorario;
import es.unican.gasolineras.injection.RepositoriesModule;
import es.unican.gasolineras.model.Gasolinera;
import es.unican.gasolineras.repository.IGasolinerasRepository;

@UninstallModules(RepositoriesModule.class)
@HiltAndroidTest
public class MostrarHorarioGasolineraUITest {
    @Rule(order = 0)  // The Hilt rule must execute first
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);
    @Rule(order = 1)
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<>(MainView.class);

    // I need the context to access resources, such as the json with test gas stations
    final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    private final List<Gasolinera> listaGasolineras = getListaGasolineras();

    // Mock repository that provides data from a JSON file instead of downloading it from the internet.
    @BindValue
    public final IGasolinerasRepository repository = getTestRepositoryList(listaGasolineras);

    public static List<Gasolinera> getListaGasolineras() {
        List<Gasolinera> aux = new ArrayList<>();

        //creo las gasolineras
        Gasolinera gasolinera1 = new Gasolinera();
        gasolinera1.setId("Gasolinera1");
        gasolinera1.setRotulo("REPSOL");
        gasolinera1.setDireccion("Sardinero");
        gasolinera1.setGasoleoA(1.5);
        gasolinera1.setGasolina95E5(1.48);
        gasolinera1.setHorario("L-D: 24H");
        aux.add(gasolinera1);

        //creo las gasolineras
        Gasolinera gasolinera2 = new Gasolinera();
        gasolinera2.setId("Gasolinera2");
        gasolinera2.setRotulo("REPSOL");
        gasolinera2.setDireccion("Sardinero");
        gasolinera2.setGasoleoA(1.5);
        gasolinera2.setGasolina95E5(1.48);
        gasolinera2.setHorario(UtilsHorario.obtenerHorarioAbiertoSimple());
        aux.add(gasolinera2);

        //creo las gasolineras
        Gasolinera gasolinera3 = new Gasolinera();
        gasolinera3.setId("Gasolinera3");
        gasolinera3.setRotulo("REPSOL");
        gasolinera3.setDireccion("Sardinero");
        gasolinera3.setGasoleoA(1.5);
        gasolinera3.setGasolina95E5(1.48);
        gasolinera3.setHorario(UtilsHorario.obtenerHorarioAbiertoIntervalo());
        aux.add(gasolinera3);

        //creo las gasolineras
        Gasolinera gasolinera4 = new Gasolinera();
        gasolinera4.setId("Gasolinera4");
        gasolinera4.setRotulo("REPSOL");
        gasolinera4.setDireccion("Sardinero");
        gasolinera4.setGasoleoA(1.5);
        gasolinera4.setGasolina95E5(1.48);
        gasolinera4.setHorario(UtilsHorario.obtenerHorarioCerradoTodoElDia());
        aux.add(gasolinera4);

        //creo las gasolineras
        Gasolinera gasolinera5 = new Gasolinera();
        gasolinera5.setId("Gasolinera5");
        gasolinera5.setRotulo("REPSOL");
        gasolinera5.setDireccion("Sardinero");
        gasolinera5.setGasoleoA(1.5);
        gasolinera5.setGasolina95E5(1.48);
        gasolinera5.setHorario(UtilsHorario.obtenerHorarioCerradoIntervalo());
        aux.add(gasolinera5);

        //creo las gasolineras
        Gasolinera gasolinera6 = new Gasolinera();
        gasolinera6.setId("Gasolinera6");
        gasolinera6.setRotulo("REPSOL");
        gasolinera6.setDireccion("Sardinero");
        gasolinera6.setGasoleoA(1.5);
        gasolinera6.setGasolina95E5(1.48);
        gasolinera6.setHorario("");
        aux.add(gasolinera6);

        return aux;
    }

    @Before
    public void setUp() {
         hiltRule.inject();
    }

    @Test
    public void test24H() {
        //comprueba la direccion de la primera gasolinera
        DataInteraction g1 = onData(anything()).inAdapterView(withId(R.id.lvStations)).atPosition(0);
        g1.onChildView(withId(R.id.tvAbiertoCerrado)).check(matches(withText("Abierto")));
        g1.onChildView(withId(R.id.tvHorarioGasolinera)).check(matches(withText("(24H)")));
    }

    @Test
    public void testAbiertaSimple() {
        //comprueba la direccion de la primera gasolinera
        DataInteraction g1 = onData(anything()).inAdapterView(withId(R.id.lvStations)).atPosition(1);
        g1.onChildView(withId(R.id.tvAbiertoCerrado)).check(matches(withText("Abierto")));
        g1.onChildView(withId(R.id.tvHorarioGasolinera)).check(matches(withText("(" + procesaHorario(UtilsHorario.obtenerHorarioAbiertoSimple(), obtenerDiaActual(LocalDateTime.now().getDayOfWeek())) + ")")));
    }

    @Test
    public void testAbiertaIntervalo() {
        //comprueba la direccion de la primera gasolinera
        DataInteraction g1 = onData(anything()).inAdapterView(withId(R.id.lvStations)).atPosition(2);
        g1.onChildView(withId(R.id.tvAbiertoCerrado)).check(matches(withText("Abierto")));
        g1.onChildView(withId(R.id.tvHorarioGasolinera)).check(matches(withText("(" + procesaHorario(UtilsHorario.obtenerHorarioAbiertoIntervalo(), obtenerDiaActual(LocalDateTime.now().getDayOfWeek())) + ")")));
    }

    @Test
    public void testCerradaTodoElDia() {
        //comprueba la direccion de la primera gasolinera
        DataInteraction g1 = onData(anything()).inAdapterView(withId(R.id.lvStations)).atPosition(3);
        g1.onChildView(withId(R.id.tvAbiertoCerrado)).check(matches(withText("Cerrado")));
        g1.onChildView(withId(R.id.tvHorarioGasolinera)).check(matches(withText("(Todo el día)")));
    }

    /*
    @Test
    public void testCerradaParcial() {
        //comprueba la direccion de la primera gasolinera
        DataInteraction g1 = onData(anything()).inAdapterView(withId(R.id.lvStations)).atPosition(4);
        g1.onChildView(withId(R.id.tvAbiertoCerrado)).check(matches(withText("Cerrado")));
        g1.onChildView(withId(R.id.tvHorarioGasolinera)).check(matches(withText("(" + procesaHorario(UtilsHorario.obtenerHorarioCerradoIntervalo(), obtenerDiaActual(LocalDateTime.now().getDayOfWeek())) + ")")));
    }

    @Test
    public void testSinDetallesDeHorario() throws InterruptedException {
        //comprueba la direccion de la primera gasolinera
        // Scroll puesto que se sale de la pantalla y no se alcanza a ver
        onView(withId(R.id.lvStations)).perform(swipeUp());
        DataInteraction g1 = onData(anything()).inAdapterView(withId(R.id.lvStations)).atPosition(5);
        g1.onChildView(withId(R.id.tvAbiertoCerrado)).check(matches(withText("")));
        g1.onChildView(withId(R.id.tvHorarioGasolinera)).check(matches(withText("(Sin detalles de horario)")));
    }
     */

}
