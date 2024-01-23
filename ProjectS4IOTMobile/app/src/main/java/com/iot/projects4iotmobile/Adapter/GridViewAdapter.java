package com.iot.projects4iotmobile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.iot.projects4iotmobile.Model.GridDashboard;
import com.iot.projects4iotmobile.R;
import java.util.ArrayList;

public class GridViewAdapter extends ArrayAdapter<GridDashboard>  {

    public GridViewAdapter(@NonNull Context context, ArrayList<GridDashboard> gridDashboardArrayList) {
        super(context, 0, gridDashboardArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.card_item, parent, false);
        }

        GridDashboard gridDashboard = getItem(position);
        TextView courseTV = listitemView.findViewById(R.id.value);
        ImageView courseIV = listitemView.findViewById(R.id.data);
        courseTV.setText(gridDashboard.getData_name());
        courseIV.setImageResource(gridDashboard.getImgid());
        return listitemView;
    }



}
