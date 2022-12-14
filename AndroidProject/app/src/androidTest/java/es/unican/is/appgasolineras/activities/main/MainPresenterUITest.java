package es.unican.is.appgasolineras.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

import android.Manifest;

import androidx.test.espresso.DataInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.repository.rest.GasolinerasService;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;
import es.unican.is.appgasolineras.utils.ScreenshotTestRule;

public class MainPresenterUITest {

    @BeforeClass
    public static void setUp() {
        GasolinerasServiceConstants.setStaticURLFiltrarPorLowcost();
    }

    @AfterClass
    public static void clean() {
        GasolinerasService.resetAPI();
        GasolinerasServiceConstants.setMinecoURL();
    }

    // IMPORTANTE: No tiene rule, se incluye en el rule de abajo
    public ActivityScenarioRule<MainView> activityRule =
            new ActivityScenarioRule(MainView.class);

    // Aquí se combinan el ActivityScenarioRule y el ScreenshotTestRule,
    // de forma que la captura de pantalla se haga antes de que se cierre la actividad
    @Rule
    public final TestRule activityAndScreenshotRule = RuleChain
            .outerRule(activityRule)
            .around(new ScreenshotTestRule());

    @Rule
    public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);



    @Test
    public void testFiltrarPorLowcost() {
        // Caso valido: filtro unico por lowcost
        onView(withId(R.id.menuFilter)).perform(click());
        onView((withId(R.id.tvLowcostTitle))).check(matches(withText("Mostrar solo low-cost")));
        onView((withId(R.id.chckLowcost))).perform(click());
        onView(withId(R.id.tvApply)).perform(click());

        /* Comprobacion de la primera gasolinera lowcost.*/
        DataInteraction gasolinera = onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(0);
        gasolinera.onChildView(withId(R.id.tvName)).check(matches(withText("PETROPRIX")));
        gasolinera.onChildView(withId(R.id.tvAddress)).check(matches(withText("CARRETERA 6316 KM. 10,5")));
        gasolinera.onChildView(withId(R.id.tv95Label)).check(matches(withText("Gasolina:")));
        gasolinera.onChildView(withId(R.id.tv95)).check(matches(withText("1,859")));
        gasolinera.onChildView(withId(R.id.tvDieselALabel)).check(matches(withText("Diésel:")));
        gasolinera.onChildView(withId(R.id.tvDieselA)).check(matches(withText("1,999")));

        /* Comprobacion de la segunda gasolinera lowcost.*/
        DataInteraction gasolinera2 = onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(1);
        gasolinera2.onChildView(withId(R.id.tvName)).check(matches(withText("BALLENOIL")));
        gasolinera2.onChildView(withId(R.id.tvAddress)).check(matches(withText("CALLE GUTIERREZ SOLANA 24, 24")));
        gasolinera2.onChildView(withId(R.id.tv95Label)).check(matches(withText("Gasolina:")));
        gasolinera2.onChildView(withId(R.id.tv95)).check(matches(withText("1,699")));
        gasolinera2.onChildView(withId(R.id.tvDieselALabel)).check(matches(withText("Diésel:")));
        gasolinera2.onChildView(withId(R.id.tvDieselA)).check(matches(withText("1,779")));

    }

}
