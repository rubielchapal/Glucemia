package magnusdroid.com.glucup_2date.Controler;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import magnusdroid.com.glucup_2date.Model.ListAlarm;
import magnusdroid.com.glucup_2date.R;

/**
 * Created by Dell on 24/07/2016.
 */
public class AlarmAdapter
        extends RecyclerView.Adapter<AlarmAdapter.RecyclerViewHolder> {

    private List<ListAlarm> items;
    private Context context;
    private Intent intent;
    private AlarmManager manager;

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView date;
        public ImageButton remove;

        public RecyclerViewHolder(View v) {
            super(v);
            date = (TextView) v.findViewById(R.id.tv);
            remove = (ImageButton) v.findViewById(R.id.ib_remove);
        }
    }

    public AlarmAdapter(List<ListAlarm> items, Context context, Intent myIntent, AlarmManager alarmManager) {
        this.items = items;
        this.context = context;
        this.intent = myIntent;
        this.manager = alarmManager;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.alarmlist, viewGroup, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder viewHolder, final int i) {
        // Pedimos la posición
        //resultp = data.get(position);
        // Capturamos la posición y pasamos los datos a los TextViews
        viewHolder.date.setText(items.get(i).getDate());
        // Set a click listener for item remove button
        viewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the clicked item label
                String itemLabel = items.get(i).getDate();

                // Remove the item on remove/button click
                items.remove(i);

                Intent intent = new Intent(AlarmClock.ALARM_SEARCH_MODE_TIME);
                intent.putExtra(AlarmClock.EXTRA_HOUR, 10);
                intent.putExtra(AlarmClock.EXTRA_MINUTES, 30);
                context.startActivity(intent);

                /*intent.putExtra("extra", "no");
                intent.putExtra("quote id", String.valueOf(i));
                getActivity().sendBroadcast(intent);

                manager.cancel(pending_intent);

                /*
                    public final void notifyItemRemoved (int position)
                        Notify any registered observers that the item previously located at position
                        has been removed from the data set. The items previously located at and
                        after position may now be found at oldPosition - 1.

                        This is a structural change event. Representations of other existing items
                        in the data set are still considered up to date and will not be rebound,
                        though their positions may be altered.

                    Parameters
                        position : Position of the item that has now been removed
                */
                notifyItemRemoved(i);

                /*
                    public final void notifyItemRangeChanged (int positionStart, int itemCount)
                        Notify any registered observers that the itemCount items starting at
                        position positionStart have changed. Equivalent to calling
                        notifyItemRangeChanged(position, itemCount, null);.

                        This is an item change event, not a structural change event. It indicates
                        that any reflection of the data in the given position range is out of date
                        and should be updated. The items in the given range retain the same identity.

                    Parameters
                        positionStart : Position of the first item that has changed
                        itemCount : Number of items that have changed
                */
                notifyItemRangeChanged(i,items.size());

                // Show the removed item label
                Toast.makeText(context,"Removed : " + itemLabel,Toast.LENGTH_SHORT).show();
            }
        });
        //holder.unit.setText(resultp.get("unite"));
        /*viewHolder.imagen.setImageResource(items.get(i).getImagen());
        viewHolder.nombre.setText(items.get(i).getNombre());
        viewHolder.visitas.setText("Visitas:"+String.valueOf(items.get(i).getVisitas()));*/
    }
}
