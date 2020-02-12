package com.example.finek.Start;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.finek.R;
import com.example.finek.Service.Functions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.WriterException;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;


public class AddAccompagnee extends Responsable{

    public static final String TAG = "TAG";
    EditText editTextLastName, editTextFirstName, editTextMobile, editTextLiaison;
    RadioButton radioButtonF,radioButtonM;
    RadioGroup radioGroup;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId, sexe, str;
    DocumentReference documentReference;

    ImageView qrImage;
    Button start, save;
    String inputValue;
    String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    Bitmap bitmap;
    QRGEncoder qrgEncoder;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    private final int PICK_IMAGE_REQUEST = 71;

    String idaccomp="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_accompagnee);
        drawer();

        editTextLastName = findViewById(R.id.editTextLastName);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLiaison= findViewById(R.id.editTextLiaison);
        editTextMobile = findViewById(R.id.editTextMobile);

        radioButtonF = findViewById(R.id.radioButtonFemale);
        radioButtonM = findViewById(R.id.radioButtonMale);

        radioGroup = findViewById(R.id.radio);
         qrImage=findViewById(R.id.QR_Image);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        userId = fAuth.getCurrentUser().getUid();
        documentReference = fStore.collection("accompagne").document(userId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.whiteTextColor));
        navigationView.setCheckedItem(R.id.nav_profile);
    }

    public void onClickAdd(View view) {
        final String lastName = editTextLastName.getText().toString();
        final String firstName = editTextFirstName.getText().toString();
        final String liaison = editTextLiaison.getText().toString();
        final String mobile = editTextMobile.getText().toString();

        final String sexe = findSelectedSexe();

        //int selectedId = radioGroup.getCheckedRadioButtonId();
        //radioButton = findViewById(selectedId);

        //final String sexe = (String) radioButton.getText();
        if (lastName.isEmpty()) {
            editTextLastName.setError("Entrez votre nom");
            editTextLastName.requestFocus();
        } else if (firstName.isEmpty()) {
            editTextFirstName.setError("Enterz votre prénom");
            editTextFirstName.requestFocus();
        }  else if (liaison.isEmpty()) {
            editTextLiaison.setError("Enterz votre Liaison");
            editTextLiaison.requestFocus();
        } else if (!Functions.isPhoneValid(mobile) || mobile.isEmpty()) {
            editTextMobile.setError("Numéro de téléphone Non valide");
            editTextMobile.requestFocus();
        } else {
            //updating artist
            Map<String, Object> accompagne = new HashMap<>();
            accompagne.put("id_responsable",userId);
            accompagne.put("nom", lastName);
            accompagne.put("prenom", firstName);
            accompagne.put("numTel", mobile);
            accompagne.put("sexe",sexe);
            accompagne.put("liaison", liaison);
            accompagne.put("etat", "");
            documentReference.set(accompagne).addOnSuccessListener( new OnSuccessListener<Void>() {

                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG,"onSuccess: user Profile is updated for "+ userId);
                    idaccomp=documentReference.getId();
                    create();
                    if (mUploadTask != null && mUploadTask.isInProgress()) {
                        Toast.makeText(AddAccompagnee.this, "Upload in progress", Toast.LENGTH_SHORT).show();
                    } else {
                        uploadImage();
                    }
                    str="Accompagnée ajoutée avec succès";
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: " + e.toString());
                    str="Accompagnée n'est pas ajoutée";
                }
            });
            Toast.makeText(this,str,Toast.LENGTH_LONG).show();
            accompagne.clear();

            editTextMobile.setText("");
            editTextLastName.setText("");
            editTextLiaison.setText("");
            editTextFirstName.setText("");

        }
    }

    public String findSelectedSexe() {
        RadioGroup rg = (RadioGroup)findViewById(R.id.radioButtonGroup);

        int option = rg.getCheckedRadioButtonId();
        String output = "";

        switch (option)
        {
            case R.id.radioButtonFemale: output = "Femme";
                break;
            case R.id.radioButtonMale: output = "Homme";
                break;
        }
        return output;
    }
private void create(){
    String   inputValue = "https://www.finek.com/find/"+idaccomp;

    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
    Display display = manager.getDefaultDisplay();
    Point point = new Point();
    display.getSize(point);
    int width = point.x;
    int height = point.y;
    int smallerDimension = width < height ? width : height;
    smallerDimension = smallerDimension * 3 / 4;

    qrgEncoder = new QRGEncoder(
            inputValue, null,
            QRGContents.Type.TEXT,
            smallerDimension);
    try {
        bitmap = qrgEncoder.encodeAsBitmap();
        qrImage.setImageBitmap(bitmap);
    } catch (WriterException e) {
        Log.v(TAG, e.toString());
    }

}
    private void uploadImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        qrImage.setDrawingCacheEnabled(true);
        qrImage.buildDrawingCache();
        Bitmap bitmap = qrImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference ref = mStorageRef.child("/qrImages/"+idaccomp);
        UploadTask u = ref.putBytes(data);
        u.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();

                Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(qrImage);
        }
    }

}


