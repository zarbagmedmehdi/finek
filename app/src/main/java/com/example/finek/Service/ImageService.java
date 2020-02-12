package com.example.finek.Service;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ImageService {
    private Button btnChoose, btnUpload;
    private ImageView imageView1,imageView2;
    private ArrayList<ImageView> imageViews=new ArrayList<>();
    private final int PICK_IMAGE_REQUEST = 10;
    private Uri filePath;
    public  FirebaseStorage storage;
    public  StorageReference storageReference;

    public ImageService() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public ImageService(Button btnChoose, Button btnUpload, ImageView imageView1, ImageView imageView2) {
        this.btnChoose = btnChoose;
        this.btnUpload = btnUpload;
        this.imageView1 = imageView1;
        this.imageView2 = imageView2;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        btnUpload.setVisibility(View.GONE);
        imageView1.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                supprimerPhoto(1);
                return true;
            }
        });
        imageView2.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                supprimerPhoto(2);
                return true;
            }
        });





    }


    private void setPhotots(Bitmap b){
        if(imageView1.getDrawable() == null){
            imageView1.setImageBitmap(b);

        }
        else  if(imageView2.getDrawable() == null){
            imageView2.setImageBitmap(b);


        }

        else
            System.out.println("no");
        ButtonActivate();
    }
    public Boolean testImage(ImageView m){
        if(m.getDrawable() != null){
            return true;
        }
        else return false;

    }
    public void activityResult(int requestCode, int resultCode, Intent data, ContentResolver c){
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(c, filePath);

                setPhotots(bitmap );                }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    public void  supprimerPhoto(int cas){
        if (cas==1){
            if (testImage(imageView2)) {
                Drawable image2 = imageView2.getDrawable();
                imageView1.setImageDrawable(image2);
                imageView2.setImageDrawable(null);

            }
            else { imageView1.setImageDrawable(null); }
            ButtonActivate();
        }
        else if(cas==2){

            imageView2.setImageDrawable(null);
            ButtonActivate();
        }
    }
    public void ButtonActivate(){
        if (testImage(imageView2) && testImage(imageView1) ){
            btnUpload.setVisibility(View.VISIBLE);btnChoose.setVisibility(View.GONE);}
        else     {    btnUpload.setVisibility(View.GONE);btnChoose.setVisibility(View.VISIBLE);}



    }




}
