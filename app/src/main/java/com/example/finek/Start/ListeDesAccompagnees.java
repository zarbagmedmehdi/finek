package com.example.finek.Start;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.finek.AccoProfil;
import com.example.finek.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ListeDesAccompagnees extends Responsable{
    public static final String TAG = "TAG";

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    DocumentReference documentReference;
    ListView listeViewAccompagnees;
    ArrayList<Pair> accompagnees;
    private Map<String, Object> mMapItems;                      // original data source of all items
    private List<Map.Entry<String, Object>> mListOfMapEntries;  // list of entries from mMapItems
    private MapEntryListAdapter mMapEntryListAdapter;
    Object obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_des_accompagnees);

        drawer();

        listeViewAccompagnees = findViewById(R.id.listeViewAccompagnees);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();
        System.out.println(userId);

        listeViewAccompagnees.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         String ids=((TextView)view.findViewById(R.id.idL)).getText().toString();
                Intent intent;
                intent = new Intent(ListeDesAccompagnees.this, AccoProfil.class);
                intent.putExtra("id", ids);

                startActivity(intent);
            }


        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.whiteTextColor));
        navigationView.setCheckedItem(R.id.nav_profile);
    }

    public void onClick(View view) {
        final Map<Integer,Accompagnees> accompagne = new HashMap<>();
       // [START get_all_users]
        mMapItems = new LinkedHashMap<>();
        Toast.makeText(ListeDesAccompagnees.this,"catch" , Toast.LENGTH_SHORT).show();
        fStore.collection("accompagne").whereEqualTo("id_responsable", userId)
               .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Integer i = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Toast.makeText(ListeDesAccompagnees.this,document.getData() + "" , Toast.LENGTH_SHORT).show();
                                accompagne.put(0,new Accompagnees(document.get("nom").toString(),document.get("prenom").toString(),document.get("liaison").toString()));
                                obj = new Accompagnees(document.get("nom").toString(),document.get("prenom").toString(),document.get("liaison").toString()).toString();
                                mMapItems.put(document.getId(),obj);
                                i++;
                            }


                            mListOfMapEntries = new ArrayList<> (mMapItems.entrySet ());    // create the list

                            mMapEntryListAdapter = new MapEntryListAdapter (ListeDesAccompagnees.this, mListOfMapEntries);

                            ListView listView = findViewById (R.id.listeViewAccompagnees);
                            listView.setAdapter (mMapEntryListAdapter);


                        } else {
                            System.out.println( "Error getting documents."+ task.getException());
                            Toast.makeText(ListeDesAccompagnees.this,"No such " , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        // [END get_all_users]
     }

    public void onListItemClick(View v) {
      //  String contactId = ((TextView) v.findViewById(android.R.id.text1)).getText().toString();
        Intent intent;
        intent = new Intent(ListeDesAccompagnees.this, Accompagnee.class);
        //intent.putExtra("accId", contactId);
        startActivity(intent);
    }

}

