package es.unican.gasolineras.activities.puntoInteres;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import es.unican.gasolineras.R;
import es.unican.gasolineras.repository.AppDatabase;
import es.unican.gasolineras.repository.DbFunctions;
import es.unican.gasolineras.repository.IPuntosInteresDAO;


/**
 * Actividad que representa la vista para añadir un nuevo punto de interés.
 * Permite al usuario introducir la información del punto de interés (nombre, latitud, longitud)
 * y guardarlo en la base de datos. Implementa la interfaz {@link IAnhadirPuntoInteresContract.View}
 * para interactuar con el presentador {@link AnhadirPuntoInteresPresenter}.
 */
public class AnhadirPuntoInteresView extends AppCompatActivity implements IAnhadirPuntoInteresContract.View {

    private EditText etNombre, etLatitud, etLongitud;
    private Button btnGuardar, btnCancelar;
    private AnhadirPuntoInteresPresenter presentador;

    /**
     * Called when the activity is first created.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anhadir_punto_interes);

        // Inicializar los elementos de la vista
        etNombre = findViewById(R.id.etNombre);
        etLatitud = findViewById(R.id.etLatitud);
        etLongitud = findViewById(R.id.etLongitud);
        btnGuardar = findViewById(R.id.buttonGuardar);
        btnCancelar = findViewById(R.id.buttonCancelar);

        // Inicializar el Presentador
        presentador = new AnhadirPuntoInteresPresenter(this);

        // Acción del botón Guardar
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nombre = obtenerNombre();
                String latitudStr = obtenerLatitud();
                String longitudStr = obtenerLongitud();
                presentador.onGuardarPuntoInteresClicked(nombre, latitudStr, longitudStr);
            }
        });

        // Acción del botón Cancelar
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Cerrar la actividad si se cancela
            }
        });
    }

    /**
     * Muestra un mensaje al usuario.
     * @param mensaje El mensaje a mostrar.
     */
    @Override
    public void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }

    /**
     * Obtiene el nombre del punto de interés.
     * @return El nombre del punto de interés.
     */
    @Override
    public String obtenerNombre() {
        return etNombre.getText().toString();
    }

    /**
     * Obtiene la latitud del punto de interés.
     * @return La latitud del punto de interés.
     */
    @Override
    public String obtenerLatitud() {
        return etLatitud.getText().toString();
    }

    /**
     * Obtiene la longitud del punto de interés.
     * @return La longitud del punto de interés.
     */
    @Override
    public String obtenerLongitud() {
        return etLongitud.getText().toString();
    }

    /**
     * Cierra la actividad después de guardar el punto de interes.
     */
    @Override
    public void cerrarVista() {
        finish();
    }

    /**
     * Obtiene el DAO de puntos de interés.
     * @return El DAO de puntos de interés.
     */
    @Override
    public IPuntosInteresDAO getPuntosInteresDAO() {
        AppDatabase db = DbFunctions.generaBaseDatosPuntosInteres(getApplicationContext());
        return db.puntosInteresDao();
    }
}