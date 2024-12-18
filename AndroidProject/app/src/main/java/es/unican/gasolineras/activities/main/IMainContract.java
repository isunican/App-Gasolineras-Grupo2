package es.unican.gasolineras.activities.main;

import java.util.List;

import es.unican.gasolineras.model.Gasolinera;
import es.unican.gasolineras.model.PuntoInteres;
import es.unican.gasolineras.model.TipoCombustible;
import es.unican.gasolineras.repository.IGasolinerasRepository;

/**
 * The Presenter-View contract for the Main activity.
 * The Main activity shows a list of gas stations.
 */
public interface IMainContract {

    /**
     * Methods that must be implemented in the Main Presenter.
     * Only the View should call these methods.
     */
    public interface Presenter {

        /**
         * Links the presenter with its view.
         * Only the View should call this method
         * @param view
         */
        public void init(View view);

        /**
         * The presenter is informed that a gas station has been clicked
         * Only the View should call this method
         * @param station the station that has been clicked
         */
        public void onStationClicked(Gasolinera station);

        /**
         * The presenter is informed that the Info item in the menu has been clicked
         * Only the View should call this method
         */
        public void onMenuInfoClicked();

        /**
         * The presenter is informed that the Filtrar item in the menu has been clicked
         */
        public void onMenuAnhadirPuntoInteresClicked();

        /**
         * Ordena las gasolineras en proximidad con el punto de interes seleccionado.
         * @param p punto de interes deseado
         */
        public void ordenarGasolinerasCercanasPtoInteres(PuntoInteres p);

        /**
         * Filtra las gasolineras por el precio maximo y por el tipo de combustible deseados.
         * @param precioMax precio maximo para filtrar
         * @param combustible tipo de combustible por el que filtrar
         */
        public void filtraGasolinerasPorPrecioMaximo(double precioMax, TipoCombustible combustible);

        /**
         * Muestra el popup de quitar filtros y ordenaciones.
         */
        public void onMenuQuitarFiltrosYOrdenacionesClicked();

        /**
         * Quita los filtros y ordenaciones aplicados a la lista de gasolineras.
         */
        public void quitarFiltrosYOrdenaciones();
    }

    /**
     * Methods that must be implemented in the Main View.
     * Only the Presenter should call these methods.
     */
    public interface View {

        /**
         * Initialize the view. Typically this should initialize all the listeners in the view.
         * Only the Presenter should call this method
         */
        public void init();

        /**
         * Returns a repository that can be called by the Presenter to retrieve gas stations.
         * This method must be located in the view because Android resources must be accessed
         * in order to instantiate a repository (for example Internet Access). This requires
         * dependencies to Android. We want to keep the Presenter free of Android dependencies,
         * therefore the Presenter should be unable to instantiate repositories and must rely on
         * the view to create the repository.
         * Only the Presenter should call this method
         * @return
         */
        public IGasolinerasRepository getGasolinerasRepository();

        /**
         * The view is requested to display the given list of gas stations.
         * Only the Presenter should call this method
         * @param stations the list of charging stations
         */
        public void showStations(List<Gasolinera> stations);

        /**
         * The view is requested to display a notification indicating  that the gas
         * stations were loaded correctly.
         * Only the Presenter should call this method
         * @param stations
         */
        public void showLoadCorrect(int stations);

        /**
         * The view is requested to display a notificacion indicating that the gas
         * stations were not loaded correctly.
         * Only the Presenter should call this method
         */
        public void showLoadError();

        /**
         * The view is requested to display the detailed view of the given gas station.
         * Only the Presenter should call this method
         * @param station the charging station
         */
        public void showStationDetails(Gasolinera station);

        /**
         * The view is requested to open the info activity.
         * Only the Presenter should call this method
         */
        public void showInfoActivity();

        /**
         * The view is requested to open the AñadirPuntoInteres activity.
         * Only the Presenter should call this method
         */
        public void getPuntosInteresDAO();

        /**
         *  La vista manda una peticion al presenter para que muestre el
         *  popup de ordenar.
         */
        public void showPopUpOrdenar();

        /**
         * Informa al presenter que el boton de filtrar ha sido clickado.
         * @param p punto de interes como referencia
         */
        public void onOrdenarClicked(PuntoInteres p);

        /**
         * Le manda una peticion al presenter para que muestre el popup
         * de anhadir un punto de interes.
         */
        public void showAnhadirPuntoInteresActivity();

        /**
         *  La vista manda una peticion al presenter para que muestre el menu de
         *  confirmacion para quitar filtros y ordenaciones.
         */
        public void showPopUpQuitarFiltrosYOrdenaciones();

        /**
         * Informa al presenter que el boton de filtrar ha sido pulsado.
         * @param precioMax precio maximo puesto por el usuario
         * @param combustible combustible seleccionado por el usuario
         */
        public void onFiltrarClicked(double precioMax, TipoCombustible combustible);

        /**
         *  La vista manda una peticion al presenter para que muestre el
         *  popup de filtrar.
         */
        public void showPopUpFiltar();

        /**
         *  La vista manda una peticion al presenter para que muestre un
         *  elemento informativo
         */
        public void showElementoInformativo();
    }
}
