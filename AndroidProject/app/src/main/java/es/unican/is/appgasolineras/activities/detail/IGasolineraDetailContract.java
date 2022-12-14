package es.unican.is.appgasolineras.activities.detail;

import android.content.Context;

import java.util.Map;

import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.repository.IPromocionesRepository;

/**
 * La actividad Detalle esta compuesta por un presentador y una vista, que deben presentar
 * los metodos definidos en las siguientes interfaces.
 *
 * @author Grupo 02-CarbuRed
 * @version 1.0
 */
public interface IGasolineraDetailContract {

    /**
     * Un presentador para la actividad Detalle debe implementar esta funcionalidad.
     */
    interface Presenter {
        /**
         * Metodo de inicializacion.
         */
        void init();

        /**
         * Metodo de respuesta ante aceptar la alerta que indica que no se han podido cargar
         * los detalles de una gasolinera.
         * Debe retornar a la vista principal.
         */
        void onAcceptClicked();

    }

    /**
     * Una vista para la actividad Detalle debe implementar esta funcionalidad.
     */
    interface View {
        /**
         * Metodo de inicializacion.
         */
        void init();

        /**
         * Muestra informacion detallada de una gasolinera especifica.
         */
        void showInfo(Map<String, String> info);

        /**
         * Muestra una alerta informando de que ha habido un error intentando cargar la informacion
         * especifica de una gasolinera.
         */
        void showLoadError();

        /**
         * Abre la vista principal.
         */
        void openMainView();

        /**
         * Muestra descuentos si existiesen.
         * @param discountedSummaryPrice Precio sumario con descuento.
         * @param discountedDieselPrice Precio diesel con descuento.
         * @param discounted95OctanesPrice Precio gasolina con descuento.
         */
        void loadDiscount(String discountedSummaryPrice, String discountedDieselPrice,
                          String discounted95OctanesPrice);

        /**
         * Retorna el contexto de la actividad.
         * @return contexto de la actividad.
         */
        Context getContext();

        /**
         * Obtiene repositorio de promociones.
         * @return repositorio de promociones.
         */
        IPromocionesRepository getPromocionesRepository();

        /**
         * Obtiene repositorio de gasolineras.
         * @return repostiorio de gasolineras.
         */
        IGasolinerasRepository getGasolinerasRepository();
    }
}
