package com.example.finek.Bean;

import com.example.finek.util.DateUtil;
import com.google.firebase.firestore.ServerTimestamp;

import java.sql.Timestamp;
import java.util.Date;

public class Tache {
    String uid;
    String nom;
    String description;
    String idAccompagné;
    String type;
    @ServerTimestamp
    private Date date;
    public Tache() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdAccompagné() {
        return idAccompagné;
    }

    public void setIdAccompagné(String idAccompagné) {
        this.idAccompagné = idAccompagné;
    }

    public String getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Tache{" +
                "id='" + uid + '\'' +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", idAccompagné='" + idAccompagné + '\'' +
                ", type='" + type + '\'' +
                ", date=" + date +
                '}';
    }
}
