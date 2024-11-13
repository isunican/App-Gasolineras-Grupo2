package es.unican.gasolineras.activities.details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import es.unican.gasolineras.R;
import es.unican.gasolineras.model.GasolineraCombustible;

public class CombustibleArrayAdapter extends BaseAdapter {

    private final List<GasolineraCombustible> combustibles;
    private final Context context;


    public CombustibleArrayAdapter(@NonNull Context context, @NonNull List<GasolineraCombustible> combustibles) {
        this.combustibles = combustibles;
        this.context = context;
    }

    @Override
    public int getCount() {
        return combustibles.size();
    }

    @Override
    public Object getItem(int position) {
        return combustibles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.activity_precio_combustible_list_item, parent, false);
        }

            //nombre
            {
                TextView tv = convertView.findViewById(R.id.tvNombreCombustible);
                tv.setText(String.valueOf(combustibles.get(position).getCombustible()));
            }

            //precio
            {
                TextView tv = convertView.findViewById(R.id.tvPrecioCombustible);
                tv.setText(String.valueOf(combustibles.get(position).getPrecio()));
            }


        return convertView;
    }
}
