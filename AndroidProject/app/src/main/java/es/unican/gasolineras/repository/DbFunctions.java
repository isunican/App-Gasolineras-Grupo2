package es.unican.gasolineras.repository;

import android.content.Context;

import androidx.room.Room;

public class DbFunctions {

    /**
     * Constructor privado
     */
    private DbFunctions() {
    }

    // Base de datos de los puntos de interes
    private static AppDatabase db;

    /**
     * Genera la base de datos de puntos de interes
     * @param c contexto de la app
     * @return la base de datos creada
     */
    public static AppDatabase generaBaseDatosPuntosInteres(Context c) {
        if (db == null) {
            db = Room.databaseBuilder(c,
                    AppDatabase.class, "puntos-interes").allowMainThreadQueries().build();
        }
        return db;
    }
}
