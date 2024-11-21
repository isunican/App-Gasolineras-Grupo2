package es.unican.gasolineras.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

/**
 * A Gas Station.
 *
 * Properties are defined in the <a href="https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/help/operations/PreciosEESSTerrestres#response-json">API</a>
 *
 * The #SerializedName annotation is a GSON annotation that defines the name of the property
 * as defined in the json response.
 *
 * Getters are automatically generated at compile time by Lombok.
 */
@Parcel
@Getter
@Setter
public class Gasolinera {

    @SerializedName("IDEESS")                       protected String id;

    @SerializedName("Rótulo")                       protected String rotulo;
    @SerializedName("C.P.")                         protected String cp;
    @SerializedName("Dirección")                    protected String direccion;
    @SerializedName("Municipio")                    protected String municipio;

    @SerializedName("Precio Biodiesel")             protected double biodiesel;
    @SerializedName("Precio Bioetanol")             protected double bioetanol;
    @SerializedName("Precio Gas Natural Comprimido") protected double gnc;
    @SerializedName("Precio Gas Natural Licuado")    protected double gnl;
    @SerializedName("Precio Gases licuados del petróleo") protected double glp;
    @SerializedName("Precio Gasoleo A")             protected double gasoleoA;
    @SerializedName("Precio Gasoleo B")             protected double gasoleoB;
    @SerializedName("Precio Gasoleo Premium")       protected double gasoleoPremium;
    @SerializedName("Precio Gasolina 95 E10")       protected double gasolina95E10;
    @SerializedName("Precio Gasolina 95 E5")        protected double gasolina95E5;
    @SerializedName("Precio Gasolina 95 E5 Premium") protected double gasolina95E5Premium;
    @SerializedName("Precio Gasolina 98 E10")       protected double gasolina98E10;
    @SerializedName("Precio Gasolina 98 E5")        protected double gasolina98E5;
    @SerializedName("Precio Hidrogeno")             protected double hidrogeno;

    @SerializedName("Horario")                      protected String horario;

    @SerializedName("Latitud")                      protected double latitud;
    @SerializedName("Longitud (WGS84)")             protected double longitud;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gasolinera that = (Gasolinera) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
