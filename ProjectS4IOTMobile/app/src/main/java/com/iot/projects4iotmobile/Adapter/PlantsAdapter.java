package com.iot.projects4iotmobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.iot.projects4iotmobile.Controller.DetailPlant;
import com.iot.projects4iotmobile.Model.Plant;
import com.iot.projects4iotmobile.R;
import java.util.LinkedList;


public class PlantsAdapter  extends RecyclerView.Adapter<PlantsAdapter.MyViewHolder> {
    private LinkedList<Plant> plants;
    private Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public PlantsAdapter(LinkedList<Plant> plants, Context context) {
        this.plants = new LinkedList<Plant>() ;
        this.plants.addAll(plants);
        this.context=context;
    }

    //   for search in list contact
    public  void setFilteredContacts(LinkedList<Plant> filteredContacts){
        this.plants=filteredContacts;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.plantitemlayout,
                        parent, false);
        MyViewHolder vh = new MyViewHolder(itemLayoutView );
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.plant=plants.get(position);

// - get element from your dataset at this position
// - replace the contents of the view with that element
        holder.identification.setText(plants.get(position).getName());
// Reference to an image file in Cloud Storage
        StorageReference storageReference =
                FirebaseStorage.getInstance().getReferenceFromUrl(plants.get(position).getImage());
// Download directly from StorageReference using Glide
// (See MyAppGlideModule for Loader registration)
        Glide.with(context /* context */)
                .load(storageReference)
                .into(holder.photo);
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return plants.size();
    }
    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
    public  class MyViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {
        Plant plant;
        public TextView identification_phone;
        public TextView identification;
        public ImageView photo;
        // Context is a reference to the activity that contain the the recycler view
        public MyViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            identification =itemLayoutView.findViewById(R.id.identification);
            photo= itemLayoutView.findViewById(R.id.plant_photo);
            itemLayoutView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Intent intent= new Intent(context, DetailPlant.class);
            intent.putExtra("plant",plant);
            context.startActivity(intent);
        }

    }

}
