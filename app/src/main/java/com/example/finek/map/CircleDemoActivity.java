
package com.example.finek.map;

import com.example.finek.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tooltip.Tooltip;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.finek.R.string.map_circle_description;


/**
 * This shows how to draw circles on a map.
 */

public class CircleDemoActivity extends AppCompatActivity
        implements OnSeekBarChangeListener, OnMarkerDragListener, OnMapLongClickListener,
        OnItemSelectedListener,OnMyLocationButtonClickListener,OnMyLocationClickListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    public boolean des_clicked=false;


    //db/////
    public static String id="";
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    //////////////////////////////////////////////

    ///alert dialogue
    private AlertDialog alertDialog;

    //zone
    Map<String, Object> zone = new HashMap<>();


    private CollectionReference accRef;
    private static final int POLYGON_STROKE_WIDTH_PX = 8;

    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int COLOR_WHITE_ARGB = 0xffffffff;
    private static final int COLOR_GREEN_ARGB = 0xff388E3C;
    private static final int COLOR_PURPLE_ARGB = 0xff81C784;
    private static final int COLOR_ORANGE_ARGB = 0xff0B507B;
    private static final int COLOR_BLUE_ARGB = 0xff3EA6F0;
    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    //map
    private GoogleMap mMap;

    //random var
    private final Random mRandom = new Random();
    //polygon points
    public List<LatLng> polygon = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circle_demo);


        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();

        id = intent.getStringExtra("id");
          accRef=db.collection("accompagne").document(id).collection("zones");

        //txt_zone
        final TextView txt_zone =findViewById(R.id.txt_zone) ;
        //bouton help : pour expliquer les etapes comment dessiner
        final ImageButton btn_help = findViewById(R.id.btn_help);
        //bouton draw : pour dessiner le plygon
        Button btn_des = findViewById(R.id.btn_des);
        //bouton supp : pour effacer les points de la carte
        Button btn_sup = findViewById(R.id.btn_sup );
        //bouton save :  enregister la zone
        ImageButton btn_save = findViewById(R.id.btn_save);

        btn_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tooltip tooltip=new Tooltip.Builder(btn_help)
                        .setText("etape1: long click sur la carte pour creer des marqueurs \n etape2: long click sur chaque marquer pour le positioner \n etape3 :cliquez sur les marqueurs de l'orde convenable \n etape4 : Cliquer sur le bouton déssiner")
                        .setGravity(Gravity.BOTTOM)
                        .setCornerRadius(12f)
                        .setDismissOnClick(true)
                        .setBackgroundColor(0xFFB7D1EE)
                        .show();
            }
        });

        btn_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                des_clicked=true;
                if (!polygon.isEmpty()){
                    Polygon polygon_des;
                polygon_des = mMap.addPolygon(new PolygonOptions().addAll(polygon));
                polygon_des.setTag("beta");
                stylePolygon(polygon_des);
                }
                else Toast.makeText(CircleDemoActivity.this, "Dessinez d'abord votre zone en suivant les étapes(bouton comment?)", Toast.LENGTH_LONG).show();

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(des_clicked) {

                    zone.put("points", polygon);
                    if (TextUtils.isEmpty(txt_zone.getText())) {
                        Toast.makeText(CircleDemoActivity.this, "Veuillez ecrire le nom de la zone", Toast.LENGTH_LONG).show();
                    } else {
                        //test if the zone name exist?
                        accRef.document(txt_zone.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        // Log.d(TAG, "Document exists!");
                                        //alert to modify or cancel
                                        alertDialog = new AlertDialog.Builder(CircleDemoActivity.this)
                                                //set icon
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                //set title
                                                .setTitle("la zone existe déja pour cette personne")
                                                //set message
                                                .setMessage("vous voulez modifier cette zone ?")
                                                //set positive button
                                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        //set what would happen when positive button is clicked

                                                        accRef.document(String.valueOf(txt_zone.getText())).update(zone).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Toast.makeText(CircleDemoActivity.this,"la zone a été modifiée avec succes",Toast.LENGTH_LONG).show();
                                                                des_clicked=false;
                                                                polygon.clear();
                                                                mMap.clear();
                                                                txt_zone.setText("");

                                                            }
                                                        });
                                                    }
                                                })
//set negative button
                                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        //set what should happen when negative button is clicked
                                                        polygon.clear();

                                                    }
                                                })
                                                .show();


                                    } else {
                                        // Log.d(TAG, "Document does not exist!");
                                        accRef.document(String.valueOf(txt_zone.getText())).set(zone).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(CircleDemoActivity.this,"La Zone a été ajoutée avec succes",Toast.LENGTH_LONG).show();
                                                des_clicked=false;
                                                polygon.clear();
                                                mMap.clear();
                                                txt_zone.setText("");

                                            }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(CircleDemoActivity.this,"Error!",Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                    }
                                } else {
                                    Log.d("TAG", "Failed with: ", task.getException());
                                }
                            }
                        });
                    }

                }else Toast.makeText(CircleDemoActivity.this, "Vueillez dessiner un zone sur la carte!", Toast.LENGTH_LONG).show();

            }
        });

        btn_sup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                des_clicked=false;
                polygon.clear();
                mMap.clear();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap map) {
        // Override the default content description on the view, for accessibility mode.
        map.setContentDescription(getString(map_circle_description));

        mMap = map;

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);


    }

    //cette methode va decorer le polygon(couleur ...)
    public static void stylePolygon(Polygon polygon) {
        String type = "";
        // Get the data object stored with the polygon.
        if (polygon.getTag() != null) {
            type = polygon.getTag().toString();
        }

        List<PatternItem> pattern = null;
        int strokeColor = COLOR_BLACK_ARGB;
        int fillColor = COLOR_WHITE_ARGB;

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "alpha":
                // Apply a stroke pattern to render a dashed line, and define colors.
                strokeColor = COLOR_GREEN_ARGB;
                fillColor = COLOR_PURPLE_ARGB;
                break;
            case "beta":
                // Apply a stroke pattern to render a line of dots and dashes, and define colors.
                strokeColor = COLOR_ORANGE_ARGB;
                fillColor = COLOR_BLUE_ARGB;
                break;
        }


        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX);
        polygon.setStrokeColor(strokeColor);
        polygon.setFillColor(fillColor);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Don't do anything here.
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // Don't do anything here.
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Don't do anything here.
    }



    @Override
    public void onMarkerDragStart(Marker marker) {
        onMarkerMoved(marker);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        onMarkerMoved(marker);
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        onMarkerMoved(marker);
    }

    private void onMarkerMoved(Marker marker) {


    }

    @Override
    public void onMapLongClick(LatLng point) {


        //ajout de marker
        Marker marker;
        marker= mMap.addMarker(new MarkerOptions()
                .position(point)
                .title("point")
                .draggable(true));


    }



    /**
     * Verifier si les pemission sont garantis
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {

        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "emplacement actuel:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.setIcon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        polygon.add(marker.getPosition());
        return false;
    }


}