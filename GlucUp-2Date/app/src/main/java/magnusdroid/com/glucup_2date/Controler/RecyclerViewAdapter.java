package magnusdroid.com.glucup_2date.Controler;

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
 * Adapter to setup the RecyclerView. This Adapter used to parse the value of the glucose records,
 * identifying the range and select correct properties to show the value
 */
public class RecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private List<ListGluc> items;

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        // UI References
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
        // Got the position of the item and use the class to get the data and send it to TextViews
        viewHolder.value.setText(items.get(i).getValue());
        viewHolder.datetime.setText(items.get(i).getIssued());
        viewHolder.performer.setText(items.get(i).getPerformer());
        viewHolder.state.setText(items.get(i).getState());
        viewHolder.unit.setText(items.get(i).getUnit());
        String color = items.get(i).getCode();
        if (color.equalsIgnoreCase("N") ){
            viewHolder.value.setTextColor(Color.parseColor("#208a18"));
        }else if (color.equalsIgnoreCase("H") ){
            viewHolder.value.setTextColor(Color.parseColor("#bf0d0d"));
        }else if (color.equalsIgnoreCase("L") ){
            viewHolder.value.setTextColor(Color.parseColor("#3875a2"));
        }
    }
}
