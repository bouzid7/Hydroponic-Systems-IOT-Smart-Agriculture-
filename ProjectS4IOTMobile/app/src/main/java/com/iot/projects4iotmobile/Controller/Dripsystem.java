package com.iot.projects4iotmobile.Controller;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.iot.projects4iotmobile.Dao.DataDao;
import com.iot.projects4iotmobile.R;


public class Dripsystem extends AppCompatActivity implements View.OnClickListener{

     DataDao dataDao;
     ImageView back;
     public Dripsystem(){
         dataDao=new DataDao(Dripsystem.this);
     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dripsystem);
        back=(ImageView)findViewById(R.id.back) ;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        @SuppressLint("UseSwitchCompatOrMaterialCode")
               Switch mSwitch = findViewById(R.id.switch_1);
                // Display Toasts in each of true and false case
                mSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        dataDao.StatusWaterPump("ON");
                        Toast.makeText(Dripsystem.this, "Water pump On", Toast.LENGTH_SHORT).show();
                    } else {
                        dataDao.StatusWaterPump("OFF");
                        Toast.makeText(Dripsystem.this, "Water pump Off", Toast.LENGTH_SHORT).show();
                    }
                });
            }


    @Override
    public void onClick(View v) {

    }

}
