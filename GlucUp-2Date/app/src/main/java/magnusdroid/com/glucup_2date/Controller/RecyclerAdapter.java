package magnusdroid.com.glucup_2date.Controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import magnusdroid.com.glucup_2date.R;

/**
 * Created by Alejo on 09/12/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.AnimeViewHolder> {
        // Declarar Variables
        private final Context context;
        ArrayList<HashMap<String, String>> data;
        //ImageLoader imageLoader;
        HashMap<String, String> resultp = new HashMap<>();

    public RecyclerAdapter(Context context, ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
        //imageLoader = new ImageLoader(context);
    }

    public static class AnimeViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView value;
        public TextView datetime;
        public TextView performer;
        public TextView state;
        public TextView unit;

        public AnimeViewHolder(View v) {
            super(v);
            value = (TextView) v.findViewById(R.id.value);
            datetime = (TextView) v.findViewById(R.id.datetime);
            performer = (TextView) v.findViewById(R.id.performer);
            state = (TextView) v.findViewById(R.id.state);
            unit = (TextView) v.findViewById(R.id.unite);
        }
    }

    @Override
    public AnimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.glucose_list, parent, false);
        return new AnimeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AnimeViewHolder holder, int position) {
        // Pedimos la posición
        resultp = data.get(position);
        // Capturamos la posición y pasamos los datos a los TextViews
        holder.value.setText(resultp.get("value"));
        holder.datetime.setText(resultp.get("issued"));
        holder.performer.setText(resultp.get("performer"));
        holder.state.setText(resultp.get("state"));
        holder.unit.setText(resultp.get("unite"));
        // Pasamos la bandera de la URL de la imagen a ImageLoader.class
        //imageLoader.DisplayImage(resultp.get(TopTenFragment.FLAG), holder.imagen);
        String color = resultp.get("code");
        if (color.equalsIgnoreCase("N") ){
            holder.value.setTextColor(Color.parseColor("#208a18"));
        }else if (color.equalsIgnoreCase("H") ){
            holder.value.setTextColor(Color.parseColor("#bf0d0d"));
        }else if (color.equalsIgnoreCase("L") ){
            holder.value.setTextColor(Color.parseColor("#3875a2"));
        }
        /*holder.imagen.setImageResource(id);
        holder.imagen.setOnClickListener(clickListener);
        holder.imagen.setTag(holder);
        holder.profile.setOnClickListener(clickListener);
        holder.profile.setTag(holder);
        holder.nombre.setOnClickListener(clickListener);
        holder.nombre.setTag(holder);*/
    }

    /*View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            RecyclerViewHolder holder = (RecyclerViewHolder) view.getTag();
            int position = holder.getPosition();
            resultp = data.get(position);
            Intent intent = new Intent(context, DetailActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("rui", resultp.get("rui"));
            intent.putExtra("owner", resultp.get("owner"));
            intent.putExtra("dni", resultp.get("dni"));
            intent.putExtra("dependency", resultp.get("dependency"));
            intent.putExtra("brand", resultp.get("brand"));
            // Iniciamos la actividad SingleItemView
            context.startActivity(intent);
        }
    };*/

    @Override
    public int getItemCount() {
        return (null != data ? data.size() : 0);
    }
}
