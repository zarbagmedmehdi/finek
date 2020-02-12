package com.example.finek;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.example.finek.Service.AccompagneService;
import com.example.finek.Service.DisparitionService;
import com.example.finek.Service.ImageService;
import com.example.finek.util.DatePickerFragment;
import com.example.finek.util.TimePickerFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class DeclarerPerduActivity extends AppCompatActivity {
    public  static int[] tabDate = new int[6];
    Button dateBtn;
    String id;
AccompagneService ac=new AccompagneService();
    Button timeBtn;
    ImageService imageService;
    DisparitionService ds=new DisparitionService();
    Button btnChoose;
    Button btnValider;
    ImageView imageView1;
    ImageView imageView2;
    EditText editTextDescription;
    Date date;
    TextView t;
    private final int PICK_IMAGE_REQUEST = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_declarer_perdu);
        Intent intent = getIntent();

         id = intent.getStringExtra("id");
        t=findViewById(R.id.NomPerdu);
        dateBtn=(Button)findViewById(R.id.dateBtn);
        timeBtn=(Button)findViewById(R.id.timeBtn);
        btnChoose=(Button)findViewById(R.id.btnChoose);
        btnValider=(Button)findViewById(R.id.validerButton);
        imageView1=findViewById(R.id.imgView1);
        imageView2=findViewById(R.id.imgView2);
        ac.getAccompagne(id,t);

       // editTextDescription=findViewById(R.id.editTextDescription);
        imageService=new ImageService( btnChoose,  btnValider,  imageView1,  imageView2);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();

            }
        });
    }
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment(timeBtn,1);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment(dateBtn,1);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageService.activityResult(requestCode, resultCode, data,getContentResolver());

    }

    public void valider(View v) throws ParseException {
        if(testDate() & !editTextDescription.getText().toString().equals("")){
            String sDate6 = tabDate[2]+"-"+tabDate[1]+"-"+tabDate[0]+" "+tabDate[3]+":"+tabDate[4]+":"+tabDate[5];
            SimpleDateFormat formatter6=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date6=formatter6.parse(sDate6);
            Timestamp ts=new Timestamp(date6);

            ds.createDisparition( editTextDescription.getText().toString(), ts, id, imageView1,  imageView2,DeclarerPerduActivity.this);
        }
        else {
            Toast.makeText(DeclarerPerduActivity.this, "remplir les données avant de valider", Toast.LENGTH_SHORT).show();

        }

    }

    public Boolean testDate(){
        Boolean t=true;
        for(int i=0;i<5;i++) {
            if (tabDate[i] == 0) t = false;
        }
        return t;

    }

}