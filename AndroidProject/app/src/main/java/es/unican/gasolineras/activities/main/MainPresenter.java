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

    // Banderas para controlar el estado de ordenación y filtrado
    private boolean estaOrdenada = false;
    private boolean estaFiltrada = false;

    // Variables de estado para el último punto de interés de ordenación
    private PuntoInteres puntoInteresOrdenActual = null;

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



    private void load() {
        IGasolinerasRepository repository = view.getGasolinerasRepository();
        ICallBack callBack = new ICallBack() {
            @Override
            public void onSuccess(List<Gasolinera> stations) {
                gasolineras = stations;
                view.showStations(stations);
                view.showLoadCorrect(stations.size());
                gasolinerasMod = new ArrayList<>(gasolineras);
                estaOrdenada = false;
                estaFiltrada = false;
            }

            @Override
            public void onFailure(Throwable e) {
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
        puntoInteresOrdenActual = p;
        estaOrdenada = true;
        gasolinerasMod.sort(new GasolineraDistanciaComparator(p));
        view.showStations(gasolinerasMod);
    }

    /**
     * Filtra la lista de gasolineras por precio máximo y tipo de combustible.
     * Aplica el filtro sobre la lista original y luego la ordena si estaba previamente ordenada.
     * @param precioMax el precio máximo del combustible
     * @param combustible el tipo de combustible
     */
    public void filtraGasolinerasPorPrecioMaximo(double precioMax, TipoCombustible combustible) {
        estaFiltrada = true;

        List<Gasolinera> gasolinerasFiltradas = new ArrayList<>();
        for (Gasolinera gasolinera : gasolineras) {
            double precioCombustible = combustible.getPrecio(gasolinera);
            if (precioCombustible > 0.0 && precioCombustible <= precioMax) {
                gasolinerasFiltradas.add(gasolinera);
            }
        }

        // Si estaba ordenada, aplicar el orden sobre la lista filtrada
        if (estaOrdenada) {
            gasolinerasFiltradas.sort(new GasolineraDistanciaComparator(puntoInteresOrdenActual));
        }

        // Si la lista esta vacia se le agrega el elemento informativo
        if (gasolinerasFiltradas.isEmpty()) {
            view.showElementoInformativo();
        } else {
            // Actualizar la lista modificada y mostrar
            gasolinerasMod = gasolinerasFiltradas;
            view.showStations(gasolinerasMod);
        }
    }
}
