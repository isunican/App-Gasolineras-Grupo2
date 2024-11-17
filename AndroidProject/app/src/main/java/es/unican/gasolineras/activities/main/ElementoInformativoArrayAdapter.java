package es.unican.gasolineras.activities.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import es.unican.gasolineras.R;

public class ElementoInformativoArrayAdapter extends BaseAdapter {
    /** Context of the application */
    private final Context context;

    public ElementoInformativoArrayAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.no_gasolineras_list_item, parent, false);
        }

        // texto
        {
            TextView tv = convertView.findViewById(R.id.tvInfoError);
            tv.setText("El precio es demasiado restrictivo. No hay gasolineras que lo cumplan.");
        }

        return convertView;
    }
}
