package com.example.finek.Start;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finek.R;
import com.example.finek.Service.Functions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Authentification extends AppCompatActivity {
    ProgressBar progressBar;
    public EditText editTextEmail, passwd;
    Button loginButton;
    TextView textViewMdpOub;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentification);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        passwd = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);
        textViewMdpOub = findViewById(R.id.textViewMdpOub);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = passwd.getText().toString();
                if (!Functions.isEmailValid(email)) {
                    editTextEmail.setError("Email invalide");
                    editTextEmail.requestFocus();
                } else if (password.isEmpty()) {
                    passwd.setError("Entrez votre mot de passe");
                    passwd.requestFocus();
                } else if (email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(Authentification.this, "Fields Empty!", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && password.isEmpty())) {

                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Authentification.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(Authentification.this.getApplicationContext(),
                                        "Connexion non reuissite : " + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                            } else {
                                startActivity(new Intent(Authentification.this, Responsable.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(Authentification.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    //// go to register /////////
    public void onLoginClick(View View) {
        startActivity(new Intent(this, Register.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
    }

    public void onClickMDP(View v) {
        final EditText editTextMail = new EditText(v.getContext());
        AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
        passwordResetDialog.setTitle("Réinitialiser Mot de passe");
        passwordResetDialog.setMessage("Donnez votre email pour réinitialiser votre mot de passe");
        passwordResetDialog.setView(editTextMail);

        passwordResetDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //extract email and reset link
                String mail = editTextMail.getText().toString();
                firebaseAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Authentification.this.getApplicationContext(),"Lien de réinitialisation est envoyé a votre boite email", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Authentification.this.getApplicationContext(),"Erreur!!, lien réinitialisation n'est pas envoyé" + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        passwordResetDialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Close the dialog
            }
        });
        passwordResetDialog.create().show();
    }
    ////////////////////////////////
}


