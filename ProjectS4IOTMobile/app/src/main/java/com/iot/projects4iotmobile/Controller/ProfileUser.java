package com.iot.projects4iotmobile.Controller;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import com.google.android.material.textfield.TextInputEditText;
import com.iot.projects4iotmobile.Dao.PlantsDao;
import com.iot.projects4iotmobile.Model.User_;
import com.iot.projects4iotmobile.R;
import java.util.HashMap;
import java.util.Map;


public class ProfileUser extends AppCompatActivity implements View.OnClickListener{

    TextInputEditText firstName;
    TextInputEditText lastName;
    TextInputEditText email;
    TextInputEditText password1;
    User_ user;
    PlantsDao plantsDao;

    public ProfileUser(){
       plantsDao=new PlantsDao(ProfileUser.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_user);
        firstName=(TextInputEditText)findViewById(R.id.firstName);
        lastName=(TextInputEditText)findViewById(R.id.lastName);
        email=(TextInputEditText)findViewById(R.id.email);
        password1=(TextInputEditText)findViewById(R.id.password);
        //Get data  from Intent
        Intent intent = getIntent();
        user= new User_(
                intent.getStringExtra("firstName"),
                intent.getStringExtra("lastName"),
                intent.getStringExtra("phone"),
                intent.getStringExtra("userName"),
                intent.getStringExtra("password")
        );
        //set  data in layout
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        email.setText(user.getUserName());
        password1.setText(user.getPassword());

    }

    public void logOut(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this::onMenuItemClick);
        popup.inflate(R.menu.menu_profile);
        popup.show();
    }
    void signOut(){
        plantsDao.LogOut();
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    void back(){
        onBackPressed();
    }

    void updateDataUser(){

        String NewFirstname = firstName.getText().toString();
        String NewLastname = lastName.getText().toString();
        String NewUsername = email.getText().toString();
        String Newpassword= password1.getText().toString();
        // User  updateUser=new User(Firstname,Lastname,birthdate,Username,password0);
        Map<String,Object> updateUser=new HashMap<>();
        updateUser.put("firstName",NewFirstname);
        updateUser.put("lastName",NewLastname);
        updateUser.put("userName",NewUsername);
        updateUser.put("password",Newpassword);
        updateUser.put("phone",user.getPhone());
        plantsDao.UpdateDataUserOfGreenHouse(updateUser,NewUsername,Newpassword,user);
        signOut();
    }
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case  R.id.option_deconexion:
                signOut();
                return true;
            case  R.id.option_save:
                updateDataUser();
                return true;
            case  R.id.option_back:
                back();
                return true;

            default:
                return false;
        }
    }


    @Override
    public void onClick(View v) {

    }


}
