package com.example.finek.map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;


import com.example.finek.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class LastLocation extends AppCompatActivity   implements GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, OnMapReadyCallback {
    private GoogleMap mMap;
    //connect to firestore data base
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    //getting the document of the specified user
    private DocumentReference LastLocation = db.collection("accompagne").document(Traitement.user);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_location);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
      //getting last location from data base of the specified user
        LastLocation.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                float zoomLevel = 16.0f; //This goes up to 21
                Map<String, Object> location = (Map<String, Object>) documentSnapshot.get("LastLocation");
                LatLng latLng = new LatLng((Double) location.get("latitude"), (Double) location.get("longitude"));
              //  Toast.makeText(getApplicationContext(),latLng.toString(),Toast.LENGTH_SHORT).show();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                googleMap.addMarker(new MarkerOptions().position(latLng)
                        .title("le deriner emplacement de "+Traitement.user));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            }
        });





    }

}
