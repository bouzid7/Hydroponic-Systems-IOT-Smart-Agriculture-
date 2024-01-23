package com.iot.projects4iotmobile.Controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.iot.projects4iotmobile.Dao.PlantsDao;
import com.iot.projects4iotmobile.Model.Note;
import com.iot.projects4iotmobile.R;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class AddNote extends AppCompatActivity implements View.OnClickListener{

    TextInputEditText objectNote;
    TextInputEditText noteText;
    Button add_note;
    Button cancel_note;
    Context context;
    PlantsDao plantsDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note);
        objectNote= (TextInputEditText) findViewById(R.id.objectNote);
        objectNote.setOnClickListener(this);
        noteText= (TextInputEditText) findViewById(R.id.noteText);
        noteText.setOnClickListener(this);
        add_note=(Button) findViewById(R.id.add_note);
        add_note.setOnClickListener(this);
        cancel_note=(Button) findViewById(R.id.cancel_note);
        cancel_note.setOnClickListener(this);
    }

    public  AddNote(){
        this.context=AddNote.this;
        plantsDao=new PlantsDao(AddNote.this);
    }

    public void AddNewNote(){
        String object=objectNote.getText().toString();
        String content = noteText.getText().toString();
        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss ");
        String date = df.format(Calendar.getInstance().getTime());
        Note note=new Note(object,content,date,"no");
        plantsDao.AddNewNoteDao(note);
        Intent intent1= new Intent(this, NoteBooks.class);
        startActivity(intent1);
        finish();
    }


    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.add_note) {
            AddNewNote();
        }

        if(v.getId()==R.id.cancel_note){
            Intent intent= new Intent(this, NoteBooks.class);
            startActivity(intent);
            finish();
        }

    }


}

