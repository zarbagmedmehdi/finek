package com.example.finek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finek.Bean.Tache;
import com.example.finek.Service.TaskService;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.sql.Timestamp;
import java.util.ArrayList;

public class GetTaskActivity extends AppCompatActivity {
    TaskService taskService=new TaskService();
    String id;
RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    FirestoreRecyclerAdapter<Tache, TaskViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_task);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        taskService.getTasks(id);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager


        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();



        Query query = rootRef.collection("tache")
                .whereEqualTo("id_accompagne", id);
        FirestoreRecyclerOptions<Tache> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<Tache>()
                .setQuery(query, Tache.class)
                .build();


        adapter = new FirestoreRecyclerAdapter<Tache, TaskViewHolder>(firestoreRecyclerOptions) {

            @Override
            public void onDataChanged() {
                // do your thing
                if(getItemCount() == 0){
                    LinearLayout l=findViewById(R.id.linearLayout);
                    l.setVisibility(View.INVISIBLE);
                    findViewById(R.id.imageView).setVisibility(View.VISIBLE);
                }
                else{
                    LinearLayout l=findViewById(R.id.linearLayout);
                    l.setVisibility(View.VISIBLE);
                    findViewById(R.id.imageView).setVisibility(View.INVISIBLE);
                }

            }

            @Override
                    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.task_list_item, parent, false);

                        return new TaskViewHolder(view);
                    }


                    @Override
                    protected void onBindViewHolder(TaskViewHolder holder, final int position, Tache model) {
                        holder.bindTache(model);
                        holder.setOnClickListener(new TaskViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                             TextView d= findViewById(R.id.descriptionTache);
                             d.setText("description : "+ getSnapshots().getSnapshot(position).get("description").toString());

                            }

                            @Override
                            public void onItemLongClick(View view,final int position) {

                                AlertDialog.Builder alert = new AlertDialog.Builder(GetTaskActivity.this,R.style.AlertDialogCustom);
                                alert.setTitle("Supprimer la tache");
                                alert.setMessage("vous voulez vraimenet supprimer?");
                                alert.setPositiveButton("oui", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getSnapshots().getSnapshot(position).getReference().delete();
                                        dialog.dismiss();
                                    }
                                });

                                alert.setNegativeButton("non", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                alert.show();                            }
                        });
                    }




                    };
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter.startListening();



        System.out.println(adapter);



        // specify an adapter (see also next example)
    //    mAdapter = new TaskAdapter(taches);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        System.out.println("start");
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }

    public void creer(View view) {
        Intent myIntent = new Intent(GetTaskActivity.this, CreateTaskActivity.class);
        myIntent.putExtra("id", id); //Optional parameters
        GetTaskActivity.this.startActivity(myIntent);
    }
}
