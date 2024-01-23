package com.iot.projects4iotmobile.Controller;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import com.iot.projects4iotmobile.Adapter.GridViewAdapter;
import com.iot.projects4iotmobile.Dao.DataDao;
import com.iot.projects4iotmobile.Dao.PlantsDao;
import com.iot.projects4iotmobile.Model.DashboardData;
import com.iot.projects4iotmobile.Model.GridDashboard;
import com.iot.projects4iotmobile.R;
import java.util.ArrayList;
import java.util.LinkedList;


public class Dashboard extends AppCompatActivity implements View.OnClickListener {


    GridView gridViewData;
    //Context context;
    DashboardData dashboardData;
    GridViewAdapter adapter;
    LinkedList<DashboardData> dashboardDataAll;
    ProgressDialog mProgressDialog;
    DataDao cda;
    PlantsDao plantsDao;


    public Dashboard(){
        plantsDao=new PlantsDao(Dashboard.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        gridViewData =(GridView)findViewById(R.id.gridViewData);
        gridViewData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                final Intent intent;
                switch(position)
                {
                    case 5:
                        intent =  new Intent(getApplicationContext(), Dripsystem.class);
                        break;

                    default:
                        intent =  new Intent(getApplicationContext(), Dashboard.class);
                        break;
                }
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
          getData();
    }

    //Using Dao (MVC)
    @SuppressLint("StaticFieldLeak")
    void getData(){

        new AsyncTask() {

            protected void onPreExecute(){
                dashboardDataAll= new LinkedList<DashboardData>();
                cda= new DataDao(Dashboard.this);
                showProgressDialog();
            }

            protected Object doInBackground(Object[] objects) {
                dashboardDataAll.addAll(cda.getAllDataDao());
                return null;
            }

            protected void onProgressUpdate(Integer... progress) {

            }

            protected void onPostExecute(Object result) {
                ArrayList<GridDashboard> courseModelArrayList = new ArrayList<GridDashboard>();
                courseModelArrayList.add(new GridDashboard(dashboardDataAll.getFirst().getPh(), R.drawable.meters));
                courseModelArrayList.add(new GridDashboard(dashboardDataAll.getFirst().getTemp(), R.drawable.celsius));
                courseModelArrayList.add(new GridDashboard(dashboardDataAll.getFirst().getHum(), R.drawable.humidity));
                courseModelArrayList.add(new GridDashboard(dashboardDataAll.getFirst().getPpm(), R.drawable.t1));
                courseModelArrayList.add(new GridDashboard(dashboardDataAll.getFirst().getLevelWater(), R.drawable.waterlevel));
                courseModelArrayList.add(new GridDashboard("", R.drawable.smartfarm));
                GridViewAdapter adapter = new GridViewAdapter(getApplicationContext(),courseModelArrayList);
                gridViewData.setAdapter(adapter);
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



          //Using normal arachitecture

    /*private void getData() {

        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if (task.isSuccessful()){

                    if (task.getResult().exists()){

                        Toast.makeText(getApplicationContext(),"Successfully Read",Toast.LENGTH_SHORT).show();
                        DataSnapshot dataSnapshot = task.getResult();
                        String ph = String.valueOf(dataSnapshot.child("ph").getValue());

                        dashboardData=new DashboardData(
                                String.valueOf(dataSnapshot.child("ph").getValue()),
                                String.valueOf(dataSnapshot.child("temperature").getValue()),
                                String.valueOf(dataSnapshot.child("humidity").getValue()),
                                String.valueOf(dataSnapshot.child("ppm").getValue()),
                                String.valueOf(dataSnapshot.child("levelWater").getValue())
                        );

                        ArrayList<GridDashboard> courseModelArrayList = new ArrayList<GridDashboard>();
                        courseModelArrayList.add(new GridDashboard(dashboardData.getPh(), R.drawable.meters));
                        courseModelArrayList.add(new GridDashboard(dashboardData.getTemp(), R.drawable.celsius));
                        courseModelArrayList.add(new GridDashboard(dashboardData.getHum(), R.drawable.humidity));
                        courseModelArrayList.add(new GridDashboard(dashboardData.getPpm(), R.drawable.t1));
                        courseModelArrayList.add(new GridDashboard(dashboardData.getLevelWater(), R.drawable.waterlevel));
                        courseModelArrayList.add(new GridDashboard("", R.drawable.smartfarm));
                        GridViewAdapter adapter = new GridViewAdapter(getApplicationContext(),courseModelArrayList);
                        gridViewData.setAdapter(adapter);

                    }else {

                        Toast.makeText(getApplicationContext(),"User Doesn't Exist",Toast.LENGTH_SHORT).show();

                    }


                }else {

                    Toast.makeText(getApplicationContext(),"Failed to read",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }*/


    public void listDashboard(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        popup.inflate(R.menu.dashboard_menu);
        popup.show();
    }

    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case  R.id.allPlants:
                Intent intent0=new Intent(Dashboard.this, Plants.class);
                startActivity(intent0);
                return true;
            case R.id.greenhouseData:
                Intent intent1=new Intent(Dashboard.this,Dashboard.class);
                startActivity(intent1);
                return true;
            case R.id.favorites:
                Intent intent2=new Intent(Dashboard.this,FavoritesPlants.class);
                startActivity(intent2);
                return true;
            case R.id.notebooks:
                Intent intentN=new Intent(Dashboard.this,NoteBooks.class);
                startActivity(intentN);
                return true;
            case R.id.logOut:
                plantsDao.LogOut();
                Intent intent3=new Intent(this, MainActivity.class);
                startActivity(intent3);
                return true;

            default:
                return false;
        }

    }


    @Override
    public void onClick(View v) {


    }
}
