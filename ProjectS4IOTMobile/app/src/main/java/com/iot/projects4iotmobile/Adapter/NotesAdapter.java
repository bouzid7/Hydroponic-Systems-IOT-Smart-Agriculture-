package com.iot.projects4iotmobile.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.iot.projects4iotmobile.Controller.DetailNote;
import com.iot.projects4iotmobile.Model.Note;
import com.iot.projects4iotmobile.R;
import java.util.LinkedList;


public class NotesAdapter extends ArrayAdapter<Note> {

    private Context context;
    // constructor for our list view adapter.
    public  NotesAdapter(@NonNull Context context,  LinkedList<Note> NotesArrayList) {
        super(context, 0, NotesArrayList);
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // below line is use to inflate the
        // layout for our item of list view.
        View listitemView = convertView;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.notes_item_layout, parent, false);
        }

        // after inflating an item of listview item
        // we are getting data from array list inside
        // our modal class.
        Note NoteModal = getItem(position);

        // initializing our UI components of list view item.
        TextView objectNote = listitemView.findViewById(R.id.idNoteObject);
        TextView dateNote = listitemView.findViewById(R.id.idDateNote);
        // ImageView courseIV = listitemView.findViewById(R.id.idIVimage);

        // after initializing our items we are
        // setting data to our view.
        // below line is use to set data to our text view.
        objectNote.setText(NoteModal.getObjectNote());
        dateNote.setText(NoteModal.getDateNote());
        // in below line we are using Picasso to
        // load image from URL in our Image VIew.
        //Picasso.get().load(dataModal.getImgUrl()).into(courseIV);

        // below line is use to add item click listener
        // for our item of list view.
        listitemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // on the item click on our list view.
                // we are displaying a toast message.
                Intent myIntent= new Intent(context, DetailNote.class);
                myIntent.putExtra("Note", NoteModal);
                context.startActivity(myIntent);
            }
        });

        return listitemView;
    }


}
