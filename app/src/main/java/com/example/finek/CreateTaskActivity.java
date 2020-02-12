package com.example.finek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.finek.Service.AccompagneService;
import com.example.finek.Service.DisparitionService;
import com.example.finek.Service.ImageService;
import com.example.finek.Service.TaskService;
import com.example.finek.util.DatePickerFragment;
import com.example.finek.util.DateUtil;
import com.example.finek.util.Record;
import com.example.finek.util.TimePickerFragment;
import com.google.firebase.Timestamp;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateTaskActivity extends AppCompatActivity {
Switch swit;
EditText nom,description;
    public  static int[] tabDate = new int[6];
    Button dateBtn;
    String id;
    AccompagneService ac=new AccompagneService();
    Button timeBtn;
    ImageService imageService;
    DisparitionService ds=new DisparitionService();
    Button ecouter,enregistrer;
    Button btnValider;
    TaskService taskService=new TaskService();
    Record r;

    public  final String LOG_TAG = "AudioRecordTest";
    public  final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    public  String fileName = null;

    public MediaRecorder recorder = null;

    public MediaPlayer player = null;

    // Requesting permission to RECORD_AUDIO
    public  boolean permissionToRecordAccepted = false;
    public String [] permissions = {Manifest.permission.RECORD_AUDIO};
    public boolean mStartRecording = true;
    public boolean mStartPlaying = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
        r=new Record();
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        taskService.getTasks(id);
        ActivityCompat.requestPermissions(this, permissions, 200);
        ecouter=findViewById(R.id.ecouter);
        enregistrer=findViewById(R.id.enregistrer);
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest.3gp";

        swit=findViewById(R.id.switch1);
        dateBtn=(Button)findViewById(R.id.dateBtn1);
        timeBtn=(Button)findViewById(R.id.timeBtn1);
        btnValider=(Button)findViewById(R.id.validerButton);
        nom=findViewById(R.id.editTextNomTache);
        description=findViewById(R.id.editTextDescriptionTache);
        swit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println(isChecked);
               if(isChecked){ dateBtn.setVisibility(View.GONE); }
               else {dateBtn.setVisibility(View.VISIBLE); }
            }
        });
    }

    public void creer(View view) throws ParseException {
     Boolean testDate=false;
        if(swit.isChecked()){ testDate= testDate(3); }
        else { testDate= testDate(0); }
        if(!nom.getText().toString().equals("") && !description.getText().toString().equals("") && testDate)
        {

            Timestamp ts =DateUtil.toTimstamp(tabDate, swit.isChecked());
            taskService.createTask(description.getText().toString(),nom.getText().toString(),swit.isChecked(),ts,id,getApplicationContext());
        }
else{
            Toast.makeText(getApplicationContext(),"cc",Toast.LENGTH_LONG).show();
        }

    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment(timeBtn,2);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(dateBtn,2);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    public Boolean testDate(int debut){
        Boolean t=true;
        for(int i=debut;i<5;i++) {
            if (tabDate[i] == 0){
                t = false;
            }
        }
        return t;

    }}

    /*
    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 200:
                r.permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!r.permissionToRecordAccepted ) finish();

    }
    @Override
    public void onStop() {
        super.onStop();
        if (r.recorder != null) {
            r.recorder.release();
            r.recorder = null;
        }

        if (r.player != null) {
            r.player.release();
            r.player = null;
        }
    }

    public void enregistrer(View view) {
       r.onRecord(r.mStartRecording);
        if (r.mStartRecording) {
            enregistrer.setText("Stop recording");
        } else {
            enregistrer.setText("Start recording");
        }
        r.mStartRecording = !r.mStartRecording;
    }

    public void ecouter(View view) {
        r.onPlay( r.mStartPlaying);
        if ( r.mStartPlaying) {
            ecouter.setText("Stop playing");
        } else {
            ecouter.setText("Start playing");
        }
        r.mStartPlaying = ! r.mStartPlaying;
    }
}

     */
