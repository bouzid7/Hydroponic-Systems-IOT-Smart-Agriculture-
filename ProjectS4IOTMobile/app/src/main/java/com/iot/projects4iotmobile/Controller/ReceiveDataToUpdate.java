package com.iot.projects4iotmobile.Controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.iot.projects4iotmobile.Dao.PlantsDao;
import com.iot.projects4iotmobile.Model.Plant;
import com.iot.projects4iotmobile.R;


public class ReceiveDataToUpdate extends AppCompatActivity implements View.OnClickListener  {

    Plant oldPlant;
    PlantsDao plantsDao;
    TextInputEditText  nameNewPlant,infoNewPlant;
    ImageView back;
    Button cancelPlant,updatePlant;
    Context context;

    public ReceiveDataToUpdate(){
        this.context=ReceiveDataToUpdate.this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatedataplant);
        nameNewPlant=(TextInputEditText) findViewById(R.id.nameNewPlant);
        infoNewPlant=(TextInputEditText) findViewById(R.id.infoNewPlant);
         //Get Data from Intent
        Intent intent = getIntent();
        oldPlant=new Plant(intent.getStringExtra("name"),intent.getStringExtra("info"),intent.getStringExtra("image"),intent.getStringExtra("isFavorite"));
        //Set data in layout of update
        nameNewPlant.setText( oldPlant.getName());
        infoNewPlant.setText( oldPlant.getInfo());
        //////////////////////////////////////////
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DetailPlant.class);
                startActivity(intent);
                finish();
            }
        });
        updatePlant=(Button) findViewById(R.id.updatePlant);
        updatePlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Plant newPlant=new Plant(nameNewPlant.getText().toString(),infoNewPlant.getText().toString(),oldPlant.getImage(),oldPlant.getIsFavorite());
                // validating the text fields if empty or not.
                if (TextUtils.isEmpty( nameNewPlant.getText().toString()))
                {
                    nameNewPlant.setError("Please enter firstname");
                }
                else if (TextUtils.isEmpty(infoNewPlant.getText().toString()))
                {
                    infoNewPlant.setError("Please enter latsname");
                }
                else
                {
                    plantsDao=new PlantsDao(context);
                    plantsDao.updatePlant(oldPlant,newPlant);
                    Intent intent=new Intent(getApplicationContext(),Plants.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    @Override
    public void onClick(View v) {

    }
}
