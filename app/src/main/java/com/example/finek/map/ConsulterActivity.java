package com.example.finek.map;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.finek.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.finek.map.CircleDemoActivity.stylePolygon;


public class ConsulterActivity extends AppCompatActivity  implements OnMyLocationButtonClickListener, OnMyLocationClickListener,OnMapReadyCallback {

    //var to select doc
    String selected="";
    //for list
    List<String> S=new ArrayList<>();
    //tag
    private String TAG;
    //fb test
    private FirebaseFirestore db;
    private CollectionReference accRef;
    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE2 = 1;
    private boolean mPermissionDenied = false;
String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulter);
        db=FirebaseFirestore.getInstance();
        if(db==null){
            System.out.println("db null");
        }
        else {
            System.out.println("db not null");


        }
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();

        id = intent.getStringExtra("id");
        accRef=db.collection("accompagne").document(id).collection("zones");

        //fb liste des zones
        accRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        S.add(document.getId());
                        //list view
                        arrayAdapterListView(S);
                    }
                    //Toast.makeText(getApplicationContext(), S.toString(),Toast.LENGTH_LONG).show();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }

        });

    }


    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "emplacement actuel:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();
        db.collection("accompagne").document(Traitement.user).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                float zoomLevel = 16.0f; //This goes up to 21
                Map<String, Object> location = (Map<String, Object>) documentSnapshot.get("LastLocation");
                //  Toast.makeText(getApplicationContext(),latLng.toString(),Toast.LENGTH_SHORT).show();
                if(location!=null){
                    LatLng latLng = new LatLng((Double) location.get("latitude"), (Double) location.get("longitude"));

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
                        mMap.addMarker(new MarkerOptions().position(latLng)
                                .title("le deriner emplacement de " + Traitement.user));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    }

            }
        });

        //btns
        Button btn_voir = findViewById(R.id.btn_voir);
        Button btn_supp = findViewById(R.id.btn_supp);

        btn_voir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected.equals("")){
                    Toast.makeText(getApplicationContext(),"Choisssez une zone",Toast.LENGTH_LONG).show();
                }
                else {
                    accRef.document(selected).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                // Document found in the offline cache
                                DocumentSnapshot document = task.getResult();
                                ///
                                List<LatLng> areaRegion = new ArrayList<>();
                                List<Object> locations = (List<Object>) document.get("points");
                                for (Object locationObj : locations) {
                                    Map<String, Object> location = (Map<String, Object>) locationObj;
                                    LatLng latLng = new LatLng((Double) location.get("latitude"), (Double) location.get("longitude"));
                                    areaRegion.add(latLng);
                                    // Toast.makeText(getApplicationContext(), areaRegion.toString(), Toast.LENGTH_LONG).show();
                                }
                                //draw
                                mMap.clear();
                                Polygon polygon_des;
                                polygon_des = mMap.addPolygon(new PolygonOptions().addAll(areaRegion));
                                polygon_des.setTag("beta");
                                stylePolygon(polygon_des);
                                float zoomLevel = 16.0f; //This goes up to 21
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getPolygonCenterPoint(areaRegion), zoomLevel));
                            } else {
                                // Log.d(TAG, "Cached get failed: ", task.getException());
                            }
                        }
                    });

                }

}
});

        btn_supp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //stop
                stopService(new Intent(getApplicationContext(),Traitement.class));

                if (selected.equals("")) Toast.makeText(getApplicationContext(), "Choisissez une zone à supprimer", Toast.LENGTH_SHORT).show();
                else{
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ConsulterActivity.this);
                    alertDialogBuilder.setTitle("Vous voulez vraiment supprimer cette zonne");
                    alertDialogBuilder.setPositiveButton("oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //suprimer zone
                            accRef.document(selected).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error deleting document", e);
                                        }
                                    });

                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);


                        }
                    })
                            .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }
                //start
                startService(new Intent(getApplicationContext(),Traitement.class));
            }

        });
}


    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE2,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE2) {
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


    // This method use an ArrayAdapter to add data in ListView.
    private void arrayAdapterListView(List<String> S)
    {
        ListView listView = (ListView)findViewById(R.id.listViewExample);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, S);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object clickItemObj = parent.getAdapter().getItem(position);
                //Toast.makeText(ConsulterActivity.this, "You clicked " + clickItemObj.toString(), Toast.LENGTH_SHORT).show();
                selected=clickItemObj.toString();
            }
        });
    }

    private LatLng getPolygonCenterPoint(List<LatLng> polygonPointsList){
        LatLng centerLatLng = null;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(int i = 0 ; i < polygonPointsList.size() ; i++)
        {
            builder.include(polygonPointsList.get(i));
        }
        LatLngBounds bounds = builder.build();
        centerLatLng =  bounds.getCenter();

        return centerLatLng;
    }
}
