package com.iot.projects4iotmobile.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iot.projects4iotmobile.Dao.PlantsDao;
import com.iot.projects4iotmobile.Model.Note;
import com.iot.projects4iotmobile.R;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class UpdateNote extends AppCompatActivity implements View.OnClickListener{

    Note note ;
    TextInputEditText objectNote;
    TextInputEditText noteText;
    Button cancelNote;
    Button updateNote;
    PlantsDao plantsDao;

    public UpdateNote(){
        plantsDao=new PlantsDao(UpdateNote.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_note);

        note= (Note) getIntent().getSerializableExtra("Note");
        objectNote=(TextInputEditText) findViewById(R.id.objectNote);
        noteText=(TextInputEditText) findViewById(R.id.noteText);
        cancelNote=(Button) findViewById(R.id.cancelNote);
        updateNote=(Button) findViewById(R.id.updateNote);

        cancelNote.setOnClickListener(this);
        //Get text from Intent
        Intent intent = getIntent();
        String objectNoteO = intent.getStringExtra("objectNote");
        String contentNoteO = intent.getStringExtra("contentNote");
        String dateNoteO = intent.getStringExtra("dateNote");
        //Set Text
        objectNote.setText(objectNoteO);
        noteText.setText(contentNoteO);

        cancelNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelUpdate();
            }
        });

        updateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String onN = objectNote.getText().toString();
                String ntN =  noteText.getText().toString();
                DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss ");
                String dateN = df.format(Calendar.getInstance().getTime());

                Note n=new Note(objectNoteO,contentNoteO,dateNoteO,"no");
                // validating the text fields if empty or not.
                if (TextUtils.isEmpty(onN))
                {
                    objectNote.setError("Please enter object");
                }
                else if (TextUtils.isEmpty(ntN))
                {
                    noteText.setError("Please enter note");
                }

                else
                {
                    // calling a method to update our course.
                    // we are passing our object class, course name,
                    // course description and course duration from our edittext field.

                    plantsDao.updateNoteInDb(n, onN, ntN, dateN);
                }
            }
        });

    }



    private void cancelUpdate(){
        Intent intent= new Intent(this, DetailNote.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {

    }


}
