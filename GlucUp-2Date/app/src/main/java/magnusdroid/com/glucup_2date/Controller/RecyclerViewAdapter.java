package magnusdroid.com.glucup_2date.Controller;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import magnusdroid.com.glucup_2date.Model.ListGluc;
import magnusdroid.com.glucup_2date.R;

/**
 * Creado por Hermosa Programación
 */
public class RecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private List<ListGluc> items;

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView value;
        public TextView datetime;
        public TextView performer;
        public TextView state;
        public TextView unit;

        public RecyclerViewHolder(View v) {
            super(v);
            value = (TextView) v.findViewById(R.id.value);
            datetime = (TextView) v.findViewById(R.id.datetime);
            performer = (TextView) v.findViewById(R.id.performer);
            state = (TextView) v.findViewById(R.id.state);
            unit = (TextView) v.findViewById(R.id.unite);
        }
    }

    public RecyclerViewAdapter(List<ListGluc> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.glucose_list, viewGroup, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, int i) {
        // Pedimos la posición
        //resultp = data.get(position);
        // Capturamos la posición y pasamos los datos a los TextViews
        viewHolder.value.setText(items.get(i).getValue());
        //holder.value.setText(resultp.get("value"));
        viewHolder.datetime.setText(items.get(i).getIssued());
        //holder.datetime.setText(resultp.get("issued"));
        viewHolder.performer.setText(items.get(i).getPerformer());
        //holder.performer.setText(resultp.get("performer"));
        viewHolder.state.setText(items.get(i).getState());
        //holder.state.setText(resultp.get("state"));
        viewHolder.unit.setText(items.get(i).getUnit());
        //holder.unit.setText(resultp.get("unite"));
        // Pasamos la bandera de la URL de la imagen a ImageLoader.class
        //imageLoader.DisplayImage(resultp.get(TopTenFragment.FLAG), holder.imagen);
        String color = items.get(i).getCode();
        if (color.equalsIgnoreCase("N") ){
            viewHolder.value.setTextColor(Color.parseColor("#208a18"));
        }else if (color.equalsIgnoreCase("H") ){
            viewHolder.value.setTextColor(Color.parseColor("#bf0d0d"));
        }else if (color.equalsIgnoreCase("L") ){
            viewHolder.value.setTextColor(Color.parseColor("#3875a2"));
        }
        /*viewHolder.imagen.setImageResource(items.get(i).getImagen());
        viewHolder.nombre.setText(items.get(i).getNombre());
        viewHolder.visitas.setText("Visitas:"+String.valueOf(items.get(i).getVisitas()));*/
    }
}
