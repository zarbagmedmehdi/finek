package com.example.finek.Service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.finek.CreateTaskActivity;
import com.example.finek.GetTaskActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class TaskService {
    FirebaseFirestore db ;
    ImageService imageService=new ImageService();
    RequestQueue mRequestQue;

    public TaskService() {
        db= FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

    }
    public void createTask(String description, String nom, Boolean type, Timestamp date, final String idAc, final Context c){
        ///  final String[] id = {""};
        Map<String, Object> disparition = new HashMap<>();
        disparition.put("date", date);
        disparition.put("nom",nom);
        disparition.put("description",description);
        disparition.put("id_accompagne", idAc);
        if(type)
        disparition.put("type","répétitive");
        else   disparition.put("type","unique");
        db.collection("tache")
                .add(disparition)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        System.out.println( documentReference.getId());
                        ///uploadImage( imageView1,  imageView2,  c,documentReference.getId());
                        Toast.makeText(c,"Tache créee",Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
        // return id[0];

    }
    public void getTasks(String id){

        db.collection("tache")
                .whereEqualTo("id_accompagne", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                              document.getData();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }



}
