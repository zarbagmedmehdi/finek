package com.example.finek.Bean;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Disparition {

    public Disparition(int id, Date vu_le, String description, Accompane accompane) {
        this.id = id;
        this.vu_le = vu_le;
        this.description = description;
        this.accompane = accompane;
    }

    public Disparition() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getVu_le() {
        return vu_le;
    }

    public void setVu_le(Date vu_le) {
        this.vu_le = vu_le;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Accompane getAccompane() {
        return accompane;
    }

    public void setAccompane(Accompane accompane) {
        this.accompane = accompane;
    }
    private int id;
    private Date vu_le;
    private String description;
    private Accompane accompane;


}