package es.unican.gasolineras.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import es.unican.gasolineras.model.PuntoInteres;

@Dao
public interface IPuntosInteresDAO {
    @Query("SELECT * FROM PuntoInteres")
    List<PuntoInteres> getAll();

    // Buscar por id
    @Query("SELECT * FROM PuntoInteres WHERE idPuntoInteres IN (:puntoInteresId)")
    PuntoInteres loadById(int puntoInteresId);

    // Buscar por nombre
    @Query("SELECT * FROM PuntoInteres WHERE nombre IN (:nombrePuntoInteres)")
    PuntoInteres loadByName(String nombrePuntoInteres);

    // Buscar por latitud y longitud exactas
    @Query("SELECT * FROM PuntoInteres WHERE latitud = :lat AND longitud = :lon")
    PuntoInteres loadByLatLon(double lat, double lon);

    @Insert
    void insertAll(PuntoInteres... puntoInteres);

    @Delete
    void delete(PuntoInteres puntoInteres);

    @Query("DELETE FROM PuntoInteres")
    void deleteAll();
}