package com.example.pfamonument;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pfamonument.Model.Images;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import static com.example.pfamonument.R.drawable.*;

public class Description extends AppCompatActivity {

    TextView mTitle, mDetail, mTeste;
    ImageView mImage, mFav;
    boolean test;

    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Favoris");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description);

        //Action Bar
        ActionBar actionBar = getSupportActionBar();
        //Actionbar titre
        actionBar.setTitle("Detail Place");
        //set back button in action bar
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //initialisations des views
        mTitle = findViewById(R.id.title);
        mDetail = findViewById(R.id.description);
        mImage = findViewById(R.id.imageView);
        mFav = findViewById(R.id.fav);

        //get data from intent
        byte[] bytes = getIntent().getByteArrayExtra("image");
        final String title = getIntent().getStringExtra("title");
        final String desc = getIntent().getStringExtra("description");
        final Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        final String testFav = getIntent().getStringExtra("teste");
        final String latitude = getIntent().getStringExtra("latitude");
        final String longitude = getIntent().getStringExtra("longitude");
        final String image = getIntent().getStringExtra("imageV");

        //set data to views
        mTitle.setText(title);
        mDetail.setText(desc);
        mImage.setImageBitmap(bmp);
        //mImage.setImage(image);

        if(testFav == "faux")
        {
            mFav.setImageDrawable(getResources().getDrawable(R.drawable.fav1));
            test = false;
        } else if(testFav == "vrai")
        {
            mFav.setImageDrawable(getResources().getDrawable(R.drawable.fav2));
            test = true;
        }

        mFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(test == false) {
                    mFav.setImageDrawable(getResources().getDrawable(R.drawable.fav2));
                    test = true;
                    //String img = bmp.toString().trim();
                    ajouterFavoris(title, desc, image, testFav, latitude, longitude);
                } else {
                    mFav.setImageDrawable(getResources().getDrawable(R.drawable.fav1));
                    test = false;
                }
            }
        });
    }

    private void ajouterFavoris(String title, String description, String bmp, String teste, String latitude, String longitude) {

        double latit = Double.parseDouble(latitude);
        double longit = Double.parseDouble(longitude);
        Images imageUploadedInfo = new Images(title, bmp, description, teste, latit, longit);
        //getting image upload id
        String imageUploadId = mDatabase.push().getKey();
        //ajouter l'id de l'image telecharger à databaseReference
        mDatabase.child(imageUploadId).setValue(imageUploadedInfo);
    }

//handle onBackPressed(go to previous activity)
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
