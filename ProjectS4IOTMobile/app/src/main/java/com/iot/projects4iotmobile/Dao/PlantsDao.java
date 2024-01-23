package com.iot.projects4iotmobile.Dao;


import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.iot.projects4iotmobile.Controller.AddNote;
import com.iot.projects4iotmobile.Controller.AddPlant;
import com.iot.projects4iotmobile.Controller.DetailPlant;
import com.iot.projects4iotmobile.Controller.NoteBooks;
import com.iot.projects4iotmobile.Controller.Plants;
import com.iot.projects4iotmobile.Controller.ProfileUser;
import com.iot.projects4iotmobile.Controller.UpdateNote;
import com.iot.projects4iotmobile.Model.Note;
import com.iot.projects4iotmobile.Model.Plant;
import com.iot.projects4iotmobile.Model.User_;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;


public class PlantsDao extends AppCompatActivity  {
     Context context;
     private FirebaseFirestore db;
     private FirebaseAuth mAuth;
     private FirebaseUser currentUser;
     private FirebaseStorage storage;
     private StorageReference storageReference;
     private FirebaseDatabase firebaseDatabase;
     private DatabaseReference databaseReference;
     private User_ data_user;

    public PlantsDao(Context context1){
        db = FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        firebaseDatabase=FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();
        this.context=context1;
    }

    public void LogOut(){
        mAuth.signOut();
    }

    //on doit s'assurer que le chargement des donnees est terminé avant de retourner
    // le resultast final

