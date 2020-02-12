package com.example.finek.map;


import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.maps.android.PolyUtil;


public class Traitement extends Service {


    //connecting to data base
    public static String user="V7jkPcxKCWf02jtdGbc4x7GYXix1";
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference accRef=db.collection("accompagne").document(user).collection("zones");
    private DocumentReference LastLocation = db.collection("accompagne").document(user);
    private  Map<String, Location> Location=new HashMap<>();
    ///areaa
    List<LatLng> areaRegion = new ArrayList<>();
    //list of documents contains Areas
    ArrayList<String> listOfArea=new ArrayList<String>();

    static int count=0;
    private LocationListener listener;
    private LocationManager locationManager;


    //notifications
    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {
        super.onCreate();
        //notifications
        mRequestQue = Volley.newRequestQueue(getApplicationContext());

        Toast.makeText(getApplicationContext(), count+"", Toast.LENGTH_SHORT).show();
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(getApplicationContext(), "onL", Toast.LENGTH_SHORT).show();

                //last Location
                Location.put("LastLocation",location);
                LastLocation.set(Location);

                //test if is inside ou non
                boolean isInside=true;
                accRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        listOfArea.clear();
                        for (DocumentSnapshot document : documents) {

                            listOfArea.add(document.getId().toString());
                        }
                    }
                });
                //getting the list of areas for the user specified
                for (String Area : listOfArea) {
                    Toast.makeText(getApplicationContext(), Area, Toast.LENGTH_SHORT).show();
                    System.out.println(Area);

                    accRef.document(Area).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                // Document found in the offline cache
                                DocumentSnapshot document = task.getResult();
                                areaRegion.clear();
                                List<Object> locations = (List<Object>) document.get("points");
                                for (Object locationObj : locations) {
                                    Map<String, Object> location = (Map<String, Object>) locationObj;
                                    LatLng latLng = new LatLng((Double) location.get("latitude"), (Double) location.get("longitude"));
                                    areaRegion.add(latLng);
                                }

                            }
                        }
                    });

                    if (location != null & !areaRegion.isEmpty()) {
                        Boolean inside = PolyUtil.containsLocation(location.getLatitude(), location.getLongitude(), areaRegion, true);
                        // if the person is inside his Areas
                        if (inside == false) {
                            isInside=false;
                        }
                    }
                }
                if(isInside)
                {
                    count=0;
                    Toast.makeText(getApplicationContext(), "on est à l'interieur", Toast.LENGTH_SHORT).show();
                    System.out.println("int");}
                else{
                    count++;
                    if (count == 1){
                        SmsManager.getDefault().sendTextMessage("555", null, "la personne "+Traitement.user+"est sortie des zone spécifiées ", null, null);
                        sendNotification();
                    }
                    Toast.makeText(getApplicationContext(), "on est à l'exterieur", Toast.LENGTH_SHORT).show();
                    System.out.println("ext");}
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        //noinspection MissingPermission
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        // specify the min time and min distance
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, listener);
    }



    private void sendNotification() {

        JSONObject json = new JSONObject();

        try {
            json.put("to","/topics/"+"lala");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","Notification");
            notificationObj.put("body","La personne est hors des zones spécifiées");





            json.put("data",notificationObj);



            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    json,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("MUR", "onResponse: ");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("MUR", "onError: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization","key=AIzaSyAy06twL__KcFW6qfPudnjizqle681t-iw");
                    return header;
                }
            };
            mRequestQue.add(request);
        }
        catch (JSONException e)

        {
            e.printStackTrace();
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(listener);
    }
}