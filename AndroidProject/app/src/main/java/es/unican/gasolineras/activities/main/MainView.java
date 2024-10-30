package es.unican.gasolineras.activities.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
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
import es.unican.gasolineras.activities.puntoInteres.AnhadirPuntoInteresView;
import es.unican.gasolineras.model.Gasolinera;
import es.unican.gasolineras.model.PuntoInteres;
import es.unican.gasolineras.model.TipoCombustible;
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
        if (itemId == R.id.menuOrdenar) {
            presenter.onMenuOrdenarClicked();
            return true;
        }
        if (itemId == R.id.menuItemAnhadirPuntoInteres) {
            presenter.onMenuAnhadirPuntoInteresClicked();
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

    /**
     * @see IMainContract.View#showAnhadirPuntoInteresActivity()
     */
    @Override
    public void getPuntosInteresDAO() {
        db = DbFunctions.generaBaseDatosPuntosInteres(getApplicationContext());
        puntosInteresDAO = db.puntosInteresDao();
    }

    @Override
    public void showPopUpOrdenar() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainView.this);
        LayoutInflater inflater = MainView.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.puntos_interes_dialog_layout, null);

        // Referencio el spinner
        Spinner spinner = dialogView.findViewById(R.id.spinnerPtosInteres);
        TextView tvListaVacia = dialogView.findViewById(R.id.tvListaVacia);  // Referencia al TextView para el mensaje de lista vacía
        View btnOrdenar = dialogView.findViewById(R.id.btnOrdenar);  // Referencia al botón "Ordenar"
        View btnCancelar = dialogView.findViewById(R.id.btnCancelar);  // Referencia al botón "Cancelar"

        // Obtengo la lista de puntos de interés
        puntosInteres = puntosInteresDAO.getAll();

        if (puntosInteres.isEmpty()) {
            // Si la lista está vacía, mostrar el mensaje y deshabilitar el botón "Ordenar"
            ((View) tvListaVacia).setVisibility(View.VISIBLE);
            btnOrdenar.setEnabled(false);  // Deshabilitar el botón "Ordenar"
        } else {
            // Si la lista no está vacía, ocultar el mensaje y habilitar el botón "Ordenar"
            tvListaVacia.setVisibility(View.GONE);
            btnOrdenar.setEnabled(true);  // Habilitar el botón "Ordenar"

            // Crear un array de Strings para almacenar los nombres de los puntos de interés
            String[] arraySpinner = new String[puntosInteres.size()];
            // Llenar el array con los nombres de los puntos de interés
            for (int i = 0; i < puntosInteres.size(); i++) {
                arraySpinner[i] = puntosInteres.get(i).nombre;
            }

            // Relleno el spinner con la lista de los puntos de interés
            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainView.this, android.R.layout.simple_spinner_item, arraySpinner);
            spinner.setAdapter(adapter);
        }

        // Creo el alert y muestro el dialog
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Listener para el botón "Cancelar"
        btnCancelar.setOnClickListener(v -> {
            dialog.dismiss();
        });

        // Listener para el botón "Ordenar"
        btnOrdenar.setOnClickListener(v -> {
            // Obtener el punto de interés seleccionado del spinner
            int selectedPosition = spinner.getSelectedItemPosition();
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

    @Override
    public void showAnhadirPuntoInteresActivity() {
        Intent intent = new Intent(this, AnhadirPuntoInteresView.class);
        startActivity(intent);
    }

    @Override
    public void onFiltrarClicked(double precioMax, TipoCombustible combustible) {
        presenter.filtraGasolinerasPorPrecioMaximo(precioMax, combustible);
    }

    @Override
    public void showPopUpFiltar() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainView.this);
        LayoutInflater inflater = MainView.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.filtrar_precio_max_dialog_layout, null);

        // Referencio el spinner
        Spinner spinner = dialogView.findViewById(R.id.spinnerCombustible);
        EditText etMaxPrice = dialogView.findViewById(R.id.etPrecioMax);
        View btnFiltrar = dialogView.findViewById(R.id.btnFiltrar);  // Referencia al botón "Filtrar"
        View btnCancelar = dialogView.findViewById(R.id.btnCancelar);  // Referencia al botón "Cancelar"

        // Llenar el spinner con los valores del enum TipoCombustible
        ArrayAdapter<TipoCombustible> adapter = new ArrayAdapter<>(
                MainView.this,
                android.R.layout.simple_spinner_item,
                TipoCombustible.values()
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Recuperar SharedPreferences para obtener el último combustible y precio
        SharedPreferences sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
        String lastSelectedFuelType = sharedPreferences.getString("lastFuelType", null);
        String lastMaxPriceTxt = sharedPreferences.getString("lastMaxPrice", "-1");
        double lastMaxPrice = Double.parseDouble(lastMaxPriceTxt);


        // Establecer el valor del EditText y el Spinner si hay preferencias guardadas
        if (lastSelectedFuelType != null) {
            int spinnerPosition = adapter.getPosition(TipoCombustible.valueOf(lastSelectedFuelType));
            spinner.setSelection(spinnerPosition);
        }
        if (lastMaxPrice != -1) {
            etMaxPrice.setText(String.valueOf(lastMaxPrice));
        }

        // Creo el alert y muestro el dialog
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Listener para el botón "Cancelar"
        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        // Listener para el botón "Filtrar"
        btnFiltrar.setOnClickListener(v -> {
            String maxPriceText = etMaxPrice.getText().toString();
            if (maxPriceText.isEmpty()) {
                Toast.makeText(MainView.this, "Por favor, introduce un precio máximo.", Toast.LENGTH_SHORT).show();
            } else {

                // Obtener el tipo de combustible seleccionado del spinner
                TipoCombustible combustible = (TipoCombustible) spinner.getSelectedItem();

                // Guardar el último combustible seleccionado y el precio máximo en SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("lastFuelType", combustible.name());
                editor.putString("lastMaxPrice", maxPriceText);
                editor.apply();

                // Llamar al método onFiltrarClicked pasando el tipo de combustible y el precio máximo
                double precioMax = Float.parseFloat(maxPriceText);
                onFiltrarClicked(precioMax, combustible);

                // Cerrar el popup
                dialog.dismiss();
            }
        });
    }

}