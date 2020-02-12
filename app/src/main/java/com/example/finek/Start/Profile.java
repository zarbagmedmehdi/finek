package com.example.finek.Start;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.finek.R;
import com.example.finek.Service.Functions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;



public class Profile extends Responsable {

    public static final String TAG = "TAG";
    EditText editTextLastName, editTextFirstName, editTextEmail, editTextMobile, editTextDateOfBirth, editTextProfession;
    RadioButton radioButtonF,radioButtonM;
    RadioGroup radioGroup;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId, sexe,str;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        drawer();


        editTextLastName = findViewById(R.id.editTextLastName);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextProfession= findViewById(R.id.editTextProfession);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextMobile = findViewById(R.id.editTextMobile);

        radioButtonF = findViewById(R.id.radioButtonFemale);
        radioButtonM = findViewById(R.id.radioButtonMale);

        radioGroup = findViewById(R.id.radio);

        editTextDateOfBirth = findViewById(R.id.editTextDateOfBirth);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();
        System.out.println(userId);
        documentReference = fStore.collection("responsable").document(userId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.whiteTextColor));
        navigationView.setCheckedItem(R.id.nav_profile);
    }

    public void onClickDiscard(View view) {

        Toast.makeText(this,"Discard",Toast.LENGTH_LONG);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                editTextMobile.setText(documentSnapshot.getString("numTel"));
                editTextLastName.setText(documentSnapshot.getString("nom"));
                editTextProfession.setText(documentSnapshot.getString("profession"));
                editTextEmail.setText(documentSnapshot.getString("email"));
                editTextFirstName.setText(documentSnapshot.getString("prenom"));
                editTextDateOfBirth.setText(documentSnapshot.getString("date_naissance"));
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
        final String profession = editTextProfession.getText().toString();
        final String email = editTextEmail.getText().toString();
        final String mobile = editTextMobile.getText().toString();
        final String dateOfBirth = editTextDateOfBirth.getText().toString();

        final String sexe = findSelectedSexe();

        //int selectedId = radioGroup.getCheckedRadioButtonId();
        //radioButton = findViewById(selectedId);

        //final String sexe = (String) radioButton.getText();
        if (lastName.isEmpty()) {
            editTextLastName.setError("Entrez votre nom");
            editTextLastName.requestFocus();
        } else if (firstName.isEmpty()) {
            editTextFirstName.setError("Enterz votre prénom");
            editTextFirstName.requestFocus();
        }  else if (profession.isEmpty()) {
            editTextProfession.setError("Enterz votre profession");
            editTextProfession.requestFocus();
        } else if (!Functions.isPhoneValid(mobile) || mobile.isEmpty()) {
            editTextMobile.setError("Numéro de téléphone Non valide");
            editTextMobile.requestFocus();
        }else if (dateOfBirth.isEmpty()) {
            editTextDateOfBirth.setError("Enterz votre date de naissance");
            editTextDateOfBirth.requestFocus();
        } else if (!Functions.isEmailValid(email) || email.isEmpty()) {
            editTextEmail.setError("Email invalide");
            editTextEmail.requestFocus();
        } else {
                 //updating artist
                 Map<String, Object> responsable = new HashMap<>();
                 responsable.put("nom", lastName);
                 responsable.put("prenom", firstName);
                 responsable.put("profession", profession);
                 responsable.put("numTel", mobile);
                 responsable.put("email", email);
                 responsable.put("sexe",sexe);
                 responsable.put("date_naissance",dateOfBirth);

                 documentReference.set(responsable).addOnSuccessListener( new OnSuccessListener<Void>() {
                     @Override
                     public void onSuccess(Void aVoid) {
                         Log.d(TAG,"onSuccess: user Profile is updated for "+ userId);
                         str="Enregistré avec succès";
                     }
                 }).addOnFailureListener(new OnFailureListener() {
                     @Override
                     public void onFailure(@NonNull Exception e) {
                         Log.d(TAG, "onFailure: " + e.toString());
                         str="erreur";
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
}

































































































































/*package com.example.finek;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle;
        toggle = new ActionBarDrawerToggle(this, drawer,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_responsable_drawer, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    // @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //here is the main place where we need to work on.
        Intent intent;
        int id=item.getItemId();
        switch (id){

            case R.id.nav_profile:
                if(!getClass().equals(Profile.class)) {
                    intent = new Intent(this, Profile.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
                }
                break;
            case R.id.nav_add_accompagnee:
                if(!getClass().equals(AddAccompagnee.class)) {
                    intent = new Intent(this, AddAccompagnee.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
                }
                break;
            case R.id.nav_liste_accompagnees:
                if(!getClass().equals(ListeDesAccompagnees.class)) {
                    intent = new Intent(this, ListeDesAccompagnees.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
                }
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        //customProgressBar.dismiss();
    }

}
*/