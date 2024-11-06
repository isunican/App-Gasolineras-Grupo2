package es.unican.gasolineras.activities.common;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static es.unican.gasolineras.utils.Matchers.withTextColor;
import static es.unican.gasolineras.utils.MockRepositories.getTestRepository;

import android.content.Context;

import androidx.test.espresso.DataInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import dagger.hilt.android.testing.BindValue;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.UninstallModules;
import es.unican.gasolineras.R;
import es.unican.gasolineras.activities.main.MainView;
import es.unican.gasolineras.common.TimeProvider;
import es.unican.gasolineras.injection.RepositoriesModule;
import es.unican.gasolineras.repository.IGasolinerasRepository;

@UninstallModules(RepositoriesModule.class)
@HiltAndroidTest
public class MostrarHorarioGasolineraUITest {
    @Rule(order = 0)  // the Hilt rule must execute first
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);
    @Rule(order = 1)
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<>(MainView.class);

    // I need the context to access resources, such as the json with test gas stations
    final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    // Mock repository that provides data from a JSON file instead of downloading it from the internet.
    @BindValue
    public final IGasolinerasRepository repository = getTestRepository(context, R.raw.gasolineras_prueba_horarios);

    @Mock
    private static TimeProvider mockObtenerDiaHora;

    @Before
    public void inicializa() {
        //inicializo los mocks
        MockitoAnnotations.openMocks(this);

        when(mockObtenerDiaHora.obtenerDiaHora()).thenReturn(LocalDateTime.of(2024, 10, 30, 18, 0, 0));
    }

    @Test
    public void test24H() {
        //comprueba la direccion de la primera gasolinera
        DataInteraction g1 = onData(anything()).inAdapterView(withId(R.id.lvStations)).atPosition(0);
        g1.onChildView(withId(R.id.tvAbiertoCerrado)).check(matches(withText("Abierto")));
        g1.onChildView(withId(R.id.tvHorarioGasolinera)).check(matches(withText("(24H)")));
//        g1.onChildView(withId(R.id.tvHorarioGasolinera)).check(matches(withTextColor(R.color.verde)));
    }

    @Test
    public void testAbiertaSimple() {
        //comprueba la direccion de la primera gasolinera
        DataInteraction g1 = onData(anything()).inAdapterView(withId(R.id.lvStations)).atPosition(1);
        g1.onChildView(withId(R.id.tvAbiertoCerrado)).check(matches(withText("Abierto")));
        g1.onChildView(withId(R.id.tvHorarioGasolinera)).check(matches(withText("(08:00-21:00)")));
//        g1.onChildView(withId(R.id.tvHorarioGasolinera)).check(matches(withTextColor(R.color.verde)));
    }

    @Test
    public void testAbiertaIntervalo() {
        //comprueba la direccion de la primera gasolinera
        DataInteraction g1 = onData(anything()).inAdapterView(withId(R.id.lvStations)).atPosition(2);
        g1.onChildView(withId(R.id.tvAbiertoCerrado)).check(matches(withText("Abierto")));
        g1.onChildView(withId(R.id.tvHorarioGasolinera)).check(matches(withText("(09:00-14:00 y 16:00-21:00)")));
//        g1.onChildView(withId(R.id.tvHorarioGasolinera)).check(matches(withTextColor(R.color.verde)));
    }

    @Test
    public void testCerradaTodoElDia() {
        //comprueba la direccion de la primera gasolinera
        DataInteraction g1 = onData(anything()).inAdapterView(withId(R.id.lvStations)).atPosition(3);
        g1.onChildView(withId(R.id.tvAbiertoCerrado)).check(matches(withText("Cerrado")));
        g1.onChildView(withId(R.id.tvHorarioGasolinera)).check(matches(withText("(Todo el día)")));
//        g1.onChildView(withId(R.id.tvHorarioGasolinera)).check(matches(withTextColor(R.color.rojo)));
    }

    @Test
    public void testCerradaParcial() {
        //comprueba la direccion de la primera gasolinera
        DataInteraction g1 = onData(anything()).inAdapterView(withId(R.id.lvStations)).atPosition(4);
        g1.onChildView(withId(R.id.tvAbiertoCerrado)).check(matches(withText("Cerrado")));
        g1.onChildView(withId(R.id.tvHorarioGasolinera)).check(matches(withText("(08:00-14:00 y 19:00-21:00)")));
//        g1.onChildView(withId(R.id.tvHorarioGasolinera)).check(matches(withTextColor(R.color.rojo)));
    }

    @Test
    public void testSinDetallesDeHorario() throws InterruptedException {
        //comprueba la direccion de la primera gasolinera
        DataInteraction g1 = onData(anything()).inAdapterView(withId(R.id.lvStations)).atPosition(5);
        g1.onChildView(withId(R.id.tvAbiertoCerrado)).check(matches(withText("")));
        g1.onChildView(withId(R.id.tvHorarioGasolinera)).check(matches(withText("(Sin detalles de horario)")));
    }

}
