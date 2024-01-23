package com.iot.projects4iotmobile.Dao;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iot.projects4iotmobile.Model.DashboardData;
import java.util.LinkedList;


public class DataDao {
     FirebaseDatabase firebaseDatabase;
     DatabaseReference databaseReference;
      Context context;
    public DataDao(Context context1){
        firebaseDatabase = FirebaseDatabase.getInstance();
        this.context=context1;
    }

     // on doit s'assurer que le chargement des donnees est terminé avant de retourner
    //  le resultast final

    public LinkedList<DashboardData> getAllDataDao() {
        LinkedList<DashboardData> data = new LinkedList<DashboardData>();
        final boolean[] terminated = new boolean[1];
        terminated[0] = false;

        DatabaseReference reference = firebaseDatabase.getReference().child("Data");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DashboardData dashboardData = new DashboardData("7.5", snapshot.child("Temperature").getValue().toString(), snapshot.child("Humidity").getValue().toString(), snapshot.child("TDS_meter_ppm").getValue().toString(), "250");
                    data.add(dashboardData);
                }
                terminated[0] = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        while (!terminated[0]) {
            System.out.println("Loading...");
            try {
                Thread.sleep(100); // Add a small delay to avoid a busy loop
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    // ass other methods of CRUD
    public void StatusWaterPump(String status) {

        databaseReference = firebaseDatabase.getReference("waterPump");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child("status_water_pump").setValue(status);
                // after adding this data we are showing toast message.
                Toast.makeText(context, "data added", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(context, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
