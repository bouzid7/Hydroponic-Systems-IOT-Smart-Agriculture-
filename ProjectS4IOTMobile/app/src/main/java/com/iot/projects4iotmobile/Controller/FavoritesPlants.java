package com.iot.projects4iotmobile.Controller;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iot.projects4iotmobile.Adapter.PlantsAdapter;
import com.iot.projects4iotmobile.Dao.PlantsDao;
import com.iot.projects4iotmobile.Model.Plant;
import com.iot.projects4iotmobile.Model.User_;
import com.iot.projects4iotmobile.R;
import java.util.LinkedList;


public class FavoritesPlants extends AppCompatActivity implements View.OnClickListener {


    SearchView searchView;
    RecyclerView plantsRecycler;
    PlantsAdapter plantsAdapter;
    LinkedList<Plant> plants;
    ProgressDialog mProgressDialog;
    PlantsDao cda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favoritesplants);
        plantsRecycler = (RecyclerView) findViewById(R.id.favorites_plants);
        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterContacts(newText);
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getFavoritesPlants();
    }

    @SuppressLint("StaticFieldLeak")
    void getFavoritesPlants() {

        new AsyncTask() {
            //exécuter des tâches avant le démarrage du thread
            //on a encore le droit d’accéder au thread principal du Gui
            protected void onPreExecute() {
                plants = new LinkedList<Plant>();
                cda = new PlantsDao(FavoritesPlants.this);
                showProgressDialog();
            }

            //La tâches principale du thread
            //on a pas droit d‘accéder au composantes du thread principal du  GUI
            protected Object doInBackground(Object[] objects) {
                plants.addAll(cda.getFavoritesPlants());
                return null;
            }

            ////exécuter des taches pendant la réalisation de la tâche principale du thread
            //on a encore le droit d’accéder au thread principal du Gui
            protected void onProgressUpdate(Integer... progress) {

            }

            //exécuter des taches après la terminaison du thread courant
            ////on a encore le droit d’accéder au thread principal du Gui
            protected void onPostExecute(Object result) {
                plantsRecycler.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(FavoritesPlants.this);
                plantsRecycler.setLayoutManager(layoutManager);
                plantsAdapter = new PlantsAdapter(plants, FavoritesPlants.this);
                plantsRecycler.setAdapter(plantsAdapter);
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

    public void plantsMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        popup.inflate(R.menu.favorites_menu);
        popup.show();
    }

    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case  R.id.profile:
                return true;
            case R.id.dashboard:
                Intent intent = new Intent(FavoritesPlants.this, Dashboard.class);
                startActivity(intent);
                return true;
            case R.id.back:
                onBackPressed();
                return true;
            case R.id.plants:
                Intent intent1 = new Intent(FavoritesPlants.this,Plants.class);
                startActivity(intent1);
                return true;
            case R.id.notes:
                Intent intentN=new Intent(FavoritesPlants.this,NoteBooks.class);
                startActivity(intentN);
                return true;
            case R.id.logOut:
                PlantsDao pd=new PlantsDao(FavoritesPlants.this);
                pd.LogOut();
                Intent intent2=new Intent(this, MainActivity.class);
                startActivity(intent2);
                finish();
                return true;

            default:
                return false;
        }

    }

    private void filterContacts(String Text) {
        LinkedList<Plant> filteredContacts = new LinkedList<Plant>();

        for (Plant plant : plants) {
            if (plant.getName().toLowerCase().contains(Text.toLowerCase())|| plant.getInfo().toLowerCase().contains(Text.toLowerCase()))

            {
                filteredContacts.add(plant);
            }

        }

        if (filteredContacts.isEmpty()) {
            Toast.makeText(this, "NO DATA FOUND", Toast.LENGTH_SHORT).show();
        }
        else
        {
            plantsAdapter = new PlantsAdapter(plants, getApplicationContext());
            plantsAdapter.setFilteredContacts(filteredContacts);
            plantsRecycler.setAdapter(plantsAdapter);
        }
    }


    @Override
    public void onClick(View v) {

    }

}
