package com.example.finek.Start;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.finek.R;
import com.example.finek.Service.Functions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Register extends AppCompatActivity {
    //needed variables
    public static final String TAG = "TAG";
    EditText editTextLastName, editTextFirstName, editTextEmail, editTextMobile, editTextPassword, editTextPasswordConfirm, editTextProfession;
    ProgressBar progressBar;
    RadioGroup radioGroup;

    EditText editTextDateOfBirth;
    private int mYear, mMonth, mDay;

    FirebaseAuth firebaseAuth;

    //create collections documents and restore data
    FirebaseFirestore fStore;
    String userID;
    ///////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //[Needed assignments]
        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        editTextLastName = findViewById(R.id.editTextLastName);
        editTextProfession = findViewById(R.id.editTextProfession);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordConfirm = findViewById(R.id.editTextPasswordConfirm);
        editTextMobile = findViewById(R.id.editTextMobile);

        radioGroup = findViewById(R.id.radioButtonGroup);

        progressBar = findViewById(R.id.progressBar);

        editTextDateOfBirth = (EditText) findViewById(R.id.editTextDateOfBirth);

        //[onClickListner]
    }
    public void onClick(View view) {
        final String lastName = editTextLastName.getText().toString();
        final String firstName = editTextFirstName.getText().toString();
        final String email = editTextEmail.getText().toString();
        final String profession = editTextProfession.getText().toString();
        final String password = editTextPassword.getText().toString();
        final String passwordConfirm = editTextPasswordConfirm.getText().toString();
        final String mobile = editTextMobile.getText().toString();
        final String dateOfBirth = editTextDateOfBirth.getText().toString();

        final String sexe = findSelectedSexe();

        if (lastName.isEmpty()) {
            editTextLastName.setError("Entrez votre nom");
            editTextLastName.requestFocus();
            System.out.println("1");
        }
        else if (firstName.isEmpty()) {
            editTextFirstName.setError("Enterz votre prénom");
            editTextFirstName.requestFocus();
            System.out.println("2");
        }
        else if (!Functions.isPhoneValid(mobile) || mobile.isEmpty()) {
            editTextMobile.setError("Numéro de téléphone Non valide");
            editTextMobile.requestFocus();
            System.out.println("3");
        }
        else if (!Functions.isEmailValid(email) || email.isEmpty()) {
            editTextEmail.setError("Email invalide");
            editTextEmail.requestFocus();
            System.out.println("4");
        }
        else if (password.length() < 8) {
            editTextPassword.setError("Le Mot de passe doit contenir 8 caractères ou plus");
            editTextPassword.requestFocus();
            System.out.println("5");
        }
        else if (passwordConfirm.isEmpty()) {
            editTextPasswordConfirm.setError("Confirmez Votre Mot de passe");
            editTextPasswordConfirm.requestFocus();
            System.out.println("6");
        }
        else if (!passwordConfirm.equals(password)) {
            editTextPasswordConfirm.setError("Mots de passe non conforme "+ password + "  " + passwordConfirm);
            editTextPasswordConfirm.requestFocus();
            System.out.println("7");
        }
        else {
            boolean b = lastName.isEmpty() && firstName.isEmpty() && email.isEmpty() && password.isEmpty() && passwordConfirm.isEmpty() && mobile.isEmpty();
            if (b) {
                Toast.makeText(Register.this, "Remplissez les champs S'il vous plaît!", Toast.LENGTH_SHORT).show();
            } else if (!(b)) {
                progressBar.setVisibility(View.VISIBLE);
                //Firebase authentification
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(Register.this.getApplicationContext(),
                                    "L'inscription  n'a pas réussi : " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Register.this.getApplicationContext(),
                                    "Utilisateur crée",
                                    Toast.LENGTH_SHORT).show();

                            //retrieve the user id of the currently registred user
                            userID = firebaseAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("responsable").document(userID);

                            Map<String,Object> responsable = new HashMap<>();
                            responsable.put("nom",lastName);
                            responsable.put("prenom",firstName);
                            responsable.put("profession",profession);
                            responsable.put("numTel",mobile);
                            responsable.put("email",email);
                            responsable.put("sexe",sexe);
                            responsable.put("date_naissance",dateOfBirth);

                            documentReference.set(responsable).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"onSuccess: user Profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                          //  startActivity(new Intent(Register.this, Responsable.class));
                        }
                    }
                });
            } else {
                Toast.makeText(Register.this, "Erreur", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
   }

    //[onLoginClick and onClickDate]
    public void onLoginClick(View view){
        startActivity(new Intent(this,Authentification.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
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

    public void onClickDate(View v) {
       // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        editTextDateOfBirth.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }
}