    public LinkedList<Plant> getAllPlants(){
        LinkedList<Plant> plants= new LinkedList<Plant>();
        final boolean[] terminated = new boolean[1];
        terminated[0] = false;
        DocumentReference docRef = db.collection("users").document(currentUser.getEmail());
        docRef.collection("plants").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Plant p= new Plant(document.get("name").toString(),document.get("info").toString(),document.get("image").toString(),document.get("isFavorite").toString());
                                plants.add(p);
                            }
                            terminated[0] = true;
                        } else {
                            Log.d("not ok", "Error getting documents: ", task.getException());
                        }
                    }
                });

        while (  terminated[0] == false)
            System.out.println("Loading...");;
        return plants;

    }


    public LinkedList<Plant> getFavoritesPlants(){

        LinkedList<Plant> plants= new LinkedList<Plant>();
        final boolean[] terminated = new boolean[1];
        terminated[0] = false;
        DocumentReference docRef = db.collection("users").document(currentUser.getEmail());
        docRef.collection("favoritesPlant").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Plant p= new Plant(document.get("name").toString(),document.get("info").toString(),document.get("image").toString(),document.get("isFavorite").toString());
                                plants.add(p);
                            }
                            terminated[0] = true;
                        } else {
                            Log.d("not ok", "Error getting documents: ", task.getException());
                        }
                    }
                });

        while (  terminated[0] == false)
            System.out.println("Loading...");;
        return plants;

    }




    public void insertDataPlant(Plant plant){

        DocumentReference docRef = db.collection("users").document(currentUser.getEmail());
        docRef.collection("plants")
                .add(plant)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                       Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void delete(Plant plant){

        // Modify the query to retrieve the document reference for the contact that needs to be deleted.
        Task<QuerySnapshot> docRef = db.collection("users").document(currentUser.getEmail())
                .collection("plants")
                .whereEqualTo("name", plant.getName())
                .whereEqualTo("info", plant.getInfo())
                .whereEqualTo("image",plant.getImage())
                .limit(1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // Get the reference of the document to be deleted.
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        DocumentReference documentReference = documentSnapshot.getReference();

                        // Delete the document from the favorites collection.
                        documentReference.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "plant removed", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Toast.makeText(context, "Failed to remove plant", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

    }

    public  void updatePlant(Plant oldPlant,Plant newPlant0) {

        Map<String,Object> newPlant=new HashMap<>();
        newPlant.put("name",newPlant0.getName());
        newPlant.put("info",newPlant0.getInfo());
        DocumentReference docRef = db.collection("users").document(currentUser.getEmail());
        docRef.collection("plants").whereEqualTo("name",oldPlant.getName()).whereEqualTo("info",oldPlant.getInfo())
                  .get()
                  .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful() && !task.getResult().isEmpty()){
                            DocumentSnapshot documentSnapshot=task.getResult().getDocuments().get(0);
                            String documentID=documentSnapshot.getId();
                            db.collection("users").document(currentUser.getEmail()).collection("plants").document(documentID)
                                    .update(newPlant).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context,"plant has been updated",Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                           Toast.makeText(context,"plant has been failed to update",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });

    }

    /*

    public void addToFavorites(Plant plant){

        DocumentReference docRef = db.collection("users").document(currentUser.getEmail());
        docRef.collection("favorites")
                .add(plant)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(context, "Successful,plant become favorite", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                        Toast.makeText(context, "Failed to be favorite", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void deleteFromFavorites(Plant plant){

        Task<QuerySnapshot> docRef = db.collection("users").document(currentUser.getEmail())
                .collection("favorites")
                .whereEqualTo("name", plant.getName())
                .whereEqualTo("info", plant.getInfo())
                .whereEqualTo("image", plant.getImage())
                .limit(1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // Get the reference of the document to be deleted.
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        DocumentReference documentReference = documentSnapshot.getReference();

                        // Delete the document from the favorites collection.
                        documentReference.delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "Contact removed from favorites", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Toast.makeText(context, "Failed to remove contact from favorites", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

    }

     */


    public void uploadPicture(Uri imageUri) {

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setTitle("Uploading picture...");
        pd.show();
        final String randomKey = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("images/" + randomKey);
        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed to upload", Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            pd.setMessage("Percentage: " + (int) progressPercent + "%");
                    }
                });


    }


     public void FalseFavorites(Plant plant){

        if (currentUser != null) {
            // Modify the query to retrieve the document reference for the contact that needs to be deleted.
            Task<QuerySnapshot> docRef = db.collection("users").document(currentUser.getEmail())
                    .collection("favoritesPlant")
                    .whereEqualTo("name", plant.getName())
                    .whereEqualTo("info",plant.getInfo())
                    .limit(1)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            // Get the reference of the document to be deleted.
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            DocumentReference documentReference = documentSnapshot.getReference();

                            // Delete the document from the favorites collection.
                            documentReference.delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "plant removed from favorites", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {
                                            Toast.makeText(context, "Failed to remove plant from favorites", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
        }

        Map<String,Object> NoteFavoritesChange=new HashMap<>();
        NoteFavoritesChange.put("name",plant.getName());
        NoteFavoritesChange.put("info",plant.getInfo());
        NoteFavoritesChange.put("isFavorite","no");
        DocumentReference docRef = db.collection("users").document(currentUser.getEmail());
        docRef.collection("plants").whereEqualTo("name",plant.getName()).whereEqualTo("info",plant.getInfo())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful() && !task.getResult().isEmpty()){
                            DocumentSnapshot documentSnapshot=task.getResult().getDocuments().get(0);
                            String documentID=documentSnapshot.getId();
                            db.collection("users").document(currentUser.getEmail()).collection("plants").document(documentID)
                                    .update(NoteFavoritesChange).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context,"note has been deleted from favorites",Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context,"note has been failed to delete it from favorites",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }

                    }
                });
    }

      public void TrueFavorites(Plant plant){

         Plant  p=new Plant(plant.getName(),plant.getInfo(),plant.getImage(),plant.getIsFavorite());
        if (currentUser != null) {
            DocumentReference docRef = db.collection("users").document(currentUser.getEmail());
            docRef.collection("favoritesPlant")
                    .add(p)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(context, "Successful,plant become favorite", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {

                            Toast.makeText(context, "Failed to be favorite", Toast.LENGTH_SHORT).show();
                        }
                    });
        }



        Map<String,Object> NoteFavoritesChange=new HashMap<>();
        NoteFavoritesChange.put("name",plant.getName());
        NoteFavoritesChange.put("info",plant.getInfo());
        NoteFavoritesChange.put("isFavorite","yes");
        DocumentReference docRef = db.collection("users").document(currentUser.getEmail());
        docRef.collection("plants").whereEqualTo("name",plant.getName()).whereEqualTo("info",plant.getInfo())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful() && !task.getResult().isEmpty()){
                            DocumentSnapshot documentSnapshot=task.getResult().getDocuments().get(0);
                            String documentID=documentSnapshot.getId();
                            db.collection("users").document(currentUser.getEmail()).collection("plants").document(documentID)
                                    .update(NoteFavoritesChange).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context,"plant has been added to favorites",Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context,"plant has been failed to added to favorites",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }

                    }
                });

    }

    public void AddNewNoteDao(Note note){

        if (currentUser != null) {
            DocumentReference docRef = db.collection("users").document(currentUser.getEmail());
            docRef.collection("notebooks")
                    .add(note)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {

                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }


  /*  public ArrayList<Note> extractDataNotes() {

        DocumentReference docRef = db.collection("users").document(currentUser.getEmail());
        docRef.collection("notebooks").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Note allNotes = new Note(
                                        document.get("objectNote").toString(),
                                        document.get("contentNote").toString(),
                                        document.get("dateNote").toString(),
                                        document.get("isFavorite").toString()
                                );

                                noteModalArrayList.add(allNotes);

                            }

                        }
                        else
                        {
                            Log.d("not ok", "Error getting documents: ", task.getException());
                        }
                    }
                });

              return noteModalArrayList;
    }*/


    public LinkedList<Note> getAllNotesBooks() {
        LinkedList<Note> notes = new LinkedList<Note>();
        final boolean[] terminated = new boolean[1];
        terminated[0] = false;
        DocumentReference docRef = db.collection("users").document(currentUser.getEmail());
        docRef.collection("notebooks").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Note p = new Note(
                                        document.get("objectNote").toString(),
                                        document.get("contentNote").toString(),
                                        document.get("dateNote").toString(),
                                        document.get("isFavorite").toString()
                                );
                                notes.add(p);
                            }
                            terminated[0] = true;
                        } else {
                            Log.d("not ok", "Error getting documents: ", task.getException());
                        }
                    }
                });

        while (terminated[0] == false)
            System.out.println("Loading...");
        return notes;
    }

    public LinkedList<Note>  getAllNotesBooksFavorites(){
        LinkedList<Note> notes = new LinkedList<Note>();
        final boolean[] terminated = new boolean[1];
        terminated[0] = false;
        DocumentReference docRef = db.collection("users").document(currentUser.getEmail());
        docRef.collection("notebooksFavorites").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Note p = new Note(
                                        document.get("objectNote").toString(),
                                        document.get("contentNote").toString(),
                                        document.get("dateNote").toString(),
                                        document.get("isFavorite").toString()
                                );
                                notes.add(p);
                            }
                            terminated[0] = true;
                        } else {
                            Log.d("not ok", "Error getting documents: ", task.getException());
                        }
                    }
                });

        while (terminated[0] == false)
            System.out.println("Loading...");
        return notes;
    }

    public void DeleteNoteFromNotebooks(Note note){

        if (currentUser != null) {
            // Modify the query to retrieve the document reference for the contact that needs to be deleted.
            Task<QuerySnapshot> docRef = db.collection("users").document(currentUser.getEmail())
                    .collection("notebooks")
                    .whereEqualTo("objectNote",note.getObjectNote())
                    .whereEqualTo("contentNote", note.getContentNote())
                    .whereEqualTo("dateNote", note.getDateNote())
                    .limit(1)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            // Get the reference of the document to be deleted.
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            DocumentReference documentReference = documentSnapshot.getReference();

                            // Delete the document from the favorites collection.
                            documentReference.delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "note removed", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {
                                            Toast.makeText(context, "Failed to remove note", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
        }
    }

    public  void FavoritesFalseNoteBooks(Note note){

        if (currentUser != null) {
            // Modify the query to retrieve the document reference for the contact that needs to be deleted.
            Task<QuerySnapshot> docRef = db.collection("users").document(currentUser.getEmail())
                    .collection("notebooksFavorites")
                    .whereEqualTo("objectNote", note.getObjectNote())
                    .whereEqualTo("contentNote",note.getContentNote())
                    .limit(1)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            // Get the reference of the document to be deleted.
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            DocumentReference documentReference = documentSnapshot.getReference();

                            // Delete the document from the favorites collection.
                            documentReference.delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "note removed from favorites", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull @NotNull Exception e) {
                                            Toast.makeText(context, "Failed to remove note from favorites", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
        }

        Map<String,Object> NoteFavoritesChange=new HashMap<>();
        NoteFavoritesChange.put("objectNote",note.getObjectNote());
        NoteFavoritesChange.put("contentNote",note.getContentNote());
        NoteFavoritesChange.put("dateNote",note.getDateNote());
        NoteFavoritesChange.put("isFavorite","no");
        DocumentReference docRef = db.collection("users").document(currentUser.getEmail());
        docRef.collection("notebooks").whereEqualTo("objectNote",note.getObjectNote()).whereEqualTo("contentNote",note.getContentNote())
                .whereEqualTo("dateNote",note.getDateNote()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful() && !task.getResult().isEmpty()){
                            DocumentSnapshot documentSnapshot=task.getResult().getDocuments().get(0);
                            String documentID=documentSnapshot.getId();
                            db.collection("users").document(currentUser.getEmail()).collection("notebooks").document(documentID)
                                    .update(NoteFavoritesChange).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context,"note has been deleted from favorites",Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context,"note has been failed to delete it from favorites",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }

                    }
                });

    }

    public  void FavoritesTrueNoteBooks(Note note){

        if (currentUser != null) {
            DocumentReference docRef = db.collection("users").document(currentUser.getEmail());
            docRef.collection("notebooksFavorites")
                    .add(note)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(context, "Successful,note become favorite", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {

                            Toast.makeText(context, "Failed to be favorite", Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        Map<String,Object> NoteFavoritesChange=new HashMap<>();
        NoteFavoritesChange.put("objectNote",note.getObjectNote());
        NoteFavoritesChange.put("contentNote",note.getContentNote());
        NoteFavoritesChange.put("dateNote",note.getDateNote());
        NoteFavoritesChange.put("isFavorite","yes");
        DocumentReference docRef = db.collection("user").document(currentUser.getEmail());
        docRef.collection("notebooks").whereEqualTo("objectNote",note.getObjectNote()).whereEqualTo("contentNote",note.getContentNote())
                .whereEqualTo("dateNote",note.getDateNote()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful() && !task.getResult().isEmpty()){
                            DocumentSnapshot documentSnapshot=task.getResult().getDocuments().get(0);
                            String documentID=documentSnapshot.getId();
                            db.collection("user").document(currentUser.getEmail()).collection("notebooks").document(documentID)
                                    .update(NoteFavoritesChange).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context,"note has been added to favorites",Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context,"note has been failed to added to favorites",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }

                    }
                });

    }


    public void updateNoteInDb(Note  note, String ob, String nt, String date) {

        Map<String,Object> newNote=new HashMap<>();
        newNote.put("objectNote",ob);
        newNote.put("contentNote",nt);
        newNote.put("dateNote",date);
        newNote.put("isFavorite","no");
        DocumentReference docRef = db.collection("users").document(currentUser.getEmail());
        docRef.collection("notebooks").whereEqualTo("objectNote",note.getObjectNote()).whereEqualTo("contentNote",note.getContentNote())
                .whereEqualTo("dateNote",note.getDateNote()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful() && !task.getResult().isEmpty()){
                            DocumentSnapshot documentSnapshot=task.getResult().getDocuments().get(0);
                            String documentID=documentSnapshot.getId();
                            db.collection("users").document(currentUser.getEmail()).collection("notebooks").document(documentID)
                                    .update(newNote).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context,"note has been updated",Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context,"note has been failed to update",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                    }
                });

    }

   public User_ getDataProfileUser() {

        db.collection("users")
                .whereEqualTo("userName", mAuth.getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                              /*
                              data_user=new User_(
                                        document.getString("firstName"),
                                        document.getString("lastName"),
                                        document.getString("phone"),
                                        document.getString("userName"),
                                        document.getString("password")
                                );
                               */
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

       data_user=new User_("bouzid",
               "Abdelfattah",
               "0656235312",
               "bouzid7@gmail.com",
               "ZT277359"
       );
       return data_user;

    }

    public void UpdateDataUserOfGreenHouse( Map<String,Object> updateUser,String NewUsername,String NewPassword,User_ user){

        assert currentUser != null;
        currentUser.updateEmail(NewUsername).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "email updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "email failed to  update it", Toast.LENGTH_SHORT).show();
            }
        });

        currentUser.updatePassword(NewPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "password updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "password failed to  update it", Toast.LENGTH_SHORT).show();
            }
        });


        db.collection("users")
                .whereEqualTo("userName", user.getUserName()).whereEqualTo("firstName", user.getFirstName())
                .whereEqualTo("lastName", user.getLastName())
                .whereEqualTo("password", user.getPassword())
                .whereEqualTo("phone", user.getPhone())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                            String documentID = documentSnapshot.getId();
                            db.collection("users").document(documentID)
                                    .update(updateUser)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "user has been updated", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "user has been failed to update", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });

    }




}

