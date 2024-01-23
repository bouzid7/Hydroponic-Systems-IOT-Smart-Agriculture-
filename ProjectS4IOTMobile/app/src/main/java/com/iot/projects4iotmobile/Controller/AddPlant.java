package com.iot.projects4iotmobile.Controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.iot.projects4iotmobile.Dao.PlantsDao;
import com.iot.projects4iotmobile.Model.Plant;
import com.iot.projects4iotmobile.R;


public class AddPlant extends AppCompatActivity implements View.OnClickListener{

    TextInputEditText namePlant,infoPlant;
    ImageView back;
    Button add_newPlant,cancel_newPlant;
    PlantsDao plantsDao;
   // Context context;
    ImageView imagePlant;
    public  Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addplant);
        namePlant=(TextInputEditText) findViewById(R.id.namePlant);
        infoPlant=(TextInputEditText) findViewById(R.id.infoPlant);
          back=(ImageView) findViewById(R.id.back);
        add_newPlant=(Button) findViewById(R.id.add_newPlant);
        cancel_newPlant=(Button) findViewById(R.id.cancel_newPlant);
        imagePlant=(ImageView)findViewById(R.id.imagePlant) ;
        add_newPlant.setOnClickListener(this);
        cancel_newPlant.setOnClickListener(this);
        back.setOnClickListener(this);

        imagePlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent.createChooser(intent,"title"),1);*/
                ChoosePicture();

            }
        });

    }

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            Uri  uri = data.getData();
            contentImage.setImageURI(uri);
        }
    }*/

    private  void ChoosePicture(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imagePlant.setImageURI(imageUri);

            plantsDao= new PlantsDao(AddPlant.this);
            plantsDao.uploadPicture(imageUri);
        }
    }

    private  void AddNewPlant() {
        String name = namePlant.getText().toString();
        String info = infoPlant.getText().toString();
       // String image = imagePlant.getImageMatrix().toString();
        Plant plant= new Plant(name, info,"gs://projets4greenhouse.appspot.com/salade.jpg","no");
         plantsDao= new PlantsDao(AddPlant.this);
         plantsDao.insertDataPlant(plant);
         Intent intent= new Intent(AddPlant.this,Plants.class);
         startActivity(intent);
         finish();
    }



    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.add_newPlant) {
            AddNewPlant();
        }

        else if(v.getId()==R.id.cancel_newPlant) {
            namePlant.setText("");
            infoPlant.setText("");
        }

        else if(v.getId()==R.id.back){
            Intent intent=new Intent(getApplicationContext(), Plants.class);
            startActivity(intent);
            finish();
        }

    }


}