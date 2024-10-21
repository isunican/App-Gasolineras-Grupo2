package es.unican.gasolineras.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import es.unican.gasolineras.R;
import es.unican.gasolineras.activities.info.InfoView;
import es.unican.gasolineras.activities.details.DetailsView;
import es.unican.gasolineras.model.Gasolinera;
import es.unican.gasolineras.model.PuntoInteres;
import es.unican.gasolineras.repository.AppDatabase;
import es.unican.gasolineras.repository.DbFunctions;
import es.unican.gasolineras.repository.IGasolinerasRepository;
import es.unican.gasolineras.repository.IPuntosInteresDAO;

/**
 * The main view of the application. It shows a list of gas stations.
 */
@AndroidEntryPoint
public class MainView extends AppCompatActivity implements IMainContract.View {

    /** The presenter of this view */
    private MainPresenter presenter;

    /** La base de datos de los puntos de interes */
    private AppDatabase db;
    private IPuntosInteresDAO puntosInteresDAO;

    /** Atributo de la lista de Puntos de Interes */
    private List<PuntoInteres> puntosInteres;

    /** The repository to access the data. This is automatically injected by Hilt in this class */
    @Inject
    IGasolinerasRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // The default theme does not include a toolbar.
        // In this app the toolbar is explicitly declared in the layout
        // Set this toolbar as the activity ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // instantiate presenter and launch initial business logic
        presenter = new MainPresenter();
        presenter.init(this);
    }

    /**
     * This creates the menu that is shown in the action bar (the upper toolbar)
     * @param menu The options menu in which you place your items.
     *
     * @return true because we are defining a new menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * This is called when an item in the action bar menu is selected.
     * @param item The menu item that was selected.
     *
     * @return true if we have handled the selection
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menuItemInfo) {
            presenter.onMenuInfoClicked();
            return true;
        }
        if (itemId == R.id.menuFiltrar) {
            presenter.onMenuFiltrarClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @see IMainContract.View#init()
     */
    @Override
    public void init() {
        // initialize on click listeners (when clicking on a station in the list)
        ListView list = findViewById(R.id.lvStations);
        list.setOnItemClickListener((parent, view, position, id) -> {
            Gasolinera station = (Gasolinera) parent.getItemAtPosition(position);
            presenter.onStationClicked(station);
        });
    }

    /**
     * @see IMainContract.View#getGasolinerasRepository()
     * @return the repository to access the data
     */
    @Override
    public IGasolinerasRepository getGasolinerasRepository() {
        return repository;
    }

    /**
     * @see IMainContract.View#showStations(List) 
     * @param stations the list of charging stations
     */
    @Override
    public void showStations(List<Gasolinera> stations) {
        ListView list = findViewById(R.id.lvStations);
        GasolinerasArrayAdapter adapter = new GasolinerasArrayAdapter(this, stations);
        list.setAdapter(adapter);
    }

    /**
     * @see IMainContract.View#showLoadCorrect(int)
     * @param stations
     */
    @Override
    public void showLoadCorrect(int stations) {
        Toast.makeText(this, "Cargadas " + stations + " gasolineras", Toast.LENGTH_SHORT).show();
    }

    /**
     * @see IMainContract.View#showLoadError()
     */
    @Override
    public void showLoadError() {
        Toast.makeText(this, "Error cargando las gasolineras", Toast.LENGTH_SHORT).show();
    }

    /**
     * @see IMainContract.View#showStationDetails(Gasolinera)
     * @param station the charging station
     */
    @Override
    public void showStationDetails(Gasolinera station) {
        Intent intent = new Intent(this, DetailsView.class);
        intent.putExtra(DetailsView.INTENT_STATION, Parcels.wrap(station));
        startActivity(intent);
    }

    /**
     * @see IMainContract.View#showInfoActivity()
     */
    @Override
    public void showInfoActivity() {
        Intent intent = new Intent(this, InfoView.class);
        startActivity(intent);
    }

    @Override
    public void getPuntosInteresDAO() {
        db = DbFunctions.generaBaseDatosPuntosInteres(getApplicationContext());
        puntosInteresDAO = db.puntosInteresDao();
    }

    @Override
    public void showPopUpFiltrar() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainView.this);
        LayoutInflater inflater = MainView.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.puntos_interes_dialog_layout, null);

        // Referencio el spinner
        Spinner spiner = dialogView.findViewById(R.id.spinnerPtosInteres);

        // Creo el adapter del spinner
        puntosInteres = puntosInteresDAO.getAll();

        // Crear un array de Strings para almacenar los nombres de los puntos de interés
        String[] arraySpinner = new String[puntosInteres.size()];
        // Llenar el array con los nombres de los puntos de interés
        for (int i = 0; i < puntosInteres.size(); i++) {
            arraySpinner[i] = puntosInteres.get(i).nombre;  // Asegúrate de tener un getter getNombre() en PuntoInteres
        }

        // Relleno el spinner con la lista de los puntos de interes
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainView.this, android.R.layout.simple_spinner_item, arraySpinner);
        spiner.setAdapter(adapter);

        // Creo el alert y muestro el dialog
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Referenciar el botón "Ordenar" y "Cancelar"
        View btnOrdenar = dialogView.findViewById(R.id.btnOrdenar);
        View btnCancelar = dialogView.findViewById(R.id.btnCancelar);

        // Listener para el botón "Cancelar"
        btnCancelar.setOnClickListener(v -> {
            dialog.dismiss();
        });

        // Listener para el botón "Ordenar"
        btnOrdenar.setOnClickListener(v -> {
            // Obtener el punto de interés seleccionado del spinner
            int selectedPosition = spiner.getSelectedItemPosition();
            if (selectedPosition != -1) {  // Verificar que se haya seleccionado un punto de interés
                PuntoInteres puntoSeleccionado = puntosInteres.get(selectedPosition);
                // Llamar al método onOrdenarClicked pasando el PuntoInteres seleccionado
                onOrdenarClicked(puntoSeleccionado);
                // Cerrar el popup
                dialog.dismiss();
            } else {
                Toast.makeText(MainView.this, "Selecciona un punto de interés", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onOrdenarClicked(PuntoInteres p) {
        presenter.ordenarGasolinerasCercanasPtoInteres(p);
    }
}