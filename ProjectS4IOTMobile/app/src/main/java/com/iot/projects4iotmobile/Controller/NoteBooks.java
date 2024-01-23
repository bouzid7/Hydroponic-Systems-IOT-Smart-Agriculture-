package com.iot.projects4iotmobile.Controller;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.iot.projects4iotmobile.Adapter.NotesAdapter;
import com.iot.projects4iotmobile.Dao.PlantsDao;
import com.iot.projects4iotmobile.Model.Note;
import com.iot.projects4iotmobile.R;
import java.util.LinkedList;


public class NoteBooks extends AppCompatActivity implements View.OnClickListener  {

    // creating a variable for our list view,
    // arraylist and firebase Firestore.
    ListView listNotes;
    ImageView add_note;
    ImageView back ;
    ImageView favorites,favorites_false;
    PlantsDao plantsDao;
    LinkedList<Note> notes;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notebooks);

        favorites=(ImageView)findViewById(R.id.favorites);
        favorites_false=(ImageView)findViewById(R.id.favorites_false);
        favorites_false=(ImageView)findViewById(R.id.favorites_false);

        favorites_false.setOnClickListener(this);
        favorites.setOnClickListener(this);

        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(this);

        add_note=(ImageView) findViewById(R.id.add_notebook);
        add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteBooks.this, AddNote.class);
                startActivity(intent);
            }
        });
        // below line is use to initialize our variables
        listNotes = findViewById(R.id.idNotes);

    }

    @Override
    protected void onResume() {
        super.onResume();
          getNotes();
    }

    @SuppressLint("StaticFieldLeak")
    private void getNotes() {


        new AsyncTask() {
            //exécuter des tâches avant le démarrage du thread
            //on a encore le droit d’accéder au thread principal du Gui
            protected void onPreExecute() {
                notes = new LinkedList<Note>();
                plantsDao= new PlantsDao(NoteBooks.this);
                showProgressDialog();
            }

            //La tâches principale du thread
            //on a pas droit d‘accéder au composantes du thread principal du  GUI
            protected Object doInBackground(Object[] objects) {
                notes.addAll(plantsDao.getAllNotesBooks());
                return null;
            }

            ////exécuter des taches pendant la réalisation de la tâche principale du thread
            //on a encore le droit d’accéder au thread principal du Gui
            protected void onProgressUpdate(Integer... progress) {

            }

            //exécuter des taches après la terminaison du thread courant
            ////on a encore le droit d’accéder au thread principal du Gui
            protected void onPostExecute(Object result) {
                NotesAdapter adapter = new NotesAdapter(NoteBooks.this, notes);
                listNotes.setAdapter(adapter);
                hideProgressDialog();
            }
        }.execute();

    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }



    @SuppressLint("StaticFieldLeak")
    private void getFavorites(){

        Drawable drawable1 = getResources().getDrawable(R.drawable.favorite);
        favorites.setImageDrawable(drawable1);
        favorites_false.setVisibility(View.GONE);
        favorites.setVisibility(View.VISIBLE);

        new AsyncTask() {
            //exécuter des tâches avant le démarrage du thread
            //on a encore le droit d’accéder au thread principal du Gui
            protected void onPreExecute() {
                notes = new LinkedList<Note>();
                plantsDao= new PlantsDao(NoteBooks.this);
                showProgressDialog();
            }

            //La tâches principale du thread
            //on a pas droit d‘accéder au composantes du thread principal du  GUI
            protected Object doInBackground(Object[] objects) {
                notes.addAll(plantsDao.getAllNotesBooksFavorites());
                return null;
            }

            ////exécuter des taches pendant la réalisation de la tâche principale du thread
            //on a encore le droit d’accéder au thread principal du Gui
            protected void onProgressUpdate(Integer... progress) {

            }

            //exécuter des taches après la terminaison du thread courant
            ////on a encore le droit d’accéder au thread principal du Gui
            protected void onPostExecute(Object result) {
                NotesAdapter adapter = new NotesAdapter(NoteBooks.this, notes);
                listNotes.setAdapter(adapter);
                hideProgressDialog();
            }
        }.execute();

    }


    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.back){
           onBackPressed();
        }
        else if(v.getId()==R.id.favorites_false){
            getFavorites();
        }

    }

}
