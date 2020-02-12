package com.example.finek;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finek.Service.Functions;
import com.example.finek.map.CircleDemoActivity;
import com.example.finek.map.ConsulterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


public class AccoProfil extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText editTextLastName, editTextFirstName, editTextMobile, editTextLiaison;
    RadioButton radioButtonF,radioButtonM;
    RadioGroup radioGroup;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId= "";
            String sexe, str;
    DocumentReference documentReference;
    String idDisparition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accompagnee);
       // drawer();
        fStore= FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        fStore.setFirestoreSettings(settings);
Intent i=getIntent();
        userId=i.getStringExtra("id");

        editTextLastName = findViewById(R.id.editTextLastName);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLiaison= findViewById(R.id.editTextLiaison);
        editTextMobile = findViewById(R.id.editTextMobile);

        radioButtonF = findViewById(R.id.radioButtonFemale);
        radioButtonM = findViewById(R.id.radioButtonMale);

        radioGroup = findViewById(R.id.radio);

      //  fAuth = FirebaseAuth.getInstance();
        //fStore = FirebaseFirestore.getInstance();

    //    userId = fAuth.getCurrentUser().getUid();
        fStore.collection("disparition")
                .whereEqualTo("id_disparu", userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ( (Button)   findViewById(R.id.declareLostButton)).setText("Declarer trouvé");
                                idDisparition=documentReference.getId();
                                System.out.println(idDisparition);;
                            }
                        }
                    }
                });
        documentReference = fStore.collection("accompagne").document(userId);

        Toast.makeText(this,"Discard",Toast.LENGTH_LONG);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                editTextMobile.setText(documentSnapshot.getString("numTel"));
                editTextLastName.setText(documentSnapshot.getString("nom"));
                editTextLiaison.setText(documentSnapshot.getString("liaison"));
                editTextFirstName.setText(documentSnapshot.getString("prenom"));
                sexe = documentSnapshot.getString("sexe");
                switch (sexe){
                    case "Femme":
                        radioButtonF.setChecked(true);
                        radioButtonM.setChecked(false);
                        break;
                    case "Homme":
                        radioButtonM.setChecked(true);
                        radioButtonF.setChecked(false);
                        break;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.whiteTextColor));
        //navigationView.setCheckedItem(R.id.nav_profile);
    }

    public void onClickShow(View view) {

        Toast.makeText(this,"Discard",Toast.LENGTH_LONG);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                editTextMobile.setText(documentSnapshot.getString("numTel"));
                editTextLastName.setText(documentSnapshot.getString("nom"));
                editTextLiaison.setText(documentSnapshot.getString("Liaison"));
                editTextFirstName.setText(documentSnapshot.getString("prenom"));
                sexe = documentSnapshot.getString("sexe");
                switch (sexe){
                    case "Femme":
                        radioButtonF.setChecked(true);
                        radioButtonM.setChecked(false);
                        break;
                    case "Homme":
                        radioButtonM.setChecked(true);
                        radioButtonF.setChecked(false);
                        break;
                }
            }
        });
    }

    public void onClickSave(View view) {
        final String lastName = editTextLastName.getText().toString();
        final String firstName = editTextFirstName.getText().toString();
        final String liaison = editTextLiaison.getText().toString();
        final String mobile = editTextMobile.getText().toString();

        final String sexe = findSelectedSexe();

        if (lastName.isEmpty()) {
            editTextLastName.setError("Entrez votre nom");
            editTextLastName.requestFocus();
        } else if (firstName.isEmpty()) {
            editTextFirstName.setError("Enterz votre prénom");
            editTextFirstName.requestFocus();
        }  else if (liaison.isEmpty()) {
            editTextLiaison.setError("Enterz votre Liaison");
            editTextLiaison.requestFocus();
       } else if (!Functions.isPhoneValid(mobile) || mobile.isEmpty()) {
            editTextMobile.setError("Numéro de téléphone Non valide");
            editTextMobile.requestFocus();
        } else {
            //updating artist
            Map<String, Object> accompagne = new HashMap<>();
            accompagne.put("id_responsable",userId);
            accompagne.put("nom", lastName);
            accompagne.put("prenom", firstName);
            accompagne.put("numTel", mobile);
            accompagne.put("sexe",sexe);
            accompagne.put("liaison", liaison);
            accompagne.put("etat", "");
            documentReference.set(accompagne).addOnSuccessListener( new OnSuccessListener<Void>() {

                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG,"onSuccess: user Profile is added for "+ userId);
                    str="Accompagnée mis à jour avec succès";
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: " + e.toString());
                    str="Accompagnée n'est pas mis à jour";
                }
            });
            Toast.makeText(this,str,Toast.LENGTH_LONG).show();
        }
    }

    public String findSelectedSexe() {
        RadioGroup rg = (RadioGroup)findViewById(R.id.radioButtonGroup);

        int option = rg.getCheckedRadioButtonId();
        String output = "";

        switch (option)
        {
            case R.id.radioButtonFemale: output = "Femme";
                break;
            case R.id.radioButtonMale: output = "Homme";
                break;
        }
        return output;
    }

    public void onClickAddZone(View view) {
       Intent intent = new Intent(getApplicationContext(), CircleDemoActivity.class);
        intent.putExtra("id",userId);
        startActivity(intent);


    }

    public void onClickDiscard(View view) {
      /*  Intent intent = new Intent(getApplicationContext(),EditorPage.class);
        intent.putExtra("id",userId);
        startActivity(intent);

       */
    }

    public void onClickTask(View view) {
        Intent intent = new Intent(getApplicationContext(),GetTaskActivity.class);
        intent.putExtra("id",userId);
        startActivity(intent);


    }

    public void onClickDeclare(View view) {

        if(((Button)   findViewById(R.id.declareLostButton)).getText().equals("Declarer trouvé")) {
            fStore.collection("disparition").document(idDisparition)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Trouvé", Toast.LENGTH_LONG);
                            ((Button) findViewById(R.id.declareLostButton)).setText("Declarer Perdu");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Erreur", Toast.LENGTH_LONG);
                        }
                    });
        }

        else {
            Intent intent = new Intent(getApplicationContext(),DeclarerPerduActivity.class);
            intent.putExtra("id",userId);
            startActivity(intent);

        }

    }

    public void onClickShowZones(View view) {
        Intent intent = new Intent(getApplicationContext(), ConsulterActivity.class);
        intent.putExtra("id",userId);
        startActivity(intent);


    }
}