package com.example.pfamonument;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pfamonument.Model.Images;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;

public class Favoris extends Fragment {

    RecyclerView mRecyclerView;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mRef;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.favoris, container, false);

        //RecyclerView
        mRecyclerView = rootView.findViewById(R.id.recyclerViewFavoris);
        mRecyclerView.setHasFixedSize(true);
        //set layout as LinearLayout
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //send Query to firebase database
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mRef = mFirebaseDatabase.getReference("Favoris");

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Images,ViewHolderFavoris> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Images, ViewHolderFavoris>(Images.class, R.layout.listfavoris, ViewHolderFavoris.class, mRef) {
                    @Override
                    protected void populateViewHolder(ViewHolderFavoris viewHolderFavoris, Images model, int position) {
                        viewHolderFavoris.setDetails(getActivity().getApplicationContext(), model.getTitle(), model.getDescription(), model.getImage());
                    }

                    @Override
                    public ViewHolderFavoris onCreateViewHolder(ViewGroup parent, int viewType) {

                        ViewHolderFavoris viewHolderFavoris = super.onCreateViewHolder(parent, viewType);
                        viewHolderFavoris.setOnClickListener(new ViewHolderFavoris.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //Views
                                TextView mTitle = view.findViewById(R.id.favTitle);
                                TextView mDescription = view.findViewById(R.id.favDescription);
                                ImageView mImageView = view.findViewById(R.id.favImageView);
                                TextView mTest = view.findViewById(R.id.favTest);

                                //get data from views
                                String mTitre = mTitle.getText().toString();
                                String mDesc = mDescription.getText().toString();
                                Drawable mDrawable = mImageView.getDrawable();
                                Bitmap mBitmap = ((BitmapDrawable)mDrawable).getBitmap();
                                String mTestFav = mTest.getText().toString();

                                //passer les données vers une nouvelle activity
                                Intent intent = new Intent(view.getContext(), Description.class);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                byte[] bytes = stream.toByteArray();
                                intent.putExtra("image", bytes); //put bitmap image as array of bytes
                                intent.putExtra("title", mTitre); //put titre
                                intent.putExtra("description", mDesc); //put description
                                intent.putExtra("test", mTestFav);
                                startActivity(intent); //start activity

                            }

                        });
                        return viewHolderFavoris;
                    }
                };
        //set adapter to recyclerView
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
