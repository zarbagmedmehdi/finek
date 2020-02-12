package com.example.finek.Start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.finek.MainActivity;
import com.example.finek.R;

public class PagePrimaire extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_primaire);
    }
    public void connexionResponsable(View view){
        Intent intent = new Intent(this,Authentification.class);
        startActivity(intent);
    }
    public void connexionAccompagnee(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
