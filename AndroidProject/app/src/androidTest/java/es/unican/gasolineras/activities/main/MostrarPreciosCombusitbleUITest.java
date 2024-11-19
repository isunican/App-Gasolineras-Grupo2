package es.unican.gasolineras.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollCompletelyTo;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.hasToString;
import static java.lang.Thread.sleep;
import static java.util.EnumSet.allOf;
import static es.unican.gasolineras.utils.MockRepositories.getTestRepository;

import android.app.Activity;
import android.content.Context;
import android.widget.ListView;

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
import es.unican.gasolineras.model.GasolineraCombustible;
import es.unican.gasolineras.repository.IGasolinerasRepository;

@UninstallModules(RepositoriesModule.class)
@HiltAndroidTest
public class MostrarPreciosCombusitbleUITest {
    @Rule(order = 0)  // The Hilt rule must execute first
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);
    @Rule(order = 1)
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<>(MainView.class);

    // I need the context to access resources, such as the json with test gas stations
    final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    // Mock repository that provides data from a JSON file instead of downloading it from the internet.
    @BindValue
    public final IGasolinerasRepository repository = getTestRepository(context, R.raw.pruebas_interfaz_mostrar_precios_combustible);

    @Before
    public void setUp() {
        hiltRule.inject();
    }

    @Test
    public void testMostrarTodosLosPreciosCompleto() {

        // Me guardo la gasolinera en una varibale para usarla para comprobar los precios.
        DataInteraction gasolineraCompleta = onData(anything()).inAdapterView(withId(R.id.lvStations)).atPosition(0);

        // Accedo a la informacion de la primera gasolinera.
        gasolineraCompleta.perform(click());

        //Compruebo que la lista de combustibles tiene 14 elementos.
        onView(withId(R.id.lvCombustibles)).check(matches(hasChildCount(14)));

        /*
        En los test de android studio si el elemento de la lista no aparece en pantalla no comprueba ese elemento
        Por lo que si no pongo el scroll el test se queda en la pantalla que aparece cuando entras en la primera
        Gasolinera y no avanza. He probado muchas cosas con bucle for swipeUp pero la unica manera en la que me
        A funcionado ha sido esta. Lo que hago es hacer un scroll hasta gasoleo Premium que es mas o menos la mitad
        de la lista para que compruebe del elemento 1 al 8. Luego hago otro scroll hasta el final para que compruebe
        todas. Al final de la lista uso un swipeUp para que baje una ultima vez y acabe el test porque sin esa linea
        el test no detecta que ha llegado al final no acaba la pantalla y se queda esperando.
         */
        onView(withText("Gasoleo Premium")).perform(scrollTo());

        // Compruebo los nombres y precios de cada combustible.

        //Biodiesel
        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(0)
                .onChildView(withId(R.id.tvNombreCombustible))
                .check(matches(withText("Biodiesel")));

        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(0)
                .onChildView(withId(R.id.tvPrecioCombustible))
                .check(matches(withText("1.1")));

        //Bioetanol
        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(1)
                .onChildView(withId(R.id.tvNombreCombustible))
                .check(matches(withText("Bioetanol")));

        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(1)
                .onChildView(withId(R.id.tvPrecioCombustible))
                .check(matches(withText("1.2")));

        //Gas natural comprimido
        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(2)
                .onChildView(withId(R.id.tvNombreCombustible))
                .check(matches(withText("Gnc")));

        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(2)
                .onChildView(withId(R.id.tvPrecioCombustible))
                .check(matches(withText("1.3")));

        //Gas natural licuado
        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(3)
                .onChildView(withId(R.id.tvNombreCombustible))
                .check(matches(withText("Gnl")));

        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(3)
                .onChildView(withId(R.id.tvPrecioCombustible))
                .check(matches(withText("1.4")));

        //Gases licuados del petroleo
        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(4)
                .onChildView(withId(R.id.tvNombreCombustible))
                .check(matches(withText("Glp")));

        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(4)
                .onChildView(withId(R.id.tvPrecioCombustible))
                .check(matches(withText("1.5")));

        //Gasoleo A
        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(5)
                .onChildView(withId(R.id.tvNombreCombustible))
                .check(matches(withText("Gasoleo A")));

        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(5)
                .onChildView(withId(R.id.tvPrecioCombustible))
                .check(matches(withText("1.6")));

        //Gasoleo B
        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(6)
                .onChildView(withId(R.id.tvNombreCombustible))
                .check(matches(withText("Gasoleo B")));

        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(6)
                .onChildView(withId(R.id.tvPrecioCombustible))
                .check(matches(withText("1.7")));

        // Scroll para avanzar hasta el final.
        onView(withText("Hidrogeno")).perform(scrollTo());

        //Gasoleo Premium
        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(7)
                .onChildView(withId(R.id.tvNombreCombustible))
                .check(matches(withText("Gasoleo Premium")));

        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(7)
                .onChildView(withId(R.id.tvPrecioCombustible))
                .check(matches(withText("1.8")));

        //Gasolina 95 E10
        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(8)
                .onChildView(withId(R.id.tvNombreCombustible))
                .check(matches(withText("Gasolina 95 E10")));

        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(8)
                .onChildView(withId(R.id.tvPrecioCombustible))
                .check(matches(withText("1.9")));

        //Gasolina 95 E5
        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(9)
                .onChildView(withId(R.id.tvNombreCombustible))
                .check(matches(withText("Gasolina 95 E5")));

        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(9)
                .onChildView(withId(R.id.tvPrecioCombustible))
                .check(matches(withText("2.0")));

        //Gasolina 95 E5 Premium
        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(10)
                .onChildView(withId(R.id.tvNombreCombustible))
                .check(matches(withText("Gasolina 95 E5 Premium")));

        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(10)
                .onChildView(withId(R.id.tvPrecioCombustible))
                .check(matches(withText("2.1")));

        //Gasolina 98 E10
        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(11)
                .onChildView(withId(R.id.tvNombreCombustible))
                .check(matches(withText("Gasolina 98 E10")));

        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(11)
                .onChildView(withId(R.id.tvPrecioCombustible))
                .check(matches(withText("2.2")));

        //Gasoleo 98 E5
        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(12)
                .onChildView(withId(R.id.tvNombreCombustible))
                .check(matches(withText("Gasolina 98 E5")));

        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(12)
                .onChildView(withId(R.id.tvPrecioCombustible))
                .check(matches(withText("2.3")));

        // Bajo hasta que la barra toca con el final para acabar el test.
        onView(withId(R.id.lvCombustibles)).perform(swipeUp());

        //Hidrogeno
        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(13)
                .onChildView(withId(R.id.tvNombreCombustible))
                .check(matches(withText("Hidrogeno")));

        gasolineraCompleta.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(13)
                .onChildView(withId(R.id.tvPrecioCombustible))
                .check(matches(withText("2.4")));

    }

    @Test
    public void testMostrarTodosLosPreciosParcial() {
        // Me guardo la gasolinera en una varibale para usarla para comprobar los precios.
        DataInteraction gasolineraParcial = onData(anything()).inAdapterView(withId(R.id.lvStations)).atPosition(1);

        // Accedo a la informacion de la primera gasolinera.
        gasolineraParcial.perform(click());

        //Compruebo que la lista de combustibles tiene 6 elementos.
        onView(withId(R.id.lvCombustibles)).check(matches(hasChildCount(6)));

        //En este caso con un solo scroll vale porque ya se muestran todos los elementos a la vez.
        onView(withId(R.id.lvCombustibles)).perform(scrollTo());

        // Compruebo los nombres y precios de cada combustible.

        //Biodiesel
        gasolineraParcial.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(0)
                .onChildView(withId(R.id.tvNombreCombustible))
                .check(matches(withText("Biodiesel")));

        gasolineraParcial.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(0)
                .onChildView(withId(R.id.tvPrecioCombustible))
                .check(matches(withText("1.1")));

        //Gas natural comprimido
        gasolineraParcial.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(1)
                .onChildView(withId(R.id.tvNombreCombustible))
                .check(matches(withText("Gnc")));

        gasolineraParcial.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(1)
                .onChildView(withId(R.id.tvPrecioCombustible))
                .check(matches(withText("1.3")));

        //Gasoleo A
        gasolineraParcial.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(2)
                .onChildView(withId(R.id.tvNombreCombustible))
                .check(matches(withText("Gasoleo A")));

        gasolineraParcial.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(2)
                .onChildView(withId(R.id.tvPrecioCombustible))
                .check(matches(withText("1.6")));


        //Gasoleo B
        gasolineraParcial.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(3)
                .onChildView(withId(R.id.tvNombreCombustible))
                .check(matches(withText("Gasoleo B")));

        gasolineraParcial.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(3)
                .onChildView(withId(R.id.tvPrecioCombustible))
                .check(matches(withText("1.7")));

        //Gasoleo Premium

        gasolineraParcial.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(4)
                .onChildView(withId(R.id.tvNombreCombustible))
                .check(matches(withText("Gasoleo Premium")));

        gasolineraParcial.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(4)
                .onChildView(withId(R.id.tvPrecioCombustible))
                .check(matches(withText("1.8")));

        //Hidrogeno
        gasolineraParcial.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(5)
                .onChildView(withId(R.id.tvNombreCombustible))
                .check(matches(withText("Hidrogeno")));

        gasolineraParcial.inAdapterView(withId(R.id.lvCombustibles))
                .atPosition(5)
                .onChildView(withId(R.id.tvPrecioCombustible))
                .check(matches(withText("2.4")));
    }

    @Test
    public void testMostrarTodosLosPreciosVacios() {
        // Me guardo la gasolinera en una varibale para usarla para comprobar los precios.
        DataInteraction gasolineraVacia = onData(anything()).inAdapterView(withId(R.id.lvStations)).atPosition(2);

        // Accedo a la informacion de la primera gasolinera.
        gasolineraVacia.perform(click());

        //Compruebo que la lista de combustibles tiene 0 elementos.
        onView(withId(R.id.lvCombustibles)).check(matches(hasChildCount(0)));

    }


}
