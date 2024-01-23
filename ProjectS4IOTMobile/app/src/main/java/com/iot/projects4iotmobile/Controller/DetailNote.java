package com.iot.projects4iotmobile.Controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.iot.projects4iotmobile.Dao.PlantsDao;
import com.iot.projects4iotmobile.Model.Note;
import com.iot.projects4iotmobile.R;


public class DetailNote extends AppCompatActivity implements View.OnClickListener{

    Note note;
    TextView object_note;
    TextView content_note;
    TextView date_note;
    ImageView edit_note;
    ImageView delete_note;
    ImageView back;
    ImageView favorite;
    ImageView favorite_false;
    PlantsDao plantsDao;
    Context context;
    public DetailNote(){
        this.context = DetailNote.this;
        plantsDao=new PlantsDao(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_note);

        object_note=(TextView)findViewById(R.id.object_note);
        content_note=(TextView)findViewById(R.id.content_note);
        date_note=(TextView)findViewById(R.id.date_note);

        edit_note=(ImageView) findViewById(R.id.edit_note);
        delete_note=(ImageView) findViewById(R.id.delete_note);
        favorite_false=(ImageView) findViewById(R.id.favorite_false);
        favorite=(ImageView) findViewById(R.id.favorite);
        back=(ImageView) findViewById(R.id.back);

        delete_note.setOnClickListener(this);
        favorite_false.setOnClickListener(this);
        favorite.setOnClickListener(this);
        back.setOnClickListener(this);

        object_note.setOnClickListener(this);
        content_note.setOnClickListener(this);
        date_note.setOnClickListener(this);

        note= (Note) getIntent().getSerializableExtra("Note");
        object_note.setText(note.getObjectNote());
        content_note.setText(note.getContentNote());
        date_note.setText(note.getDateNote());

        if (note.getIsFavorite().equals("no")) {
            favorite_false.setVisibility(View.VISIBLE);
            favorite.setVisibility(View.GONE);
        }
        else if (note.getIsFavorite().equals("yes")) {
            favorite_false.setVisibility(View.GONE);
            favorite.setVisibility(View.VISIBLE);
        }

        edit_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get data from input field
                //Pass data to 2nd activity
                Intent intent = new Intent(DetailNote.this, UpdateNote.class);
                intent.putExtra("objectNote", note.getObjectNote());
                intent.putExtra("contentNote", note.getContentNote());
                intent.putExtra("dateNote", note.getDateNote());
                intent.putExtra("isFavorite", note.getIsFavorite());
                startActivity(intent);

            }
        });
    }

    private void deleteNote() {

         plantsDao.DeleteNoteFromNotebooks(note);
        Intent intent2= new Intent(DetailNote.this, NoteBooks.class);
        startActivity(intent2);
        finish();

    }



    private void FavoriteFalse(){

        favorite.setVisibility(View.GONE);
        favorite_false.setVisibility(View.VISIBLE);
        // delete note from favorites in cloud
        plantsDao.FavoritesFalseNoteBooks(note);
    }


    private  void FavoriteTrue(){

        favorite_false.setVisibility(View.GONE);
        favorite.setVisibility(View.VISIBLE);
        // add note to favorites in cloud
        plantsDao.FavoritesTrueNoteBooks(note);
    }


    @Override
    public void onClick(View v) {

        if  (v.getId()==R.id.back){
            Intent intent1= new Intent(this, NoteBooks.class);
            startActivity(intent1);
            finish();
        }

        else if (v.getId()==R.id.delete_note){
            deleteNote();
        }

        else if (v.getId()==R.id.favorite_false){
            FavoriteTrue();
        }

        else if (v.getId()==R.id.favorite){
            FavoriteFalse();
        }

    }

}
