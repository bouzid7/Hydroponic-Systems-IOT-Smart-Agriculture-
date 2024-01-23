package com.iot.projects4iotmobile.Controller;

import static android.content.ContentValues.TAG;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iot.projects4iotmobile.Model.User_;
import com.iot.projects4iotmobile.R;

import org.jetbrains.annotations.NotNull;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    LinearLayout layout_auth;
    LinearLayout layout_acc;
    TextInputEditText username_auth;
    TextInputEditText password_auth;
    TextInputEditText firstname_acc;
    TextInputEditText lastname_acc;
    TextInputEditText username_acc;
    TextInputEditText password_acc;
    TextInputEditText phone_acc;
    Button singin;
    Button signup_auth;
    Button signup_acc;
    Button cancel;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialisation des views
        layout_auth=(LinearLayout)findViewById(R.id.layout_auth);
        layout_acc=(LinearLayout)findViewById(R.id.layout_acc);
        username_auth= (TextInputEditText) findViewById(R.id.username_auth);
        password_auth= (TextInputEditText) findViewById(R.id.password_auth);

        firstname_acc= (TextInputEditText) findViewById(R.id.firstname_acc);
        lastname_acc= (TextInputEditText) findViewById(R.id.lastname_acc);
        phone_acc= (TextInputEditText) findViewById(R.id.phone_acc);
        username_acc= (TextInputEditText) findViewById(R.id.username_acc);
        password_acc= (TextInputEditText) findViewById(R.id.password_acc);

        singin=(Button) findViewById(R.id.bt_signin);
        signup_auth=(Button) findViewById(R.id.btt_signup);
        signup_acc=(Button) findViewById(R.id.btt_singapp_acc);
        cancel=(Button)findViewById(R.id.btt_cancel_acc);

        //mettre les boutons en ecoute
        singin.setOnClickListener(this);
        signup_auth.setOnClickListener(this);
        signup_acc.setOnClickListener(this);
        cancel.setOnClickListener(this);
        //initialisation de Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {

        if (currentUser!=null)
        {
            layout_auth.setVisibility(View.GONE);
            layout_acc.setVisibility(View.GONE);
            Intent myintent1= new Intent(this, Dashboard.class);
            startActivity(myintent1);
            finish();
        }
        else
        {
            layout_auth.setVisibility(View.VISIBLE);
            layout_acc.setVisibility(View.GONE);
        }

    }


    private  void InsertInCloudDatabase(){

        String Firstname = firstname_acc.getText().toString();
        String Lastname = lastname_acc.getText().toString();
        String phone =  phone_acc.getText().toString();
        String Username = username_acc.getText().toString();
        String password = password_acc.getText().toString();
        db=FirebaseFirestore.getInstance();
        User_ user=new User_(Firstname,Lastname,phone,Username,password);

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity.this,"Successful",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(MainActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }


    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            InsertInCloudDatabase();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void  ClearTextInputEditText() {
        firstname_acc.setText("");
        lastname_acc.setText("");
        username_acc.setText("");
        phone_acc.setText("");
        password_acc.setText("");

    }

     private void Cancel(){
         layout_acc.setVisibility(View.GONE);
         ClearTextInputEditText();
         layout_auth.setVisibility(View.VISIBLE);
     }


    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.bt_signin){
            String email = username_auth.getText().toString();
            String password = password_auth.getText().toString();
            signIn( email,  password);
        }

        else if (view.getId()==R.id.btt_signup) {
            layout_acc.setVisibility(View.VISIBLE);
            layout_auth.setVisibility(View.GONE);
        }

        else if (view.getId()==R.id.btt_cancel_acc) {
                 Cancel();
        }

        else if (view.getId()==R.id.btt_singapp_acc) {
            String email = username_acc.getText().toString();
            String password = password_acc.getText().toString();
            createAccount( email, password) ;
        }

    }

}