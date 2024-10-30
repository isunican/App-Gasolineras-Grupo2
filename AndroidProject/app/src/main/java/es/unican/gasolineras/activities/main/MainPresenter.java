package es.unican.gasolineras.activities.main;

import java.util.ArrayList;
import java.util.List;

import es.unican.gasolineras.model.Gasolinera;
import es.unican.gasolineras.model.IDCCAAs;
import es.unican.gasolineras.model.PuntoInteres;
import es.unican.gasolineras.model.TipoCombustible;
import es.unican.gasolineras.repository.ICallBack;
import es.unican.gasolineras.repository.IGasolinerasRepository;

/**
 * The presenter of the main activity of the application. It controls {@link MainView}
 */
public class MainPresenter implements IMainContract.Presenter {

    /** The view that is controlled by this presenter */
    private IMainContract.View view;

    /** Atributo lista gasolineras */
    List<Gasolinera> gasolineras;
    List<Gasolinera> gasolinerasMod;

    /**
     * @see IMainContract.Presenter#init(IMainContract.View)
     * @param view the view to control
     */
    @Override
    public void init(IMainContract.View view) {
        this.view = view;
        this.view.init();
        load();
    }

    /**
     * @see IMainContract.Presenter#onStationClicked(Gasolinera)
     * @param station the station that has been clicked
     */
    @Override
    public void onStationClicked(Gasolinera station) {
        view.showStationDetails(station);
    }

    /**
     * @see IMainContract.Presenter#onMenuInfoClicked()
     */
    @Override
    public void onMenuInfoClicked() {
        view.showInfoActivity();
    }

    /**
     * @see IMainContract.Presenter#onMenuAnhadirPuntoInteresClicked()
     */
    public void onMenuAnhadirPuntoInteresClicked() {
        view.showAnhadirPuntoInteresActivity();
    }
    /**
     * Muestra el popup de ordenar
     */
    public void onMenuOrdenarClicked() {
        view.showPopUpOrdenar();
    }

    /**
     * Muestra el popup de filtrar
     */
    public void onMenuFiltrarClicked() {
        view.showPopUpFiltar();
    }



    /**
     * Loads the gas stations from the repository, and sends them to the view
     */
    private void load() {
        IGasolinerasRepository repository = view.getGasolinerasRepository();

        ICallBack callBack = new ICallBack() {

            @Override
            public void onSuccess(List<Gasolinera> stations) {
                gasolineras = stations;
                view.showStations(stations);
                view.showLoadCorrect(stations.size());
                //Inicializo la lista que se modifica
                gasolinerasMod = new ArrayList<>(gasolineras);
            }

            @Override
            public void onFailure(Throwable e) {
                view.showLoadError();
                view.showLoadError();
            }
        };
        view.getPuntosInteresDAO();
        repository.requestGasolineras(callBack, IDCCAAs.CANTABRIA.id);
    }



    /**
     * Muestra la lista de gasolineras ordenadas por el punto de interes
     * @param p el punto de interes
     */
    public void ordenarGasolinerasCercanasPtoInteres(PuntoInteres p) {
        GasolineraDistanciaComparator comparator = new GasolineraDistanciaComparator(p);
        gasolinerasMod.sort(comparator);
        view.showStations(gasolinerasMod);
    }

    public void filtraGasolinerasPorPrecioMaximo(double precioMax, TipoCombustible combustible) {
        // Crear una lista temporal para almacenar las gasolineras que cumplen con los criterios
        List<Gasolinera> gasolinerasFiltradas = new ArrayList<>();

        // Iterar sobre cada gasolinera en la lista original
        for (Gasolinera gasolinera : gasolinerasMod) {
            double precioCombustible = combustible.getPrecio(gasolinera);
            // Verificar si la gasolinera tiene el tipo de combustible deseado
            if (precioCombustible > 0.0) {
                // Comprobar si el precio del combustible es menor o igual al precio máximo
                if (precioCombustible <= precioMax) {
                    gasolinerasFiltradas.add(gasolinera);
                }
            }
        }

        // Actualizar la lista original con las gasolineras filtradas
        gasolinerasMod.clear(); // Limpiar la lista original
        gasolinerasMod.addAll(gasolinerasFiltradas); // Añadir las gasolineras que cumplen

        // Mostrar las gasolineras filtradas
        view.showStations(gasolinerasMod);
    }
}
