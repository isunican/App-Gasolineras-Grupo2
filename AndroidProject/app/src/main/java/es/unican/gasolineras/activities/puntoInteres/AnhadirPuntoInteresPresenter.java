package es.unican.gasolineras.activities.puntoInteres;

import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteException;

import es.unican.gasolineras.model.PuntoInteres;
import es.unican.gasolineras.repository.IPuntosInteresDAO;


/**
 * El presenter que controla la actividad añadir punto interés.
 */
public class AnhadirPuntoInteresPresenter implements IAnhadirPuntoInteresContract.Presenter {

    private IAnhadirPuntoInteresContract.View vista;
    private IPuntosInteresDAO puntosInteresDao;

    /**
     * Constructor.
     * @param vista vista controlada por este presenter.
     */
    public AnhadirPuntoInteresPresenter(IAnhadirPuntoInteresContract.View vista) {
        this.vista = vista;
        this.puntosInteresDao = vista.getPuntosInteresDAO();
    }

    /**
     * Clase que actúa como el presentador en el patrón MVP para la vista de añadir un nuevo punto de interés.
     * Maneja la lógica de negocio relacionada con la validación de datos y el acceso a la base de datos
     * para insertar un nuevo punto de interés.
     */
    public void onGuardarPuntoInteresClicked(String nombre, String latitudStr, String longitudStr) {

        // Validar que los campos no estén vacíos
        if (nombre.isEmpty() || latitudStr.isEmpty() || longitudStr.isEmpty()) {
            vista.mostrarMensaje("Por favor, llene todos los campos");
            return;
        }


        try {
            // Convertir las coordenadas a tipo double
            double latitud = Double.parseDouble(latitudStr);
            double longitud = Double.parseDouble(longitudStr);

            // Verificar que los datos están en rango válido
            if (latitud >= 90.00 || latitud <= -90.00) {
                vista.mostrarMensaje("La latitud está fuera de los límites permitidos. No se ha guardado el punto");
                return;
            }
            if (longitud >= 180.00 || longitud <= -180.00) {
                vista.mostrarMensaje("La longitud está fuera de los límites permitidos. No se ha guardado el punto");
                return;
            }

            // Crear un nuevo punto de interés
            PuntoInteres nuevoPunto = new PuntoInteres(nombre, latitud, longitud);

            // Insertar el nuevo punto de interés en la base de datos
            puntosInteresDao.insertAll(nuevoPunto);

            vista.mostrarMensaje("Punto de interés guardado");
            vista.cerrarVista(); // Cerrar la vista después de guardar

        } catch (NumberFormatException e) {
            vista.mostrarMensaje("Por favor, ingresa valores numéricos válidos para latitud y longitud");
        } catch (SQLiteConstraintException e) {
            vista.mostrarMensaje("Ya existe un punto de interés con ese nombre");
        } catch (SQLiteException e) {
            vista.mostrarMensaje("Ha ocurrido un error en la base de datos");
        }
    }
}