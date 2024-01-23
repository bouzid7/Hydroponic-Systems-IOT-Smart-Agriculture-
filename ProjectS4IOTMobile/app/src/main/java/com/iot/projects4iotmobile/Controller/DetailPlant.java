package com.iot.projects4iotmobile.Controller;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.iot.projects4iotmobile.Dao.PlantsDao;
import com.iot.projects4iotmobile.Model.Plant;
import com.iot.projects4iotmobile.R;


public class DetailPlant extends AppCompatActivity implements View.OnClickListener {

     Plant plant;
     ImageView photo_plant,back,editPlant;
     ImageView favorite_true;
     ImageView favorite_false;
     TextView identification_plant,infoPlant;
     PlantsDao plantsDao;

     public DetailPlant(){
         plantsDao=new PlantsDao(DetailPlant.this);

     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailplant);


        favorite_true=(ImageView) findViewById(R.id.favorite_true);
        favorite_false=(ImageView) findViewById(R.id.favorite_false);

        favorite_true.setOnClickListener(this);
        favorite_false.setOnClickListener(this);
        back=(ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        editPlant=(ImageView) findViewById(R.id.editPlant);
        /* data of details plant */
        plant= (Plant) getIntent().getSerializableExtra("plant");
        photo_plant=(ImageView) findViewById(R.id.photo_plant);
        identification_plant=(TextView) findViewById(R.id.identification_plant);
        infoPlant=(TextView) findViewById(R.id.infoPlant);
        identification_plant.setText(plant.getName());
        infoPlant.setText(plant.getInfo());

        if (plant.getIsFavorite().equals("no")){
           favorite_false.setVisibility(View.VISIBLE);
           favorite_true.setVisibility(View.GONE);
        }

        else if (plant.getIsFavorite().equals("yes")) {
            favorite_false.setVisibility(View.GONE);
            favorite_true.setVisibility(View.VISIBLE);
        }

        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(plant.getImage());
        Glide.with(getApplicationContext() ).load(storageReference).into(photo_plant);


        editPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailPlant.this, ReceiveDataToUpdate.class);
                intent.putExtra("name",plant.getName());
                intent.putExtra("info",plant.getInfo());
                intent.putExtra("image",plant.getImage());
                startActivity(intent);
            }
        });




    }


    private void getProfile(){

        Intent intent = new Intent(DetailPlant.this, ProfileUser.class);
        intent.putExtra("userName" ,plantsDao.getDataProfileUser().getUserName());
        intent.putExtra("firstName",plantsDao.getDataProfileUser().getFirstName());
        intent.putExtra("lastName",plantsDao.getDataProfileUser().getLastName());
        intent.putExtra("phone",plantsDao.getDataProfileUser().getPhone());
        intent.putExtra("password",plantsDao.getDataProfileUser().getPassword());
        startActivity(intent);

    }

    public void  menuDetails(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        popup.inflate(R.menu.details_menu);
        popup.show();
    }

    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case  R.id.profile:
                getProfile();
                return true;
            case R.id.favorites:
                Intent intent0=new Intent(DetailPlant.this,FavoritesPlants.class);
                startActivity(intent0);
                return true;
            case R.id.delete:
                 plantsDao.delete(plant);
                Intent intent1=new Intent(getApplicationContext(), Plants.class);
                startActivity(intent1);
                finish();
                return true;
            case R.id.notes:
                Intent intentN=new Intent(DetailPlant.this,NoteBooks.class);
                startActivity(intentN);
                return true;
            case R.id.logOut:
                 plantsDao.LogOut();
                Intent intent2=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent2);
                finish();
                return true;

            default:
                return false;
        }

    }

    private void FavoriteFalse(){

         favorite_true.setVisibility(View.GONE);
         favorite_false.setVisibility(View.VISIBLE);
        // delete note from favorites in cloud
          plantsDao.FalseFavorites(plant);
    }


     private  void FavoriteTrue(){

         favorite_false.setVisibility(View.GONE);
         favorite_true.setVisibility(View.VISIBLE);
         // add note to favorites in cloud
          plantsDao.TrueFavorites(plant);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back){
            onBackPressed();
        }

        else if (v.getId() == R.id.favorite_false){
             FavoriteTrue();
        }

        else if (v.getId() == R.id.favorite_true){
             FavoriteFalse();

        }

    }

}
