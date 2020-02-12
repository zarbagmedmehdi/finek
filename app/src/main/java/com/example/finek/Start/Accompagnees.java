package com.example.finek.Start;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class  Accompagnees {
    private String documentId;
    private String nom;
    private String prenom;
    private String liaison;

    public Accompagnees() {
        //public no-arg constructor needed
    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Accompagnees(String nom,String prenom, String liaison) {
        this.nom = nom;
        this.prenom = prenom;
        this.liaison = liaison;
    }

    @NonNull
    @Override
    public String toString(){
        return nom + " " + prenom + " " +liaison;
    }

    public String getNom() {
        return nom;
    }
    public String getName() {
        return prenom;
    }

    public String getLiaison() {
        return liaison;
    }
}